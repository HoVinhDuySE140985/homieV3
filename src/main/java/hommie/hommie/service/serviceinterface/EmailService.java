package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.responseDTO.EmailResponseDTO;

public interface EmailService {
    String sendSimpleMail(EmailResponseDTO dto);
}
