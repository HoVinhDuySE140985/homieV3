package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponseDTO {
    private Long cartItemId;
    private Long itemDetailId;
    private String itemName;
    private String itemImage;
    private String material;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal price;
}
