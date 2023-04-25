package com.example.hw04s_att.services;

import com.example.hw04s_att.models.Orders;
import com.example.hw04s_att.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }
    
    
    public List<Orders> getOrdersByPersonIdAndProductId(int person_id, int product_id) {
        return orderRepository.findByPersonIdAndProductId(person_id, product_id);
    }
    
    public Orders getOrdersId(int id) {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        return optionalOrders.orElse(null);
    }
    
    public List<Orders> getOrdersByPersonId(int id) {
        return orderRepository.findByPersonId(id);
    }
    public List<Orders> getOrdersByNumber(String number) {
        return orderRepository.findByNumberContainingIgnoreCase(number);
    }
    
    @Transactional
    public void saveOrders(Orders orders) {
        orderRepository.save(orders);
    }
    
    @Transactional
    public void updateOrders(int id, Orders orders) {
        orders.setId(id);
        orderRepository.save(orders);
    }
    
    @Transactional
    public void deleteOrders(int id) {
        orderRepository.deleteById(id);
    }
   
    @Transactional
    public void deleteOrdersByIdAndProductId(int order_id, int product_id) {
        orderRepository.deleteOrdersByIdAndProductId(order_id, product_id);
    }
    
}
