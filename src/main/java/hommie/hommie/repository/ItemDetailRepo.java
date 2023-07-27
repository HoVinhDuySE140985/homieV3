package hommie.hommie.repository;

import hommie.hommie.dto.responseDTO.ItemDetailResponseDTO;
import hommie.hommie.entity.ItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDetailRepo extends JpaRepository<ItemDetail, Long> {
    List<ItemDetail> findAllByItem_Id(Long itemId);

}
