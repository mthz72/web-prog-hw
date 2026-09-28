package murach.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import murach.business.Cart;
import murach.business.LineItem;
import murach.business.Product;
import murach.data.ProductIO;


public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";  
        }

        HttpSession session = request.getSession();
        String url = "/cart.jsp";

     
        final Object lock = session.getId().intern();

        synchronized (lock) {
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {          
                cart = new Cart();
            }

            switch (action) {
                case "shop":
                    url = "/index.jsp";
                    break;
                case "add":
                    addToCart(request, cart);
                    break;
                case "update":
                    updateCart(request, cart);
                    break;
                case "remove":
                    removeFromCart(request, cart);
                    break;
                default:
                    break;               
            }

            session.setAttribute("cart", cart);
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

  
    private void addToCart(HttpServletRequest request, Cart cart) {
        Product product = getProductFromRequest(request);
        if (product == null) {
            return;
        }
        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(1);
        cart.addItem(lineItem);
    }

   
    private void updateCart(HttpServletRequest request, Cart cart) {
        Product product = getProductFromRequest(request);
        if (product == null) {
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            return;  
        }
        if (quantity < 0) {
            return;
        }

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(quantity);

        if (quantity > 0) {
            cart.addItem(lineItem);
        } else {
            cart.removeItem(lineItem);
        }
    }

    
    private void removeFromCart(HttpServletRequest request, Cart cart) {
        Product product = getProductFromRequest(request);
        if (product == null) {
            return;
        }
        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        cart.removeItem(lineItem);
    }

    private Product getProductFromRequest(HttpServletRequest request) {
        String productCode = request.getParameter("productCode");
        if (productCode == null) {
            return null;
        }
        return ProductIO.getProduct(productCode);
    }
}
