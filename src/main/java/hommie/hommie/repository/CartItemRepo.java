package hommie.hommie.repository;

import hommie.hommie.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUser_Id(Long userId);
}
