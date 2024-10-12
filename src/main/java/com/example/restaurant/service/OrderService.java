package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddOrderOnlineRequest;
import com.example.restaurant.entity.*;
import com.example.restaurant.repository.*;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodOrderRepository foodOrderRepository;


    @Transactional
    public Order addOrderOnline(AddOrderOnlineRequest request) throws BadRequestException {
        Optional<User> user = userRepository.findByEmail(request.getCustomer_mail());

        List<FoodOrder> foodOrderList = new ArrayList<>();

        Order order = new Order();

        if (user.isPresent()){
            order.setUser(user.get());
            order.setDescription(request.getDescription());


        }else {
            throw new BadRequestException("Bad data");
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

}
