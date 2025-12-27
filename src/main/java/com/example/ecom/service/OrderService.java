package com.example.ecom.service;

import com.example.ecom.dto.CreateOrderRequest;
import com.example.ecom.dto.UpdateOrderItemRequest;
import com.example.ecom.dto.UpdateOrderRequest;
import com.example.ecom.entity.*;
import com.example.ecom.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderHeaderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactMechRepository contactMechRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderHeader createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Customer ID"));

        ContactMech shipping = contactMechRepository.findById(request.getShippingContactMechId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Shipping Contact ID"));

        ContactMech billing = contactMechRepository.findById(request.getBillingContactMechId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Billing Contact ID"));

        OrderHeader order = new OrderHeader();
        order.setOrderDate(request.getOrderDate());
        order.setCustomer(customer);
        order.setShippingContactMech(shipping);
        order.setBillingContactMech(billing);
        order.setOrderItems(new ArrayList<>());

        if (request.getOrderItems() != null) {
            for (CreateOrderRequest.OrderItemRequest itemReq : request.getOrderItems()) {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid Product ID: " + itemReq.getProductId()));

                OrderItem item = new OrderItem();
                item.setOrderHeader(order);
                item.setProduct(product);
                item.setQuantity(itemReq.getQuantity());
                item.setStatus(itemReq.getStatus());

                order.getOrderItems().add(item);
            }
        }

        return orderRepository.save(order);
    }

    public OrderHeader getOrder(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Transactional
    public OrderHeader updateOrder(Integer orderId, UpdateOrderRequest request) {
        OrderHeader order = getOrder(orderId);

        if (request.getShippingContactMechId() != null) {
            ContactMech shipping = contactMechRepository.findById(request.getShippingContactMechId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Shipping Contact ID"));
            order.setShippingContactMech(shipping);
        }

        if (request.getBillingContactMechId() != null) {
            ContactMech billing = contactMechRepository.findById(request.getBillingContactMechId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Billing Contact ID"));
            order.setBillingContactMech(billing);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public OrderHeader addOrderItem(Integer orderId, CreateOrderRequest.OrderItemRequest itemReq) {
        OrderHeader order = getOrder(orderId);
        Product product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Product ID"));

        OrderItem item = new OrderItem();
        item.setOrderHeader(order);
        item.setProduct(product);
        item.setQuantity(itemReq.getQuantity());
        item.setStatus(itemReq.getStatus());

        order.getOrderItems().add(item);
        return orderRepository.save(order);
    }

    @Transactional
    public OrderItem updateOrderItem(Integer orderId, Integer orderItemSeqId, UpdateOrderItemRequest request) {
        // Verify order exists mainly
        if (!orderRepository.existsById(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        OrderItem item = orderItemRepository.findById(orderItemSeqId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Item not found"));

        // Basic check to ensure item belongs to order
        if (!item.getOrderHeader().getOrderId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item does not belong to specified Order");
        }

        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getStatus() != null) {
            item.setStatus(request.getStatus());
        }

        return orderItemRepository.save(item);
    }

    @Transactional
    public void deleteOrderItem(Integer orderId, Integer orderItemSeqId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        OrderItem item = orderItemRepository.findById(orderItemSeqId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Item not found"));

        if (!item.getOrderHeader().getOrderId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item does not belong to specified Order");
        }

        orderItemRepository.delete(item);
    }
}
