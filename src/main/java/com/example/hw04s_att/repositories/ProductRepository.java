package com.example.hw04s_att.repositories;

import com.example.hw04s_att.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    //поиск по части строки без учета регистра
    List<Product> findByTitleContainingIgnoreCase(String name);
    
    //часть строки + суммы
//    @Query(value = "select * from product where ((lower(title) like '%?1%') or (lower(title) like '%?1') or (lower(title) like '?1%')) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    @Query(value = "select * from product p where ((lower(p.title) like concat('%',?1,'%')) or (lower(p.title) LIKE concat(?1,'%')) OR (lower(p.title) LIKE concat('%',?1))) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String name, float Ot, float Do);
    
    @Query(value = "select * from product p where ((lower(p.title) like concat('%',?1,'%')) or (lower(p.title) LIKE concat(?1,'%')) OR (lower(p.title) LIKE concat('%',?1))) and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String name, float Ot, float Do);
    
    @Query(value = "select * from product p where ((lower(p.title) like concat('%',?1,'%')) or (lower(p.title) LIKE concat(?1,'%')) OR (lower(p.title) LIKE concat('%',?1))) and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String name, float Ot, float Do);
    
    //часть строки + суммы + категории
    @Query(value = "select * from product p where ((lower(p.title) like concat('%',?1,'%')) or (lower(p.title) LIKE concat(?1,'%')) OR (lower(p.title) LIKE concat('%',?1))) and (price >= ?2 and price <= ?3) and category_id = ?4 order by price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String name, float Ot, float Do, int category);
    
    @Query(value = "select * from product p where ((lower(p.title) like concat('%',?1,'%')) or (lower(p.title) LIKE concat(?1,'%')) OR (lower(p.title) LIKE concat('%',?1))) and (price >= ?2 and price <= ?3) and category_id = ?4 order by price desc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String name, float Ot, float Do, int category);
    
}
