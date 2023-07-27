package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResponseDTO {
    private Long orderId;
    private LocalDate orderDate;
    private String orderCode;
    private List<ItemDetailOrderResponse> listItems;
    private BigDecimal totalPrice;
    private String orderStatus;
    private String phoneNumber;
    private String address;
}
