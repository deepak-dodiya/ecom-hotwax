package com.example.ecom.controller;

import com.example.ecom.dto.CreateOrderRequest;
import com.example.ecom.dto.UpdateOrderItemRequest;
import com.example.ecom.dto.UpdateOrderRequest;
import com.example.ecom.entity.OrderHeader;
import com.example.ecom.entity.OrderItem;
import com.example.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderHeader> createOrder(@RequestBody CreateOrderRequest request) {
        OrderHeader createdOrder = orderService.createOrder(request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderHeader> getOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderHeader> updateOrder(@PathVariable Integer orderId,
            @RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderHeader> addOrderItem(@PathVariable Integer orderId,
            @RequestBody CreateOrderRequest.OrderItemRequest itemReq) {
        return ResponseEntity.ok(orderService.addOrderItem(orderId, itemReq));
    }

    @PutMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Integer orderId,
            @PathVariable Integer orderItemSeqId, @RequestBody UpdateOrderItemRequest request) {
        return ResponseEntity.ok(orderService.updateOrderItem(orderId, orderItemSeqId, request));
    }

    @DeleteMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemSeqId) {
        orderService.deleteOrderItem(orderId, orderItemSeqId);
        return ResponseEntity.noContent().build();
    }
}
