package com.example.board.shop.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ShopOrder, Long> {

    List<ShopOrder> findAllByOrderByIdDesc();

    List<ShopOrder> findAllByUsernameOrderByIdDesc(String username);
}
