package hommie.hommie.service.serviceimplement;

import hommie.hommie.entity.FeedBack;
import hommie.hommie.entity.User;
import hommie.hommie.repository.FeedBackRepo;
import hommie.hommie.repository.UserRepo;
import hommie.hommie.service.serviceinterface.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FeedBackRepo feedBackRepo;
    @Override
    public String createFeedBack(String email, String content) {
        String mess = "Gửi FeedBack Thất Bại ";
        User user = userRepo.findUserByEmail(email);
        if (user == null){
            FeedBack feedBack = FeedBack.builder()
                    .email(email)
                    .content(content)
                    .build();
            feedBackRepo.save(feedBack);
            mess = "Gửi FeedBack Thành Công ";
        }else {
            FeedBack feedBack = FeedBack.builder()
                    .email(email)
                    .content(content)
                    .user(user)
                    .build();
            feedBackRepo.save(feedBack);
            mess = "Gửi FeedBack Thành Công ";
        }
        return mess;
    }

    @Override
    public List<FeedBack> viewAllFeedBack() {
        List<FeedBack> feedBacks = feedBackRepo.findAll();
        if (feedBacks.isEmpty()){
            throw new ResponseStatusException(HttpStatus.valueOf(400),"Chưa Có Ý Kiến Góp Ý Nào !");
        }
        return feedBacks;
    }
}
