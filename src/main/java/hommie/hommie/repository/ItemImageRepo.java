package hommie.hommie.repository;

import hommie.hommie.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepo extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findAllByItemDetail_Id(Long itemDetailId);
    ItemImage findFirstByItemDetail_Id(Long itemDetailId);
}
