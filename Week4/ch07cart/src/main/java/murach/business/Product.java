package murach.business;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * JavaBean mo ta mot san pham (mot dia CD).
 * Phai co constructor rong + cap get/set thi EL moi doc duoc: ${product.description}
 */
public class Product implements Serializable {

    private String code;
    private String description;
    private double price;

    public Product() {
        this.code = "";
        this.description = "";
        this.price = 0;
    }

    public Product(String code, String description, double price) {
        this.code = code;
        this.description = description;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // EL goi duoc bang ${product.priceCurrencyFormat}
    // Locale.US de ra "$14.95" giong slide, khong ra tien Viet theo may cua ban.
    public String getPriceCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }
}
