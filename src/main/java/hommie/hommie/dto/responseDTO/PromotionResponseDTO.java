package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PromotionResponseDTO {
    private Long id;
    private String type;
    private String title;
    private String image;
    private String code;
    private LocalDate dateStart;
    private LocalDate dateExp;
    private String description;
    private float value;
    private float minValueOrder;
    private float maxValueDiscount;
    private int quantity;
    private String status;

}
