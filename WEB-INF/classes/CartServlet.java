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
        else if ("checkout".equals(action)) {
            // Show checkout form
            Cart cartInSession = (Cart) session.getAttribute("cart");
            if (cartInSession == null) {
                cartInSession = new Cart();
            }
            request.setAttribute("cart", cartInSession);
            request.setAttribute("total", cartInSession.getTotal());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
            dispatcher.forward(request, response);
        } else if ("confirmOrder".equals(action)) {
            // F6, F7, F8: Authenticate/register user, store order, show confirmation
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String cardType = request.getParameter("card_type");
            String cardNumber = request.getParameter("card_number");
            String submitType = request.getParameter("submitType");

            System.out.println(submitType);
            Cart cartInSession = (Cart) session.getAttribute("cart");
            if (cartInSession == null || cartInSession.getItems().isEmpty()) {
                request.setAttribute("error", "Your cart is empty.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                dispatcher.forward(request, response);
                return;
            }
            double totalAmount = cartInSession.getTotal();
            java.sql.Connection conn = null;
            java.sql.PreparedStatement stmt = null;
            java.sql.ResultSet rs = null;
            int userId = -1;
            try {
                javax.naming.Context initCtx = new javax.naming.InitialContext();
                javax.sql.DataSource ds = (javax.sql.DataSource) initCtx.lookup("java:comp/env/jdbc/VirtualShopDB");
                conn = ds.getConnection();
                // Check if user exists
                stmt = conn.prepareStatement("SELECT id, password FROM users WHERE email = ?");
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                boolean userExists = rs.next();
                if ("login".equals(submitType)) {
                    if (userExists) {
                        // User exists, check password
                        String storedPassword = rs.getString("password");
                        if (!storedPassword.equals(password)) {
                            request.setAttribute("error", "Incorrect password.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                        userId = rs.getInt("id");
                    } else {
                        request.setAttribute("error", "User does not exist. Please register.");
                        request.setAttribute("error_type", "non_existing_user");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                } else if ("register".equals(submitType)) {
                    if (userExists) {
                        request.setAttribute("error", "User already exists. Please log in.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                        dispatcher.forward(request, response);
                        return;
                    } else {
                        // Register new user
                        stmt.close();
                        stmt = conn.prepareStatement("INSERT INTO users (name, email, password, card_type, card_number) VALUES (?, ?, ?, ?, ?) RETURNING id");
                        stmt.setString(1, email); // Using email as name for simplicity
                        stmt.setString(2, email);
                        stmt.setString(3, password);
                        stmt.setString(4, cardType);
                        stmt.setString(5, cardNumber);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            userId = rs.getInt("id");
                        } else {
                            request.setAttribute("error", "Failed to register user.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                } else {
                    request.setAttribute("error", "Unknown action.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                // Store order
                stmt.close();
                stmt = conn.prepareStatement("INSERT INTO orders (user_id, total_amount) VALUES (?, ?) RETURNING id, order_date");
                stmt.setInt(1, userId);
                stmt.setDouble(2, totalAmount);
                rs = stmt.executeQuery();
                int orderId = -1;
                java.sql.Timestamp orderDate = null;
                if (rs.next()) {
                    orderId = rs.getInt("id");
                    orderDate = rs.getTimestamp("order_date");
                }
                // Clear cart
                cartInSession.clear();
                session.setAttribute("cart", cartInSession);
                // Set confirmation attributes
                request.setAttribute("orderConfirmed", true);
                java.util.HashMap<String, Object> orderInfo = new java.util.HashMap<>();
                orderInfo.put("orderId", orderId);
                orderInfo.put("totalAmount", totalAmount);
                orderInfo.put("orderDate", orderDate);
                request.setAttribute("order", orderInfo);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                dispatcher.forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("error", "An error occurred: " + ex.getMessage());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
                dispatcher.forward(request, response);
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception ignored) {}
                try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
                try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            }
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
