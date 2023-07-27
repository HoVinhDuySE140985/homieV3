package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportResponseDTO {
    private Long reportId;
    private String imageReport;
    private String reason;
    private String email;
    private String orderCode;
}
