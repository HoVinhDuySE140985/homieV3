package hommie.hommie.repository;

import hommie.hommie.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Id(Long userId);

    Order findByOrderCode(String orderCode);
    @Query("Select o \n" +
            "From Order as o \n" +
            "where year(o.orderDate) = :year and month(o.orderDate) = :month and o.status = 'Hoàn Thành'")
    List<Order> findAllByOrderDate(int year, int month);

    @Query("Select o \n" +
            "From Order as o \n" +
            "where year(o.orderDate) = :year and o.status = 'Hoàn Thành'")
    List<Order> findAllByOrderDate(int year);

    List<Order> findAllByStatus(String status);
}
