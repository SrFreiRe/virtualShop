
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.util.*;

public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double total = 0.0;
        if (cart != null) {
            for (CartItem item : cart) {
                double price = extractPrice(item.getCd());
                total += price * item.getQuantity();
            }
        }
        // Clear cart after checkout
        session.setAttribute("cart", new ArrayList<CartItem>());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Checkout Complete | Virtual Shop</title><link rel='stylesheet' type='text/css' href='styles.css'></head><body>");
        out.println("<div class='container'>");
        out.println("<header><h1>Thank you for your purchase! 🎉</h1><p class='subtitle'>Your order was completed successfully.</p></header>");
        out.println("<p style='text-align:center; font-size:1.2em; margin-top:30px;'>Total paid: <b>$" + String.format("%.2f", total) + "</b></p>");
        out.println("<div style='text-align:center; margin-top:28px;'><a href='index.html' style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Back to shop 🛒</a></div>");
        out.println("<footer><hr><p style='text-align:center;color:#888;font-size:0.95em;'>&copy; 2025 Virtual Shop.</p></footer>");
        out.println("</div>");
        out.println("</body></html>");
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
