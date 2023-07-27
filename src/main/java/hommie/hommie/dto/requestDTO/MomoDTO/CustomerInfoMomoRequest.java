package hommie.hommie.dto.requestDTO.MomoDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class CustomerInfoMomoRequest {
    private String name;
    private String phoneNumber;
    private String email;   
}
