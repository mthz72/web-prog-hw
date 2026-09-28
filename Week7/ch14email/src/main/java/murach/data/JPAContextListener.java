package murach.data;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Dong EntityManagerFactory khi ung dung bi go ra khoi server.
 *
 * Khong co lop nay thi moi lan redeploy (rat hay xay ra luc dang lam bai),
 * EMF cu van giu connection va giu classloader cu -> server ro ri bo nho,
 * chay vai lan la bao OutOfMemoryError: Metaspace.
 *
 * (Listener la noi dung chuong 18; o day chi dung dung mot ham don gian.)
 */
@WebListener
public class JPAContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Khong lam gi: EMF duoc tao lan dau khi co request thuc su,
        // de server van khoi dong duoc ngay ca khi MySQL chua bat.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
