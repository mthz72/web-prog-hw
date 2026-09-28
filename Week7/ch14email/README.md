# Week 7 - Chuong 14: How to use JavaMail to send email

Ung dung "Join our email list": nguoi dung nhap ho ten + email, he thong luu
vao CSDL roi gui mail chao mung.

Bai tap yeu cau them **connection pool** va **JPA** so voi slide, nen tang
truy cap du lieu khong con la JDBC thuan nhu Week 6 nua.

## 1. Ba phan cua bai

### JavaMail (noi dung chuong 14)
Bon buoc gui mail o slide 17, nam trong `murach/util/`:

| File | Slide | Dung khi |
|---|---|---|
| `MailUtilLocal.java` | 40-43 | SMTP server tren may minh, `localhost:25`, khong dang nhap |
| `MailUtilGmail.java` | 52-55 | `smtp.gmail.com:465`, co dang nhap |
| `MailUtil.java` | (them) | Chon 1 trong 2 theo cau hinh o `web.xml` |

### Connection pool
Pool do **container** tao, ung dung chi tra cuu qua JNDI ten `jdbc/murach`:

- Tomcat: `src/main/webapp/META-INF/context.xml` (pool DBCP co san cua Tomcat)
- Jetty: `src/main/webapp/WEB-INF/jetty-env.xml` (pool HikariCP)
- `murach/data/ConnectionPool.java` chi lam mot viec: `lookup("java:comp/env/jdbc/murach")`

### JPA
- `murach/business/User.java` - entity, `@Entity @Table(name="User")`, khoa chinh la `Email`
- `murach/data/UserDB.java` - DAO viet bang `persist / merge / remove / find`, khong con cau SQL nao
- `src/main/resources/META-INF/persistence.xml` - cau hinh persistence unit `murachPU`
- `murach/data/JPAUtil.java` - **cho JPA gap connection pool**, xem duoi

## 2. Diem dang chu y nhat: JPA dung CHUNG pool cua container

Neu `persistence.xml` khai bao san `url` / `username` / `password` thi Hibernate
se **tu mo mot pool rieng**. Luc do ung dung co 2 pool, va thong tin ket noi bi
lap o 2 file.

O day `persistence.xml` **khong co mot dong thong tin ket noi nao**. Thay vao do
`JPAUtil` lay `DataSource` cua container roi truyen vao luc tao
`EntityManagerFactory`:

```java
DataSource ds = ConnectionPool.getInstance().getDataSource();
Map<String, Object> props = new HashMap<>();
props.put("jakarta.persistence.nonJtaDataSource", ds);   // ten chuan cua JPA
props.put("hibernate.connection.datasource", ds);        // ten rieng Hibernate
emf = Persistence.createEntityManagerFactory("murachPU", props);
```

Ket qua: chi con **mot** pool duy nhat, va user/password chi nam trong
`context.xml` / `jetty-env.xml`.

Cach kiem tra: dat `maximumPoolSize = 10` trong `jetty-env.xml`, chay ung dung
roi dem so ket noi phia MySQL - phai dung 10, khong hon:

```sql
SELECT COUNT(*) FROM information_schema.PROCESSLIST WHERE USER = 'murach_user';
```

## 3. Cach chay

### Buoc 1 - Tao CSDL
```bash
mysql -u root -p < db/create_murach_db.sql
```
File nay tao CSDL `murach`, bang `useraccount`, va tai khoan
`murach_user / sesame`. JPA **khong** tu tao bang
(`schema-generation = none`) nen buoc nay bat buoc.

Ten bang la `useraccount` chu khong phai `User` nhu Week6: `USER` la tu khoa
cua PostgreSQL, ma ban deploy len Render bat buoc dung PostgreSQL. Xem muc 4.

### Buoc 2 - Cau hinh gui mail trong `WEB-INF/web.xml`

| `mail.mode` | Y nghia |
|---|---|
| `smtp` | Gui qua SMTP server bat ky (host/port tu dat). **Mac dinh**, va la che do dung tren Render |
| `local` | Gui qua SMTP o `localhost:25` (slide 40-43). Mail **khong ra Internet** |
| `gmail` | Gui that qua Gmail (slide 52-55). Can `mail.username` + App password |
| `off` | Khong gui gi, chi luu CSDL |

Voi `local`, neu may khong co SMTP server thi `thanks.jsp` hien thong bao loi
nhu slide 51, nhung du lieu **van duoc luu vao CSDL**.

### Buoc 3 - Chay

Mac dinh du an dat `mail.mode = smtp` (de deploy len Render chay duoc ngay).
Muon gui bang Gmail o may thi dat `MAIL_MODE=gmail`. Mat khau **khong** nam
trong `web.xml` (file do bi commit len git) ma doc tu bien moi truong:

