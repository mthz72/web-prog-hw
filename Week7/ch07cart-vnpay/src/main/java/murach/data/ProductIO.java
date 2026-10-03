package murach.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import murach.business.Product;

public class ProductIO {

    private static ArrayList<Product> products = null;
    private static String filePath = null;

    public static void init(String path) {
        filePath = path;
    }

    public static ArrayList<Product> selectProducts() {
        products = new ArrayList<Product>();
        File file = new File(filePath);
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = in.readLine();
            while (line != null) {
                StringTokenizer t = new StringTokenizer(line, "|");
                if (t.countTokens() >= 3) {
                    Product p = new Product();
                    p.setCode(t.nextToken());
                    p.setDescription(t.nextToken());
                    p.setPrice(Double.parseDouble(t.nextToken()));
                    products.add(p);
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return products;
    }

    public static Product selectProduct(String productCode) {
        products = selectProducts();
        for (Product p : products) {
            if (productCode != null && productCode.equalsIgnoreCase(p.getCode())) {
                return p;
            }
        }
        return null;
    }
}
