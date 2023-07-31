package hommie.hommie.dto.requestDTO;

import hommie.hommie.dto.responseDTO.ItemImageResponseDTO;
import hommie.hommie.entity.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateItemDetailRequestDTO {
    private Long itemDetailId;
    private BigDecimal price;
    List<ItemImageResponseDTO> listImage;
}
