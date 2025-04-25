<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Double total = (Double) request.getAttribute("total");
    if (total == null) total = 0.0;
%>
<html>
<head>
    <title>Checkout Complete | Virtual Shop</title>
    <link rel='stylesheet' type='text/css' href='styles.css'>
</head>
<body>
<div class='container'>
<header><h1>Thank you for your purchase! 🎉</h1><p class='subtitle'>Your order was completed successfully.</p></header>
<p style='text-align:center; font-size:1.2em; margin-top:30px;'>Total paid: <b>$<%= String.format("%.2f", total) %></b></p>
<div style='text-align:center; margin-top:28px;'><a href='index.html' style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Back to shop 🛒</a></div>
<footer><hr><p style='text-align:center;color:#888;font-size:0.95em;'>&copy; 2025 Virtual Shop.</p></footer>
</div>
</body>
</html>
