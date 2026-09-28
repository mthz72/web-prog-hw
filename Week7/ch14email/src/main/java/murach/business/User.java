package murach.business;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * JavaBean tuong ung voi 1 dong trong bang User(Email, FirstName, LastName).
 *
 * Khac Week6: lop nay gio la ENTITY cua JPA.
 *   @Entity  -> JPA quan ly lop nay, moi object la 1 dong trong bang
 *   @Table   -> ten bang duoi CSDL
 *   @Id      -> khoa chinh; o day la email (bang khong co cot id tu tang)
 *   @Column  -> ten cot duoi CSDL
 *
 * VI SAO BANG TEN "useraccount" CHU KHONG PHAI "User" nhu Week6:
 * "USER" la TU KHOA cua PostgreSQL, viet CREATE TABLE User khong dat dau
 * nhay kep se loi cu phap. Ma ban deploy tren Render thi bat buoc dung
 * PostgreSQL (Render khong co MySQL). Dat ten trung tinh thi chay duoc ca
 * MySQL o may va PostgreSQL tren Render, khong phai dat dau nhay o dau ca.
 *
 * Ten COT cung de chu thuong het (email, firstname, lastname): PostgreSQL tu
 * ha ten khong dat nhay kep ve chu thuong, nen viet "Email" nhu Week6 se
 * thanh "email" - de chu thuong ngay tu dau cho khoi nhap nhang.
 *
 * Doi ten bang/cot ma KHONG phai sua UserDB mot dong nao: do la viec cua
 * @Table va @Column. Ten dung trong JPQL la ten ENTITY ("User"), khong phai
 * ten bang, nen @NamedQuery ben duoi cung giu nguyen.
 *
 * Nho @NamedQuery nay ma UserDB khong phai viet chuoi JPQL rai rac trong code:
 * cau lenh duoc kiem tra ngay luc Hibernate khoi dong chu khong doi den luc chay.
 *
 * Luu y: JPA BAT BUOC entity phai co constructor khong tham so (public hoac
 * protected). Vi da khai bao constructor 3 tham so nen phai giu lai
 * constructor rong, neu khong Hibernate se bao loi luc khoi dong.
 */
@Entity
@Table(name = "useraccount")
@NamedQuery(
        name = "User.countByEmail",
        query = "SELECT COUNT(u) FROM User u WHERE u.email = :email")
public class User implements Serializable {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    public User() {
        firstName = "";
        lastName = "";
        email = "";
    }

    // Thu tu tham so giu giong slide 47: firstName, lastName, email
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
