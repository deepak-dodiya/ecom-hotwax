package com.example.ecom.dto;

import lombok.Data;

@Data
public class UpdateOrderItemRequest {
    private Integer quantity;
    private String status;
}
