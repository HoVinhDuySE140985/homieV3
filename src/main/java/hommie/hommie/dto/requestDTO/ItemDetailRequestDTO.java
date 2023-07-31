package hommie.hommie.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailRequestDTO {
    private String color;
    private String size;
    private BigDecimal price;
    private String description;
    private List<String> imageLists;
}
