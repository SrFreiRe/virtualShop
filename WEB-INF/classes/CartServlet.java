import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.*;
import beans.Cart;
import beans.CDItem;

public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
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
                // Expect cd in format: name|...|price
                String[] parts = cd.split("\\|");
                String name = parts[0].trim();
                double price = 0.0;
                try { price = Double.parseDouble(parts[parts.length - 1].replace("$", "").trim()); } catch (Exception e) {}
                CDItem item = new CDItem(name, price, quantity);
                cart.addItem(item);
            }
            session.setAttribute("cart", cart);
            response.sendRedirect("index.html?added=1");
        } else if (action.equals("remove")) {
            // Remove or decrement from cart
            String indexStr = request.getParameter("index");
            String mode = request.getParameter("mode");
            if (indexStr != null) {
                try {
                    int index = Integer.parseInt(indexStr);
                    if ("all".equals(mode)) {
                        cart.removeItem(index);
                    } else {
                        cart.decrementItem(index);
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
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();
        request.setAttribute("cart", cart);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
        dispatcher.forward(request, response);
    }

}
