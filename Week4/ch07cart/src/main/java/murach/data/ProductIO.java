package murach.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import murach.business.Product;


public class ProductIO {

    private static final Map<String, Product> products = new LinkedHashMap<>();

    static {
        add(new Product("8601", "86 (the band) - True Life Songs and Pictures", 14.95));
        add(new Product("pf01", "Paddlefoot - The first CD", 12.95));
        add(new Product("pf02", "Paddlefoot - The second CD", 14.95));
        add(new Product("jr01", "Joe Rut - Genuine Wood Grained Finish", 14.95));
    }

    private static void add(Product product) {
        products.put(product.getCode(), product);
    }

    public static Product getProduct(String code) {
        return products.get(code);
    }

    public static Collection<Product> getProducts() {
        return products.values();
    }
}
