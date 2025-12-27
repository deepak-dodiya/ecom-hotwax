package com.example.ecom.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrderRequest {
    private LocalDate orderDate;
    private Integer customerId;
    private Integer shippingContactMechId;
    private Integer billingContactMechId;
    private List<OrderItemRequest> orderItems;

    @Data
    public static class OrderItemRequest {
        private Integer productId;
        private Integer quantity;
        private String status;
    }
}
