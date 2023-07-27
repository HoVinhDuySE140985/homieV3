package hommie.hommie.dto.responseDTO;

import hommie.hommie.dto.requestDTO.ItemImageRequestDTO;
import hommie.hommie.entity.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateItemResponseDTO {
    private Long id;
    private String name;
    private String material;
    private List<ItemDetailResponseDTO> details;
    private Long subId;
}
