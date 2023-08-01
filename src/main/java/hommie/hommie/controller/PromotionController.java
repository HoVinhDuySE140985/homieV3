package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.PromotionRequestDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    PromotionService promotionService;

    @PostMapping("create-promotion")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> createPromotion(@RequestBody @Validated PromotionRequestDTO promotionRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.createPromotion(promotionRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-promotion")
    @PermitAll
    public  ResponseEntity<ResponseDTO> getAllPromotion(@RequestParam(required = false) @Validated Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getAllPromotion(userId));
        responseDTO.setResult(promotionService.getAllPromotion(userId).size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("delete-promotion")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> deletePromotion(@RequestParam Long promoId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.deletePromo(promoId));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-promotion-info")
    @PermitAll
    public  ResponseEntity<ResponseDTO> getPromotionInfo(@RequestParam @Valid String  proCode){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getPromotionInfo(proCode));
        return ResponseEntity.ok().body(responseDTO);
    }

}