```powershell
# Windows PowerShell - gui bang Gmail o may
$env:MAIL_MODE = "gmail"
$env:MAIL_PASSWORD = "<App password 16 ky tu>"
mvn jetty:run
```
```bash
# Linux / macOS
MAIL_MODE=gmail MAIL_PASSWORD="<App password 16 ky tu>" mvn jetty:run
```

Mo http://localhost:8080/ch14email/

Cac bien moi truong duoc ho tro (deu uu tien hon `<context-param>`):
`MAIL_MODE`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`,
`MAIL_FROM`. Rieng cau hinh CSDL: `JDBC_URL` / `DATABASE_URL`, `DB_USERNAME`,
`DB_PASSWORD`, `DB_MAX_TOTAL`, `DB_MAX_IDLE`.

**Ve App password:** tu 2022 Gmail khong con cho dang nhap SMTP bang mat khau
thuong. Phai bat xac thuc 2 buoc cho tai khoan roi tao App password 16 ky tu.
Google hien no duoi dang 4 nhom `abcd efgh ijkl mnop`; code tu bo khoang trang
nen copy nguyen van cung duoc. App password cap **toan quyen** vao tai khoan
Google, nen dung dua no vao file nguon hay chia se cho ai.

Chay tren Tomcat bang Docker: xem muc 4.5.

### Thu gui mail ma khong can cai SMTP server
Dung MailHog, no bat mail lai va cho xem tren web thay vi gui that:
```bash
docker run -d -p 25:1025 -p 8025:8025 mailhog/mailhog
```
Dat `mail.mode = local`, ghi danh mot email, roi mo http://localhost:8025
de doc mail vua gui.

## 4. Deploy len Render

Render **khong co MySQL**, chi co PostgreSQL. Vi tang du lieu viet bang JPA
nen doi engine KHONG phai sua mot dong nao trong `UserDB.java` - chi doi JDBC
URL. Cung mot file `.war` chay duoc ca hai (da kiem tra thuc te).

### 4.1. Hai gioi han cua goi free can biet truoc

| Gioi han | He qua | Cach xu ly o day |
|---|---|---|
| Chan outbound port **25 / 465 / 587** | Gmail KHONG gui duoc sau khi deploy | Dung SMTP o **port 2525** (Brevo), Render khong chan |
| PostgreSQL free **het han sau 30 ngay** (+14 ngay gia han roi bi xoa) | Mat CSDL | Nop bai/cham diem trong thang dau, hoac nang len goi tra phi |
| Web service **ngu sau 15 phut** khong ai truy cap | Lan vao dau tien cho ~1 phut | Chap nhan duoc voi bai tap |

### 4.2. Chuan bi tai khoan gui mail (Brevo)

1. Dang ky https://www.brevo.com (free 300 mail/ngay, khong can the tin dung)
2. Xac minh dia chi gui o **Senders & IP** - dia chi nay se la `MAIL_FROM`
3. Vao **Settings > SMTP & API > SMTP**, lay hai thu:
   - **Login**: dang `xxxxx@smtp-brevo.com` -> dung lam `MAIL_USERNAME`
   - **SMTP key**: chuoi dai -> dung lam `MAIL_PASSWORD`

Luu y: phai dung **SMTP key**, khong phai API key, va `MAIL_USERNAME` la cai
login `@smtp-brevo.com` chu khong phai email dang nhap tai khoan Brevo.

### 4.3. Deploy

1. Push repo len GitHub (file `render.yaml` phai o **goc repo**, da co san)
2. Render Dashboard > **New** > **Blueprint** > chon repo nay
3. Render doc `render.yaml`, tao 1 web service + 1 PostgreSQL, roi **hoi ba
   bien**: `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM` - dien gia tri Brevo
   o buoc 4.2. Ba bien nay khong nam trong git.
4. Doi build xong, roi **tao bang** bang cach chay file
   `db/create_render_db.sql`:
   ```bash
   psql "<External Database URL lay o trang Render Postgres>" -f db/create_render_db.sql
   ```
   (JPA dat `schema-generation = none` nen khong tu tao bang.)
5. Mo URL cua service - dang ky thu mot email.

### 4.4. Nhung bien moi truong Render dung

Render tu dien `DATABASE_URL`; cac bien con lai do `render.yaml` khai bao.

| Bien | Gia tri | Ai dat |
|---|---|---|
| `DATABASE_URL` | `postgresql://user:pass@host/db` | Render tu noi tu database |
| `DB_MAX_TOTAL` / `DB_MAX_IDLE` | `10` / `5` | render.yaml |
| `MAIL_MODE` | `smtp` | render.yaml |
| `MAIL_HOST` / `MAIL_PORT` | `smtp-relay.brevo.com` / `2525` | render.yaml |
| `MAIL_USERNAME` / `MAIL_PASSWORD` / `MAIL_FROM` | bi mat | Render hoi luc tao Blueprint |

