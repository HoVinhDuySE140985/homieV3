package hommie.hommie.dto.requestDTO;

import hommie.hommie.dto.responseDTO.ItemImageResponseDTO;
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
public class CreateItemRequestDTO {
    private String name;
    private String material;
    private String avatar;
//    private List<ItemDetailRequestDTO> itemDetails;
    private Long subCateId;
}
