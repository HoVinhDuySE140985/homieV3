package hommie.hommie.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;

}
