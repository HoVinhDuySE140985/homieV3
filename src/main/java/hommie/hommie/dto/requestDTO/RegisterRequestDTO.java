package hommie.hommie.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