`docker-entrypoint.sh` doi `DATABASE_URL` (dang `postgresql://...`) thanh JDBC
URL, roi sinh ra `conf/Catalina/localhost/ROOT.xml` chua cau hinh connection
pool that.

**Vi sao sinh file chu khong truyen `-Dten=giatri`** (hai loi that gap phai
khi lam bai nay):
1. Tomcat in **toan bo command line argument** ra log luc khoi dong, nen mat
   khau CSDL se nam nguyen van trong log cua Render.
2. `catalina.sh` chay `eval` tren `$CATALINA_OPTS` ma khong dat trong nhay.
   JDBC URL cua MySQL co ky tu `&` -> shell cat lenh ngay tai do va Tomcat
   khong khoi dong duoc.

### 4.5. Chay thu bang Docker o may truoc khi deploy

```bash
docker network create ch14net

# 1. PostgreSQL giong tren Render
docker run -d --name pg --network ch14net \
    -e POSTGRES_USER=renderuser \
    -e POSTGRES_PASSWORD=renderpass \
    -e POSTGRES_DB=murach postgres:16

# 2. Tao bang
docker exec -i pg psql -U renderuser -d murach -f - < db/create_render_db.sql

# 3. Build va chay dung anh Docker ma Render se build
docker build -t ch14email .
docker run --rm --network ch14net -p 8099:10000 \
    -e PORT=10000 \
    -e DATABASE_URL="postgresql://renderuser:renderpass@pg:5432/murach" \
    -e MAIL_MODE=off \
    ch14email
```
Mo http://localhost:8099/

## 5. Nhung cho lam khac slide

| Slide | Trong bai nay | Ly do |
|---|---|---|
| `javax.mail.*` | `jakarta.mail.*` | Tu Jakarta EE 9, package `javax.*` doi ten thanh `jakarta.*` |
| Tai `javax.mail.jar` thu cong (slide 13) | Khai bao trong `pom.xml` | Maven tu tai; dung `angus-mail` (Jakarta Mail 2.1) de khong dung `jakarta.activation` voi Hibernate |
| Tai khoan Gmail viet cung trong code (slide 55) | Doc tu `<context-param>` | Khong dua mat khau vao file `.java` / len git |
| `transport.close()` de ngoai (slide 55) | Dat trong `finally` | `sendMessage` loi thi connection toi Gmail bi bo lung lung |
| `Session.getDefaultInstance(props)` | `Session.getInstance(props)` | `getDefaultInstance` tra ve Session dung chung ca JVM, properties bi dong bang tu lan goi dau. Chay `local` roi doi sang `gmail` thi gmail van gui vao `localhost:25`. Day dung la cai bay slide 25 mo ta ("phai restart Tomcat") |
| Mat khau Gmail trong `web.xml` | Doc tu bien moi truong `MAIL_PASSWORD` | `web.xml` bi commit len git |
| Servlet bao "thanks" du insert that bai | Kiem tra `insert() == 0` | DB loi thi nguoi dung phai biet, khong the bao thanh cong |
| JDBC + `PreparedStatement` (chuong 12) | JPA | Yeu cau cua bai tap |
| - | Them `JPAContextListener` | Khong dong `EntityManagerFactory` thi redeploy vai lan la `OutOfMemoryError: Metaspace` |
| Bang ten `User` | Bang ten `useraccount`, cot chu thuong | `USER` la tu khoa cua PostgreSQL. Doi ten bang ma khong phai sua `UserDB` - do la viec cua `@Table`/`@Column` |
| Chi co MySQL | Them driver PostgreSQL | Render khong co MySQL. Cung mot war chay ca hai engine |

## 6. Cau truc

```
ch14email/
├── db/create_murach_db.sql             # bang cho MySQL (o may)
├── db/create_render_db.sql             # bang cho PostgreSQL (Render)
├── docker-entrypoint.sh                # sinh cau hinh pool tu bien moi truong
├── Dockerfile
├── pom.xml
└── src/main/
    ├── java/murach/
    │   ├── business/User.java              # @Entity
    │   ├── data/ConnectionPool.java        # JNDI -> DataSource
    │   ├── data/JPAUtil.java               # JPA dung pool cua container
    │   ├── data/JPAContextListener.java    # dong EMF luc undeploy
    │   ├── data/UserDB.java                # DAO bang JPA
    │   ├── email/EmailListServlet.java     # Controller
    │   └── util/MailUtil*.java             # JavaMail (local / gmail / smtp)
    ├── resources/META-INF/persistence.xml
    └── webapp/
        ├── index.jsp, thanks.jsp, main.css
        ├── META-INF/context.xml            # pool cho Tomcat (deploy war thu cong)
        └── WEB-INF/web.xml, jetty-env.xml  # pool cho Jetty (chay o may)
```

Ngoai ra, o **goc repo** co `render.yaml` - blueprint khai bao web service +
PostgreSQL cho Render.
