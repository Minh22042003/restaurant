package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddOrderOnlineRequest;
import com.example.restaurant.dto.request.FoodOrderDTO;
import com.example.restaurant.dto.request.UpdateOrderRequest;
import com.example.restaurant.entity.*;
import com.example.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    @Autowired
    private OrderTotalRepository orderTotalRepository;

    public Order addOrderOnline(AddOrderOnlineRequest request){
        //tao order
        Order order = Order.builder()
                .customer_name(request.getCustomer_name())
                .customer_mail(request.getCustomer_mail())
                .customer_address(request.getCustomer_address())
                .order_type(TypeOrder.Online.name())
                .order_status(false)
                .build();

        //tim user
        Optional<User> user = userRepository.findByEmail(request.getCustomer_mail());
        user.ifPresent(order::setUser);

        //Tong tien cua order
        int sum = 0;

        //tao FoodOrder
        Set<FoodOrder> foodOrders = new HashSet<>();
        for (FoodOrderDTO order1 : request.getFoodOrderDTOS()){
            Optional<Food> food = foodRepository.findById(order1.getFoodId());
            if (food.isPresent()){
                sum = sum + (food.get().getPrice()*order1.getQuatity());
                FoodOrder foodOrder = FoodOrder.builder()
                        .food(food.get())
                        .order(order)
                        .item_quantity(order1.getQuatity())
                        .build();
                foodOrderRepository.save(foodOrder);
                foodOrders.add(foodOrder);
            }
        }

        //tao orderTotal
        OrderTotal orderTotal = OrderTotal.builder()
                .order(order)
                .amount(sum)
                .build();
        orderTotalRepository.save(orderTotal);

        order.setFoodOrders(foodOrders);
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
