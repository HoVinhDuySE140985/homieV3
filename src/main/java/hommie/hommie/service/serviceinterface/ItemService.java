package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.CreateItemRequestDTO;
import hommie.hommie.dto.requestDTO.ItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateItemRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateUserRequestDTO;
import hommie.hommie.dto.responseDTO.CreateItemResponseDTO;
import hommie.hommie.dto.responseDTO.ItemDetailResponseDTO;
import hommie.hommie.dto.responseDTO.ItemResponseDTO;

import java.util.List;

public interface ItemService {
    CreateItemResponseDTO createItem(CreateItemRequestDTO createItemRequestDTO);


    String updateItem( UpdateItemRequestDTO updateItemRequestDTO);

    String updateStatus(Long itemId);

    List<ItemResponseDTO> getAllItemBy(Long cateId, Long subId, String keyWord);

    List<ItemResponseDTO> getTopItem();

    List<ItemResponseDTO> getAllItem();
}
