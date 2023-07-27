package hommie.hommie.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartRequestDTO {
    private Long userId;
    private Long itemDetailId;
    private Integer quantity;
}
