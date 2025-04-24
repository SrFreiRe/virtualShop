import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.*;


public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        String cd = request.getParameter("cd");
        String quantityStr = request.getParameter("quantity");
        int quantity = 1;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception ignored) {}
        if (cd != null && !cd.isEmpty()) {
            boolean found = false;
            for (CartItem item : cart) {
                if (item.getCd().equals(cd)) {
                    // Increase quantity if already in cart
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cart.add(new CartItem(cd, quantity));
            }
        }
        session.setAttribute("cart", cart);
        response.sendRedirect("index.html?added=1");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        showCart(response, cart);
    }
    private void showCart(HttpServletResponse response, List<CartItem> cart) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Carrito | Virtual Shop</title><link rel='stylesheet' type='text/css' href='styles.css'></head><body>");
        out.println("<div class='container'>");
        out.println("<header><h1 style='font-weight:700;letter-spacing:-1px;'>🛒 Your Cart</h1><p class='subtitle' style='font-size:1.1em;'>Selected albums</p></header>");
        out.println("<table><tr><th>Album</th><th>Price</th><th>Quantity</th><th>Remove</th></tr>");
        double total = 0.0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            String cd = item.getCd();
            String[] parts = cd.split("\\|");
            String albumName = parts[0].trim();
            String priceStr = parts[parts.length - 1].trim();
            double price = extractPrice(cd);
            total += price * item.getQuantity();
            out.println("<tr>");
            out.println("<td>" + albumName + "</td>");
            out.println("<td>$" + String.format("%.2f", price) + "</td>");
            out.println("<td>" + item.getQuantity() + "</td>");
            out.println("<td>");
            out.println("<div class='remove-btns' style='display:flex;align-items:center;justify-content:center;gap:6px;'>");
            out.println("<form method='post' action='RemoveItemServlet' style='display:inline;'>");
            out.println("<input type='hidden' name='index' value='" + i + "'>");
            out.println("<input type='hidden' name='mode' value='one'>");
            out.println("<input type='submit' value='🗑️' class='remove-btn' title='Remove One'>");
            out.println("</form>");
            out.println("<form method='post' action='RemoveItemServlet' style='display:inline;'>");
            out.println("<input type='hidden' name='index' value='" + i + "'>");
            out.println("<input type='hidden' name='mode' value='all'>");
            out.println("<input type='submit' value='❌' class='remove-btn' title='Remove All'>");
            out.println("</form>");
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("<p style='text-align:center; font-size:1.2em;'><b>Total: $" + String.format("%.2f", total) + "</b></p>");
        if (cart.size() > 0) {
            out.println("<form action='CheckoutServlet' method='post' style='text-align:center;'>");
            out.println("<input type='submit' value='Checkout ✅'>");
            out.println("</form>");
        } else {
            out.println("<div style='text-align:center;color:#b388ff;font-size:1.15em;margin:28px 0 22px 0;font-weight:500;'>Your cart is empty.</div>");
        }
        out.println("<div style='text-align:center; margin-top:18px;'>");
        out.println("<a href='index.html' style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Continue shopping 🎶</a>");
        out.println("</div>");
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
