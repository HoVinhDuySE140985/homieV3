package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.ItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateItemDetailRequestDTO;
import hommie.hommie.dto.responseDTO.DetailInfoResponseDTO;
import hommie.hommie.dto.responseDTO.DetailsResponse;
import hommie.hommie.dto.responseDTO.ItemDetailResponseDTO;

import java.util.List;

public interface ItemDetailService {
    List<DetailsResponse> getAllDetail(Long itemId);
    String updateItemDetail(UpdateItemDetailRequestDTO updateItemRequestDTO);
    ItemDetailResponseDTO createItemDetail(Long itemId, ItemDetailRequestDTO itemDetailRequestDTO);

    String updateItemDetailStatus(Long itemDetailId);
}
