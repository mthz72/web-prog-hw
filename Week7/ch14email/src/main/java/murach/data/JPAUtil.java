package murach.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Cau noi giua CONNECTION POOL va JPA - phan trong tam cua bai tap nay.
 *
 * Mac dinh, neu persistence.xml khai bao san url/user/password thi Hibernate
 * se TU MO mot connection pool rieng cua no. Nhu vay ung dung co 2 pool
 * chay song song, va cau hinh CSDL bi lap o 2 noi (context.xml + persistence.xml).
 *
 * Cach lam o day: khong ghi thong tin ket noi nao trong persistence.xml, ma
 * lay DataSource cua container (da la mot pool: DBCP tren Tomcat, HikariCP
 * tren Jetty) roi truyen vao luc tao EntityManagerFactory. Ket qua:
 *   - chi con MOT pool duy nhat, do container quan ly
 *   - user/password chi nam trong context.xml / jetty-env.xml
 *
 * "jakarta.persistence.nonJtaDataSource" la ten thuoc tinh CHUAN cua JPA nen
 * doi sang EclipseLink van chay. Dat kem "hibernate.connection.datasource"
 * (ten rieng cua Hibernate, cung tro toi dung object DataSource do) cho chac.
 *
 * EntityManagerFactory rat nang (quet entity, dung metamodel, mo pool) nen
 * chi tao MOT lan cho ca ung dung. Con EntityManager thi nhe va KHONG
 * thread-safe, nen moi request tao moi roi dong ngay - giong nhu cach ta
 * muon mot Connection roi tra ve pool.
 */
public class JPAUtil {

    // Phai trung <persistence-unit name="..."> trong META-INF/persistence.xml
    private static final String PERSISTENCE_UNIT = "murachPU";

    private static EntityManagerFactory emf = null;

    private JPAUtil() {
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                DataSource ds = ConnectionPool.getInstance().getDataSource();

                Map<String, Object> props = new HashMap<>();
                props.put("jakarta.persistence.nonJtaDataSource", ds);
                props.put("hibernate.connection.datasource", ds);

                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, props);
            } catch (SQLException e) {
                // Khong co DataSource thi khong the tao EMF -> bao loi ro rang
                // thay vi de Hibernate nem mot loi kho doc o tan ben trong.
                throw new IllegalStateException(
                        "Khong tao duoc EntityManagerFactory vi thieu connection pool.", e);
            }
        }
        return emf;
    }

    /** Moi request / moi thao tac CSDL goi mot lan, va PHAI close() sau khi dung. */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /** Goi luc ung dung dung (xem JPAContextListener). */
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }
}
