package hommie.hommie.dto.responseDTO;

import hommie.hommie.entity.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemDetailResponseDTO {
    private Long id;
    private String size;
    private String color;
    private int quantity;
    private List<ItemImageResponseDTO> itemImages;
    private BigDecimal price;
    private String description;
}
