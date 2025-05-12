<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
            <%@ page isELIgnored="false" %>
                <html>

                <head>
                    <title>Checkout Complete | Virtual Shop</title>
                    <link rel='stylesheet' type='text/css' href='styles.css'>
                </head>

                <body>
                    <div class='container'>
                        <div>
                            <c:choose>
                                <c:when test="${empty orderConfirmed}">
                                    <header>
                                        <h1>Checkout</h1>
                                        <p class='subtitle'>Enter your details to confirm your purchase.</p>
                                    </header>
                                    <form method="post" action="CartServlet" id="checkoutForm">
                                        <input type="hidden" name="action" value="confirmOrder" />
                                        <input type="hidden" name="submitType" value="login" id="submitTypeInput" />
                                        <div class="form-group">
                                            <label for="email"><b>Email:</b></label>
                                            <input type="email" id="email" name="email" required />
                                        </div>
                                        <div class="form-group">
                                            <label for="password"><b>Password:</b></label>
                                            <input type="password" id="password" name="password" required />
                                        </div>
                                        <div id="registerFields" style="display:none;">
                                            <div class="form-group">
                                                <label for="card_type"><b>Card Type:</b></label>
                                                <input type="text" id="card_type" name="card_type" />
                                            </div>
                                            <div class="form-group">
                                                <label for="card_number"><b>Card Number:</b></label>
                                                <input type="text" id="card_number" name="card_number" />
                                            </div>
                                            <div style="text-align:center; margin-top:18px;">
                                                <input type="button" id="goBackToLogin" value="Go Back To Login"
                                                    class="small-btn" onclick="goBackToLoginForm()" />
                                                <input type="submit" id="register" value="Register & Confirm Purchase"
                                                    class="small-btn" />
                                            </div>
                                        </div>
                                        <div id="registerPrompt" style="text-align:center; margin-top:18px;">
                                            <input type="button" onclick="expandRegisterFields()" id="expandBtn"
                                                name="submitType" value="You don't have an account?"
                                                class="small-btn" />
                                            <input type="submit" id="login" value="Login & Confirm Purchase"
                                                class="small-btn" />
                                        </div>
                                    </form>
                                    <script>
                                        function expandRegisterFields() {
                                            document.getElementById('registerFields').style.display = 'block';
                                            document.getElementById('registerPrompt').style.display = 'none';
                                            document.getElementById('card_type').required = true;
                                            document.getElementById('card_number').required = true;
                                            document.getElementById('submitTypeInput').value = 'register';
                                            //document.querySelector('.error').style.display = 'none';
                                        }

                                        function goBackToLoginForm() {
                                            document.getElementById('registerFields').style.display = 'none';
                                            document.getElementById('registerPrompt').style.display = 'block';
                                            document.getElementById('card_type').required = false;
                                            document.getElementById('card_number').required = false;
                                            document.getElementById('submitTypeInput').value = 'login';
                                            document.querySelector('.error').style.display = 'none';
                                        }
                                    </script>
                                    <c:if test="${not empty error}">
                                        <c:if test="${error_type == 'non_existing_user'}">
                                            <script>expandRegisterFields();</script>
                                        </c:if>
                                        <div class="error" style="display: block;">${error}</div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <header>
                                        <h1>Thank you for your purchase! 🎉</h1>
                                        <p class='subtitle'>Your order was completed successfully.</p>
                                    </header>
                                    <p style='text-align:center; font-size:1.2em; margin-top:30px;'>Total paid:
                                        <b>${order.totalAmount} $</b>
                                    </p>
                                    <p style='text-align:center;'>Order date: <b>
                                            <c:choose>
                                                <c:when test="${not empty order.orderDate}">
                                                    ${fn:substringBefore(order.orderDate, ".")}
                                                </c:when>
                                                <c:otherwise>
                                                    -
                                                </c:otherwise>
                                            </c:choose>
                                        </b></p>
                                    <div style='text-align:center; margin-top:28px;'><a href='index.html'
                                            style='color:#4f8cff;text-decoration:none;font-weight:bold;'>Back to shop
                                            🛒</a></div>
                                </c:otherwise>
                            </c:choose>
                            <footer>
                                <hr>
                                <p style='text-align:center;color:#888;font-size:0.95em;'>&copy; 2025 Virtual Shop.<a
                                        href="index.html">🏠</a></p>
                            </footer>
                        </div>
                </body>

                </html>