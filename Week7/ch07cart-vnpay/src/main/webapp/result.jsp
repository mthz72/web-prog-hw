<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Kết quả thanh toán</title>
    <link rel="stylesheet" href="main.css" type="text/css"/>
</head>
<body>

<c:choose>
    <c:when test="${!validSignature}">
        <h1>Chữ ký không hợp lệ</h1>
    </c:when>
    <c:when test="${success}">
        <h1>Thanh toán thành công</h1>
    </c:when>
    <c:otherwise>
        <h1>Thanh toán không thành công</h1>
    </c:otherwise>
</c:choose>

<table>
    <tr><td>Mã đơn hàng</td><td><c:out value="${param.vnp_TxnRef}"/></td></tr>
    <tr><td>Số tiền</td><td>${amount} VND</td></tr>
    <tr><td>Nội dung</td><td><c:out value="${param.vnp_OrderInfo}"/></td></tr>
    <tr><td>Mã phản hồi</td><td><c:out value="${param.vnp_ResponseCode}"/></td></tr>
    <tr><td>Mã GD VNPAY</td><td><c:out value="${param.vnp_TransactionNo}"/></td></tr>
    <tr><td>Ngân hàng</td><td><c:out value="${param.vnp_BankCode}"/></td></tr>
    <tr><td>Thời gian</td><td><c:out value="${param.vnp_PayDate}"/></td></tr>
</table>

<form action="cart" method="post">
    <input type="hidden" name="action" value="shop">
    <input type="submit" value="Continue Shopping">
</form>

</body>
</html>
