package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.responseDTO.NotificationResponseDTO;

public interface NotificationService {

    NotificationResponseDTO sendNotification(Long userId, String title, String body);
}
