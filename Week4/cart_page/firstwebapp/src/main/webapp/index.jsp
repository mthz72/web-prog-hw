<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title> Shopping Cart - Buy your CDs! </title>
    <link rel = "stylesheet" href="main.css" type="text/css">
</head>
<body>
    <h2>CD list</h2>
    <table>
        <tr>
            <th>Description</th>
            <th>Price</th>
            <th></th>
        </tr>
        <tr>
            <td>86 (the band) - True Life Songs and Pictures</td>
            <td>$14.95</td>
            <td>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productCode" value="1">
                    <input type="submit" value="Add to Cart">
                </form>
            </td>
            
        </tr>
        <tr>
            <td>Paddlefoot - The first CD</td>
            <td>$12.95</td>
            <td>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productCode" value="2">
                    <input type="submit" value="Add to Cart">
                </form>
            </td>
        </tr>
        <tr>
            <td>Paddlefoot - The second CD</td>
            <td>$14.95</td>
            <td>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productCode" value="3">
                    <input type="submit" value="Add to Cart">
                </form>
            </td>
        </tr>
        <tr>
            <td>Joe Rut - Genuine Wood Grained Finish</td>
            <td>$14.95</td>
            <td>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productCode" value="4">
                    <input type="submit" value="Add to Cart">
                </form>
            </td>
        </tr>
    </table>

</body>
</html>
