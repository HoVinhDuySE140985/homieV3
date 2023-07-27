package hommie.hommie.dto.responseDTO;

import hommie.hommie.entity.ItemDetail;
import hommie.hommie.entity.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDTO {
    private Long id;
    private String avatar;
    private String name;
    private String material;
    private LocalDate createDate;
    private List<ItemDetailResponseDTO> details;
    private Long subId;
    private String subName;
    private Long cateId;
    private String cateName;
    private String status;
    private Boolean checkNumber;
    private int buyNumber;
}
