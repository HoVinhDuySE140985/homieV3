package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private String orderCode;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private String phoneNumber;
    private String shipAddress;
    private String paymentType;
}
