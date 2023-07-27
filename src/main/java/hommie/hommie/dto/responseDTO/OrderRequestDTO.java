package hommie.hommie.dto.responseDTO;

import hommie.hommie.dto.requestDTO.CartRequestDTO;
import hommie.hommie.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDTO {
    private String paymentType;
    private String promoCode;
    private Long userId;
    private String phoneNumber;
    private String shipAddress;
    private BigDecimal feeShip;
}
