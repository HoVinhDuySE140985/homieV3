package hommie.hommie.service.serviceinterface;

import hommie.hommie.entity.FeedBack;

import java.util.List;

public interface FeedBackService {
    String createFeedBack(String email, String content);
    List<FeedBack> viewAllFeedBack();
}
