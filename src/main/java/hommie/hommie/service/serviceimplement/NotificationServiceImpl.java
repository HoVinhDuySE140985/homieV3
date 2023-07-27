package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.responseDTO.MyNotificationDTO;
import hommie.hommie.dto.responseDTO.NotificationResponseDTO;
import hommie.hommie.entity.User;
import hommie.hommie.repository.UserRepo;
import hommie.hommie.service.serviceinterface.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableAsync
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserRepo userRepo;

    private String uri = "https://fcm.googleapis.com/fcm/send";
    private String key = "Authorization";
    // key
    private String value = "key=AAAAKdNMbsY:APA91bEaZmxvvv7dfVUSKSia-fW0UIrgNhVKWsEw680niQN_QBvPyolxhtJIlcbfB9D8l89KF31sbBjpD_Aw5kq-4kyG-yyPJakwEw93s6whb9ZcO3vIYRPMmj1Zx50RvCLqicTBcSSa";


    @Override
    public NotificationResponseDTO sendNotification(Long userId, String title, String body) {
        RestTemplate restTemplate = new RestTemplate();
        NotificationResponseDTO responseDTO = null;
        try {
            User user = userRepo.findById(userId).get();
            MyNotificationDTO myNotificationDTO = MyNotificationDTO.builder()
                    .title(title)
                    .body(body)
                    .build();
            responseDTO = NotificationResponseDTO.builder()
                    .to(user.getTokenDevice())
                    .notification(myNotificationDTO)
                    .build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(key,value);
            HttpEntity<NotificationResponseDTO> request = new HttpEntity<>(responseDTO,httpHeaders);
            restTemplate.postForObject(uri,request,NotificationResponseDTO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseDTO;
    }
}
