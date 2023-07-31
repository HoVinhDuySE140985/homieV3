package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.PromotionRequestDTO;
import hommie.hommie.dto.responseDTO.PromotionResponseDTO;
import hommie.hommie.entity.Order;
import hommie.hommie.entity.Promotion;
import hommie.hommie.repository.OrderRepo;
import hommie.hommie.repository.PromotionRepo;
import hommie.hommie.service.serviceinterface.PromotionService;
import hommie.hommie.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepo promotionRepo;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequestDTO promotionRequestDTO) {
        String code = Utilities.randomAlphaNumeric(7);
        Promotion promotion = Promotion.builder()
                .type(promotionRequestDTO.getType())
                .title(promotionRequestDTO.getTitle())
                .image(promotionRequestDTO.getImage())
                .code(code)
                .value(promotionRequestDTO.getValue())
                .minValueOrder(promotionRequestDTO.getMinValueOrder())
                .maxValueDiscount(promotionRequestDTO.getMaxValueDiscount())
                .quantity(promotionRequestDTO.getQuantity())
                .dateStart(promotionRequestDTO.getDateStart())
                .dateExp(promotionRequestDTO.getDateExp())
                .description(promotionRequestDTO.getDescription())
                .status("ACTIVE")
                .build();
        promotion = promotionRepo.save(promotion);
        PromotionResponseDTO promotionResponseDTO = PromotionResponseDTO.builder()
                .id(promotion.getId())
                .title(promotion.getTitle())
                .type(promotion.getType())
                .image(promotion.getImage())
                .code(promotion.getCode())
                .value(promotion.getValue())
                .minValueOrder(promotion.getMinValueOrder())
                .maxValueDiscount(promotion.getMaxValueDiscount())
                .quantity(promotion.getQuantity())
                .dateStart(promotion.getDateStart())
                .dateExp(promotion.getDateExp())
                .description(promotion.getDescription())
                .status(promotion.getStatus())
                .build();
        return promotionResponseDTO;
    }

    @Override
    public List<PromotionResponseDTO> getAllPromotion(Long userId) {
        List<PromotionResponseDTO> list = new ArrayList<>();
        List<Promotion> promotionList = promotionRepo.findAll();
        if(userId!=null){
            List<Order> orderList = orderRepo.findAllByUser_Id(userId);
            for (Order order: orderList) {
                if( null != order.getPromotion()){
                    promotionList.remove(order.getPromotion());
                }
            }
        }
        if (promotionList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Hiện Chưa Voucher Có Thể Sử Dụng");
        } else {
            for (Promotion promotion : promotionList) {
                if (promotion.getStatus().equals("ACTIVE") && promotion.getQuantity()>=1){
                    PromotionResponseDTO promotionResponseDTO = PromotionResponseDTO.builder()
                            .id(promotion.getId())
                            .type(promotion.getType())
                            .title(promotion.getTitle())
                            .image(promotion.getImage())
                            .code(promotion.getCode())
                            .value(promotion.getValue())
                            .dateStart(promotion.getDateStart())
                            .dateExp(promotion.getDateExp())
                            .description(promotion.getDescription())
                            .status(promotion.getStatus())
                            .build();
                    list.add(promotionResponseDTO);
                }
            }
        }
        return list;
    }

    @Override
    public String deletePromo(Long promoId) {
        String mess = "";
        Promotion promotion = promotionRepo.findById(promoId).get();
        if (promotion == null){
            throw new ResponseStatusException(HttpStatus.valueOf(400),"Promo Không Tồn Tại");
        }else {
            promotion.setStatus("DEACTIVE");
            promotionRepo.save(promotion);
            mess = "Xóa Thành Công";
        }
        return mess;
    }
}
