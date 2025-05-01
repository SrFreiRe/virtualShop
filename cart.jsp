<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Carrito | Virtual Shop</title>
    <link rel='stylesheet' type='text/css' href='styles.css'>
</head>
<body>
<div class='container'>
<header><h1 style='font-weight:700;letter-spacing:-1px;'>🛒 Your Cart</h1><p class='subtitle' style='font-size:1.1em;'>Selected albums</p></header>
<table><tr><th>Album</th><th>Price</th><th>Quantity</th><th>Subtotal</th><th>Remove</th></tr>
<c:forEach var="item" items="${cart.items}" varStatus="status">
<tr>
    <td>${item.name}</td>
    <td><fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" />$</td>
    <td>${item.quantity}</td>
    <td><fmt:formatNumber value="${item.subtotal}" type="number" minFractionDigits="2" />$</td>
    <td>
        <div class='remove-btns' style='display:flex;align-items:center;justify-content:center;gap:6px;'>
            <form method='post' action='CartServlet' style='display:inline;'>
                <input type='hidden' name='action' value='remove'>
                <input type='hidden' name='index' value='${status.index}'>
                <input type='hidden' name='mode' value='one'>
                <input type='submit' value='🗑️' class='remove-btn' title='Remove One'>
            </form>
            <form method='post' action='CartServlet' style='display:inline;'>
                <input type='hidden' name='action' value='remove'>
                <input type='hidden' name='index' value='${status.index}'>
                <input type='hidden' name='mode' value='all'>
                <input type='submit' value='❌' class='remove-btn' title='Remove All'>
            </form>
        </div>
    </td>
</tr>
</c:forEach>
</table>
<p style='text-align:center; font-size:1.2em;'><b>Total: <fmt:formatNumber value="${cart.total}" type="number" minFractionDigits="2" />$</b></p>
<c:choose>
    <c:when test="${cart.size > 0}">
        <form action='CheckoutServlet' method='post' style='text-align:center;'>
            <input type='submit' value='Checkout ✅'>
        </form>
    </c:when>
    <c:otherwise>
        <div style='text-align:center;color:#b388ff;font-size:1.15em;margin:28px 0 22px 0;font-weight:500;'>Your cart is empty.</div>
    </c:otherwise>
</c:choose>
<div style='text-align:center; margin-top:18px;'>
    <a href='index.html' style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Continue shopping 🎶</a>
</div>
<footer><hr><p style='text-align:center;color:#888;font-size:0.95em;'>&copy; 2025 Virtual Shop.</p></footer>
</div>
</body>
</html>
