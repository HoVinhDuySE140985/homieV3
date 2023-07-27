package hommie.hommie.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PromotionRequestDTO {
    private String type;
    private String title;
    private String image;
    private LocalDate dateStart;
    private LocalDate dateExp;
    private String description;
    private Double value;
}
