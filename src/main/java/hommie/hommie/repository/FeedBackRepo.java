package hommie.hommie.repository;

import hommie.hommie.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepo extends JpaRepository<FeedBack, Long> {
}
