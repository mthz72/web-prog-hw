package murach.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import murach.business.User;

/**
 * Lop truy cap du lieu (DAO) cho bang User.
 *
 * Khac Week6: khong con cau SQL nao, khong con PreparedStatement, khong con
 * ResultSet. Thay vao do la cac ham cua JPA:
 *   persist() -> INSERT
 *   merge()   -> UPDATE
 *   remove()  -> DELETE
 *   find()    -> SELECT theo khoa chinh
 * Hibernate tu sinh SQL, va tu dat tham so nen VAN chong duoc SQL injection
 * giong nhu PreparedStatement o Week6.
 *
 * Ba diem phai nho khi viet DAO kieu nay:
 *  1. Moi ham tao EntityManager rieng va dong no trong finally. Quen close()
 *     la giu connection cua pool -> chay mot luc la het connection.
 *  2. Moi thao tac GHI phai nam trong transaction (begin/commit), loi thi
 *     rollback. Thao tac DOC thi khong can.
 *  3. Giu nguyen chu ky ham nhu Week6 (tra ve int / boolean) de
 *     EmailListServlet khong phai sua gi khi chuyen tu JDBC sang JPA.
 */
public class UserDB {

    public static int insert(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(user);
            trans.commit();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            if (trans.isActive()) {
                trans.rollback();
            }
            return 0;
        } finally {
            em.close();
        }
    }

    public static int update(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            // merge() chu khong phai persist(): user la object "detached"
            // (tao bang new o servlet), persist() se coi no la dong moi.
            em.merge(user);
            trans.commit();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            if (trans.isActive()) {
                trans.rollback();
            }
            return 0;
        } finally {
            em.close();
        }
    }

    public static int delete(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            // Chi xoa duoc entity dang "managed", nen phai merge() ve truoc.
            em.remove(em.merge(user));
            trans.commit();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            if (trans.isActive()) {
                trans.rollback();
            }
            return 0;
        } finally {
            em.close();
        }
    }

    public static boolean emailExists(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Dung named query khai bao o User: dem thay vi tai ca dong ve.
            TypedQuery<Long> q = em.createNamedQuery("User.countByEmail", Long.class);
            q.setParameter("email", email);
            return q.getSingleResult() > 0;
        } catch (Exception e) {
            System.out.println(e);
            // Tra false khi loi se cho insert chay tiep va that bai ro rang,
            // hon la chan nguoi dung bang thong bao "email da ton tai" sai.
            return false;
        } finally {
            em.close();
        }
    }

    public static User selectUser(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // find() tra ve null neu khong co dong nao - khong nem exception.
            return em.find(User.class, email);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            em.close();
        }
    }
}
