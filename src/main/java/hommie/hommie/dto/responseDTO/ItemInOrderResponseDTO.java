package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInOrderResponseDTO {
    private Long itemId;
    private String itemName;
    private String itemImage;
    private String material;
    private String size;
    private String color;
    private Integer orderQuantity;
    private BigDecimal price;
}
