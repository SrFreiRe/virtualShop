<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, Integer> cart = (Map<String, Integer>) request.getAttribute("cart");
    if (cart == null) cart = new LinkedHashMap<>();
    double total = 0.0;
%>
<html>
<head>
    <title>Carrito | Virtual Shop</title>
    <link rel='stylesheet' type='text/css' href='styles.css'>
</head>
<body>
<div class='container'>
<header><h1 style='font-weight:700;letter-spacing:-1px;'>🛒 Your Cart</h1><p class='subtitle' style='font-size:1.1em;'>Selected albums</p></header>
<table><tr><th>Album</th><th>Price</th><th>Quantity</th><th>Remove</th></tr>
<%
    int i = 0;
    for (Map.Entry<String, Integer> entry : cart.entrySet()) {
        String cd = entry.getKey();
        int quantity = entry.getValue();
        String[] parts = cd.split("\\|");
        String albumName = parts[0].trim();
        String priceStr = parts[parts.length - 1].replace("$", "").trim();
        double price = 0.0;
        try { price = Double.parseDouble(priceStr); } catch (Exception e) {}
        total += price * quantity;
%>
<tr>
    <td><%= albumName %></td>
    <td>$<%= String.format("%.2f", price) %></td>
    <td><%= quantity %></td>
    <td>
        <div class='remove-btns' style='display:flex;align-items:center;justify-content:center;gap:6px;'>
            <form method='post' action='CartServlet' style='display:inline;'>
                <input type='hidden' name='action' value='remove'>
                <input type='hidden' name='index' value='<%= i %>'>
                <input type='hidden' name='mode' value='one'>
                <input type='submit' value='🗑️' class='remove-btn' title='Remove One'>
            </form>
            <form method='post' action='CartServlet' style='display:inline;'>
                <input type='hidden' name='action' value='remove'>
                <input type='hidden' name='index' value='<%= i %>'>
                <input type='hidden' name='mode' value='all'>
                <input type='submit' value='❌' class='remove-btn' title='Remove All'>
            </form>
        </div>
    </td>
</tr>
<% i++; } %>
</table>
<p style='text-align:center; font-size:1.2em;'><b>Total: $<%= String.format("%.2f", total) %></b></p>
<% if (cart.size() > 0) { %>
<form action='CheckoutServlet' method='post' style='text-align:center;'>
    <input type='submit' value='Checkout ✅'>
</form>
<% } else { %>
<div style='text-align:center;color:#b388ff;font-size:1.15em;margin:28px 0 22px 0;font-weight:500;'>Your cart is empty.</div>
<% } %>
<div style='text-align:center; margin-top:18px;'>
    <a href='index.html' style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Continue shopping 🎶</a>
</div>
<footer><hr><p style='text-align:center;color:#888;font-size:0.95em;'>&copy; 2025 Virtual Shop.</p></footer>
</div>
</body>
</html>
