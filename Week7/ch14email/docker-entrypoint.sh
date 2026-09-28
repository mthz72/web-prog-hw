#!/bin/sh
# Chay truoc khi Tomcat khoi dong.
#
# Viec cua no: doc cac BIEN MOI TRUONG ma Render cung cap, roi SINH RA file
# context descriptor cua Tomcat chua thong tin connection pool that.
#
# Vi sao sinh file chu khong truyen bang -Dten=giatri qua CATALINA_OPTS:
#   1. Tomcat (VersionLoggerListener) IN TOAN BO command line argument ra log
#      luc khoi dong -> mat khau CSDL se nam nguyen van trong log cua Render,
#      ai vao dashboard cung doc duoc.
#   2. catalina.sh chay "eval" tren $CATALINA_OPTS ma khong dat trong nhay.
#      JDBC URL cua MySQL co ky tu & -> shell cat lenh ngay tai do va Tomcat
#      khong khoi dong duoc.
# Sinh file tranh duoc ca hai.
#
# File duoc ghi vao conf/Catalina/localhost/ROOT.xml - day la context
# descriptor rieng cua ung dung ROOT, va no DE LEN META-INF/context.xml nam
# trong war. Nho vay META-INF/context.xml van giu cau hinh MySQL o may cho ai
# muon deploy war thu cong, con trong container thi dung file sinh ra o day.
set -e

# --- 1. Tim chuoi ket noi -------------------------------------------------
# Render tao san DATABASE_URL dang:  postgresql://user:pass@host:5432/dbname
# JDBC lai can dang:                 jdbc:postgresql://host:5432/dbname
# Neu da tu dat JDBC_URL thi ton trong, khong dung toi DATABASE_URL.
if [ -z "$JDBC_URL" ] && [ -n "$DATABASE_URL" ]; then
    _rest="${DATABASE_URL#*://}"                  # bo "postgresql://"
    if [ "$_rest" != "${_rest#*@}" ]; then        # con phan "user:pass@"
        _creds="${_rest%%@*}"
        _hostpart="${_rest#*@}"
        [ -z "$DB_USERNAME" ] && DB_USERNAME="${_creds%%:*}"
        [ -z "$DB_PASSWORD" ] && DB_PASSWORD="${_creds#*:}"
    else
        _hostpart="$_rest"
    fi
    JDBC_URL="jdbc:postgresql://${_hostpart}"
fi

# Khong co cau hinh gi -> quay ve MySQL o may, de chay thu bang Docker local
if [ -z "$JDBC_URL" ]; then
    JDBC_URL="jdbc:mysql://localhost:3306/murach?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"
    [ -z "$DB_USERNAME" ] && DB_USERNAME="murach_user"
    [ -z "$DB_PASSWORD" ] && DB_PASSWORD="sesame"
fi

# --- 2. Doan driver tu JDBC URL ------------------------------------------
if [ -z "$DB_DRIVER" ]; then
    case "$JDBC_URL" in
        jdbc:postgresql:*) DB_DRIVER="org.postgresql.Driver" ;;
        jdbc:mysql:*)      DB_DRIVER="com.mysql.cj.jdbc.Driver" ;;
        *) echo "Khong doan duoc JDBC driver tu: $JDBC_URL" >&2; exit 1 ;;
    esac
fi

# --- 3. Kich thuoc pool ---------------------------------------------------
# Render Postgres goi free gioi han so ket noi dong thoi, va instance free hay
# bi khoi dong lai; de pool nho cho an toan.
: "${DB_MAX_TOTAL:=10}"
: "${DB_MAX_IDLE:=5}"

# --- 4. Sinh context descriptor ------------------------------------------
# Gia tri duoc nhet vao XML nen phai escape & < > " truoc, neu khong JDBC URL
# cua MySQL (co ky tu &) se lam hong file XML.
xml_escape() {
    printf '%s' "$1" | sed -e 's/&/\&amp;/g' -e 's/</\&lt;/g' -e 's/>/\&gt;/g' -e 's/"/\&quot;/g'
}

CTX_DIR=/usr/local/tomcat/conf/Catalina/localhost
mkdir -p "$CTX_DIR"

cat > "$CTX_DIR/ROOT.xml" <<XMLEOF
<?xml version="1.0" encoding="UTF-8"?>
<!-- File nay do docker-entrypoint.sh sinh ra luc khoi dong. Dung sua tay. -->
<Context>
    <Resource name="jdbc/murach" auth="Container" type="javax.sql.DataSource"
              driverClassName="$(xml_escape "$DB_DRIVER")"
              url="$(xml_escape "$JDBC_URL")"
              username="$(xml_escape "$DB_USERNAME")"
              password="$(xml_escape "$DB_PASSWORD")"
              maxTotal="$(xml_escape "$DB_MAX_TOTAL")"
              maxIdle="$(xml_escape "$DB_MAX_IDLE")"
              maxWaitMillis="10000"
              testOnBorrow="true" validationQuery="SELECT 1"
              logAbandoned="true" removeAbandonedOnBorrow="true"
              removeAbandonedTimeout="60" />
</Context>
XMLEOF
chmod 600 "$CTX_DIR/ROOT.xml"

# Chi in nhung gi khong phai bi mat
echo "[entrypoint] JDBC_URL = $JDBC_URL"
echo "[entrypoint] driver   = $DB_DRIVER"
echo "[entrypoint] user     = $DB_USERNAME"
echo "[entrypoint] pool     = maxTotal=$DB_MAX_TOTAL maxIdle=$DB_MAX_IDLE"
echo "[entrypoint] port     = ${PORT:-8080}"

# --- 5. Giao lai cho Tomcat ----------------------------------------------
# Chi con port o command line: khong phai bi mat, khong chua ky tu dac biet.
CATALINA_OPTS="-Dport.http=${PORT:-8080}"
export CATALINA_OPTS

exec catalina.sh run
