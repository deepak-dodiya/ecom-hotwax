1. create order --post  http://localhost:8080/orders

{
  "orderDate": "2025-12-27",
  "customerId": 1,
  "shippingContactMechId": 1,
  "billingContactMechId": 2,
  "orderItems": [
    { "productId": 1, "quantity": 2, "status": "PENDING" },
    { "productId": 2, "quantity": 1, "status": "PENDING" }
  ]
}


2. retrieve order details  --get  http://localhost:8080/orders/2

3. update order --put   http://localhost:8080/orders/2

 {
  "shippingContactMechId": 2,
  "billingContactMechId": 1
}


4. delete an order  ---delete  http://localhost:8080/orders/1

5. add an order item --post   http://localhost:8080/orders/3/items

{
  "productId": 3,
  "quantity": 1,
  "status": "APPROVED"
}

6. update an order item --- put  http://localhost:8080/orders/5/items/10

{
  "quantity": 2,
  "status": "APPROVED"
}

7. delete an order item  ---delete  http://localhost:8080/orders/5/items/10



