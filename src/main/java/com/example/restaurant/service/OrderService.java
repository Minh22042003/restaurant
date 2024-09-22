package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddOrderRequest;
import com.example.restaurant.dto.request.UpdateOrderRequest;
import com.example.restaurant.entity.Order;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order addOrder(AddOrderRequest request){
        Order order = Order.builder()
                .customer_name(request.getCustomer_name())
                .customer_mail(request.getCustomer_mail())
                .customer_address(request.getCustomer_address())
                .order_status(false)
                .build();
        if (request.getUser_id().isEmpty()){
            order.setOrder_type("Directly at the counter");
        }
        else {
            order.setOrder_type("Online");
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(String id){
        orderRepository.deleteById(id);
    }

    public Page<Order> listOrder(int page, int size){
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    public Order orderDetail(String id){
        if (orderRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order does not exists");
        }
        return orderRepository.findById(id).get();
    }

    public Order updateOrder(UpdateOrderRequest request, String id){
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isEmpty()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Order does not exists");
            }

            Order order = orderOptional.get();

            order.setCustomer_name(request.getCustomer_name());
            order.setCustomer_mail(request.getCustomer_mail());
            order.setOrder_type(request.getOrder_type());
            order.setCustomer_address(request.getCustomer_address());

            return orderRepository.save(order);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating order: " + e.getMessage());
        }

    }
}
