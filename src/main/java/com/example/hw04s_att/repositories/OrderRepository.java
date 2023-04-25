package com.example.hw04s_att.repositories;

import com.example.hw04s_att.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Orders, Integer> {
  
  List<Orders> findByPersonId(int id);
  List<Orders> findByPersonIdAndProductId(int person_id, int product_id);
  
  @Modifying
  @Query(value = "delete from orders o where o.id = ?1 and o.product_id = ?2", nativeQuery = true)
  void deleteOrdersByIdAndProductId(int order_id, int product_id);
  
  @Query(value = "select * from orders o where (lower(o.number) like concat('%',?1,'%')) or (lower(o.number) LIKE concat(?1,'%')) OR (lower(o.number) LIKE concat('%',?1)) order by date_time", nativeQuery = true)
  List<Orders> findByNumberContainingIgnoreCase(String number);
  
}
