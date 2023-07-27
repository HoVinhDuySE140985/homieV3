package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.CreateSubCateRequestDTO;
import hommie.hommie.dto.responseDTO.SubCategoryResponseDTO;

import java.util.List;

public interface SubCategoryService {
    SubCategoryResponseDTO createSubCategory(CreateSubCateRequestDTO createSubCateRequestDTO);
    List<SubCategoryResponseDTO> getAllSubBy(Long cateId);
    List<SubCategoryResponseDTO> getAllSub();
    String updateSubCateStatus(Long subCateId);
    String updateSubCate(Long subId, String name);
    String deleteSubCate(Long subId);
}
