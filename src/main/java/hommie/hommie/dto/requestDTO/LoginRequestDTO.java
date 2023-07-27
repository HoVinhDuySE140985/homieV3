package hommie.hommie.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginRequestDTO {
    private String email;
    private String password;
    private String fcmKey;
}
