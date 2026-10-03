<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Checkout</title>
    <link rel="stylesheet" href="main.css" type="text/css"/>
</head>
<body>

<h1>Checkout</h1>
<table>
    <tr>
        <th>Quantity</th>
        <th>Description</th>
        <th>Amount</th>
    </tr>
    <c:forEach var="item" items="${cart.items}">
    <tr>
        <td>${item.quantity}</td>
        <td>${item.product.description}</td>
        <td>${item.totalCurrencyFormat}</td>
    </tr>
    </c:forEach>
    <tr>
        <td colspan="2"><b>Total</b></td>
        <td><b>${cart.totalCurrencyFormat}</b></td>
    </tr>
</table>

<p>Số tiền thanh toán: <b>${amountVnd} VND</b> (1 USD = 25.000 VND)</p>

<form action="payment" method="post">
    <p>Chọn phương thức thanh toán:</p>
    <label><input type="radio" name="bankCode" value="" checked> Cổng thanh toán VNPAY (QR, thẻ, ví...)</label><br>
    <label><input type="radio" name="bankCode" value="VNBANK"> Thẻ ATM / Tài khoản ngân hàng</label><br>
    <label><input type="radio" name="bankCode" value="INTCARD"> Thẻ quốc tế</label><br><br>
    <input type="submit" value="Thanh toán">
</form>

<form action="cart" method="post">
    <input type="hidden" name="action" value="cart">
    <input type="submit" value="Quay lại giỏ hàng">
</form>

</body>
</html>
