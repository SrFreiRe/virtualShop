
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.util.*;

public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        double total = 0.0;
        if (cart != null) {
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                String cd = entry.getKey();
                int quantity = entry.getValue();
                double price = extractPrice(cd);
                total += price * quantity;
            }
        }
        // Clear cart after checkout
        session.setAttribute("cart", new LinkedHashMap<String, Integer>());
        request.setAttribute("total", total);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);
    }
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
