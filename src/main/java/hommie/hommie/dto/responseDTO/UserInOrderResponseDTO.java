package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserInOrderResponseDTO {
    private String emailOrder;
    private String userOrder;
    private String userReceive;
    private Long orderId;
    private String phoneNumber;
    private String shipAddress;
}
