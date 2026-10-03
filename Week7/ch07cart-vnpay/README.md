# Week 7 - Cart (chuong 7) + thanh toan VNPay

Ung dung CD list cua chuong 7, them buoc thanh toan qua VNPay sandbox
(theo video https://www.youtube.com/watch?v=kAyL_gA0zc8).

Luong: `index.jsp` -> `cart` -> `checkout.jsp` -> `payment` (chuyen sang VNPay)
-> VNPay tra ve `vnpay-return` -> `result.jsp`.

- `PaymentServlet`: tao URL thanh toan, ky bang HMAC-SHA512
- `VNPayReturnServlet`: kiem tra chu ky va hien ket qua
- `VNPayConfig`: cau hinh + ham ky (theo code demo cua VNPay)

## Chay

Lay `vnp_TmnCode`, `vnp_HashSecret` khi dang ky tai https://sandbox.vnpayment.vn/devreg/

```powershell
$env:VNP_TMN_CODE = "..."
$env:VNP_HASH_SECRET = "..."
mvn jetty:run
```
Mo http://localhost:8080/ch07cart/

The test: NCB - `9704198526191432198` - `NGUYEN VAN A` - `07/15` - OTP `123456`

## Deploy Render

Service `hoalvh-baitap-web-tuan7-ch07cart-vnpay` trong `render.yaml`,
them 2 bien moi truong `VNP_TMN_CODE`, `VNP_HASH_SECRET`.
