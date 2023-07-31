package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.responseDTO.momoDTO.MomoResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface PaymentService {
    ResponseEntity<MomoResponse> getPaymentMomoV1(String codeOrder, BigDecimal feeShip, Long userId, BigDecimal totalPrice, String paymentType, String shipAddress, String phoneNumber, String promoCode,String userReceive);
}
