package murach.business;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Mot dong trong gio hang = mot Product + so luong.
 */
public class LineItem implements Serializable {

    private Product product;
    private int quantity;

    public LineItem() {
        this.product = null;
        this.quantity = 0;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    // EL: ${item.totalCurrencyFormat}
    public String getTotalCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(getTotal());
    }
}
