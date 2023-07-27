package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.responseDTO.*;
import hommie.hommie.dto.responseDTO.momoDTO.MomoResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    ResponseEntity<MomoResponse> createLinkOrder(BigDecimal feeShip, BigDecimal totalPrice,String paymentType,String shipAddress,Long userId, String phoneNumber, String promoCode);

    OrderResponseDTO createOrder(Long userId, BigDecimal feeShip, String paymentType,String shipAddress, String phoneNumber, String promoCode);

    List<OrderDetailResponseDTO> getAllMyOrder(Long userId);

    List<OrderDetailResponseDTO> getAllOrder();

    List<OrderDetailResponseDTO>getAllOrderByStatus(String status);

    String convertOrderStatus(Long orderId);

    String cancelOrder(Long orderId, String cancelReason);

    List<RevenueResponseDTO> revenueCalculation(String month, String year);
    List<OrderDetailResponseDTO> searchOrderByOrderCode(String orderCode);

    List<ItemInOrderResponseDTO> getAllItemInOrder(String orderCode);
}
