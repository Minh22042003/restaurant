package com.example.restaurant.controller;

import com.example.restaurant.dto.request.AddOrderRequest;
import com.example.restaurant.dto.request.UpdateOrderRequest;
import com.example.restaurant.entity.Order;
import com.example.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestParam("customer_mail") String customer_mail,
                                         @RequestParam("customer_name") String customer_name,
                                         @RequestParam("customer_address") String customer_address,
                                          @RequestParam(value = "user_id", required = false) String user_id){
        AddOrderRequest request = AddOrderRequest.builder()
                .customer_name(customer_name)
                .customer_mail(customer_mail)
                .customer_address(customer_address)
                .user_id(user_id)
                .build();
        return ResponseEntity.ok().body(orderService.addOrder(request));
    }

    @GetMapping
    public ResponseEntity<Page<Order>> listOrder(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(orderService.listOrder(page, size));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> orderDetail(@PathVariable String orderId){
        return ResponseEntity.ok().body(orderService.orderDetail(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId,
                                           @RequestParam("customer_mail") String customer_mail,
                                           @RequestParam("customer_name") String customer_name,
                                           @RequestParam("customer_address") String customer_address,
                                           @RequestParam("order_type") String order_type){
        UpdateOrderRequest updateOrderRequest = UpdateOrderRequest.builder()
                .customer_name(customer_name)
                .customer_mail(customer_mail)
                .customer_address(customer_address)
                .order_type(order_type)
                .build();

        Order order = orderService.updateOrder(updateOrderRequest, orderId);

        return ResponseEntity.ok().body(order);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
    }
}
