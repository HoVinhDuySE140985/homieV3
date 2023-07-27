package hommie.hommie.controller;

import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("send-notification")
    public ResponseEntity<ResponseDTO> sendNoti(@RequestParam Long userId,
                                                @RequestParam String title,
                                                @RequestParam String body){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(notificationService.sendNotification(userId,title,body));
        return ResponseEntity.ok().body(responseDTO);
    }
}
