import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.util.*;

public class RemoveItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        String indexStr = request.getParameter("index");
        String mode = request.getParameter("mode");
        if (cart != null && indexStr != null) {
            try {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index < cart.size()) {
                    CartItem item = cart.get(index);
                    if ("all".equals(mode)) {
                        cart.remove(index);
                    } else {
                        if (item.getQuantity() > 1) {
                            item.setQuantity(item.getQuantity() - 1);
                        } else {
                            cart.remove(index);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
        session.setAttribute("cart", cart);
        response.sendRedirect("CartServlet");
    }
}
