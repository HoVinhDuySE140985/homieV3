package hommie.hommie.controller;

import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.entity.FeedBack;
import hommie.hommie.service.serviceinterface.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {

    @Autowired
    FeedBackService feedBackService;

    @PostMapping("create-feedback")
    @PermitAll
    public ResponseEntity<ResponseDTO> createFeedBack(@RequestParam @Validated String email,
                                                      @RequestParam @Validated String content){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(feedBackService.createFeedBack(email,content));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("view-all-feedback")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> viewAllFeedBack(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(feedBackService.viewAllFeedBack());
        responseDTO.setResult(feedBackService.viewAllFeedBack().size());
        return ResponseEntity.ok().body(responseDTO);
    }
}
