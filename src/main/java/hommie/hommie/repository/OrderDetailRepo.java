package hommie.hommie.repository;

import hommie.hommie.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findAllByOrder_Id(Long orderId);
}
