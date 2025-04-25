import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.*;

public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
        }
        String action = request.getParameter("action");
        if (action == null || action.equals("add")) {
            // Add to cart
            String cd = request.getParameter("cd");
            String quantityStr = request.getParameter("quantity");
            int quantity = 1;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (Exception ignored) {}
            if (cd != null && !cd.isEmpty()) {
                if (cart.containsKey(cd)) {
                    cart.put(cd, cart.get(cd) + quantity);
                } else {
                    cart.put(cd, quantity);
                }
            }
            session.setAttribute("cart", cart);
            response.sendRedirect("index.html?added=1");
        } else if (action.equals("remove")) {
            // Remove or decrement from cart
            String indexStr = request.getParameter("index");
            String mode = request.getParameter("mode");
            if (cart != null && indexStr != null) {
                try {
                    int index = Integer.parseInt(indexStr);
                    if (index >= 0 && index < cart.size()) {
                        String cd = new ArrayList<>(cart.keySet()).get(index);
                        if ("all".equals(mode)) {
                            cart.remove(cd);
                        } else {
                            int quantity = cart.get(cd);
                            if (quantity > 1) {
                                cart.put(cd, quantity - 1);
                            } else {
                                cart.remove(cd);
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }
            session.setAttribute("cart", cart);
            response.sendRedirect("CartServlet");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        if (cart == null) cart = new LinkedHashMap<>();
        request.setAttribute("cart", cart);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
        dispatcher.forward(request, response);
    }
    // Removed: showCart() method. All HTML is now in cart.jsp.
    private double extractPrice(String cd) {
        try {
            String[] parts = cd.split("\\|");
            String priceStr = parts[parts.length - 1].replace("$", "").trim();
            return Double.parseDouble(priceStr);
        } catch (Exception e) {
            return 0.0;
        }
    }

}
