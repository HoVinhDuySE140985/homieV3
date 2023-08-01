package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.PromotionRequestDTO;
import hommie.hommie.dto.responseDTO.PromotionResponseDTO;

import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequestDTO promotionRequestDTO);
    List<PromotionResponseDTO> getAllPromotion(Long userId);

    String deletePromo(Long promoId);

    PromotionResponseDTO getPromotionInfo(String proCode);
}
