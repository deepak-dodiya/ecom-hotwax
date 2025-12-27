package com.example.ecom.dto;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private Integer shippingContactMechId;
    private Integer billingContactMechId;
}
