import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.*;
import beans.Cart;
import beans.CDItem;

public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        double total = 0.0;
        if (cart != null) {
            for (CDItem cd : cart.getItems()) {
                int quantity = cd.getQuantity();
                double price = cd.getPrice();
                total += price * quantity;
            }
        }
        // Clear cart after checkout
        session.setAttribute("cart", new Cart());
        request.setAttribute("total", total);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);
    }

}
