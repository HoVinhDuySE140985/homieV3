package hommie.hommie.dto.responseDTO;

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
public class DetailsResponse {
    private Long id;
    private String size;
    private String color;
    private int quantity;
    private BigDecimal price;
    private String description;
    private List<ItemImageResponseDTO> listImage;
}
