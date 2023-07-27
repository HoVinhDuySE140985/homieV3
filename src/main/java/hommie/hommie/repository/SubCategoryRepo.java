package hommie.hommie.repository;

import hommie.hommie.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findAllByCategory_Id(Long cateId);
}
