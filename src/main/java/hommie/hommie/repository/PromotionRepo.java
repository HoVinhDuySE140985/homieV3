package hommie.hommie.repository;

import hommie.hommie.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepo extends JpaRepository<Promotion, Long> {
    Promotion findByCode(String code);
}
