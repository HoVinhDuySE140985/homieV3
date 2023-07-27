package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.responseDTO.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    String createCategory(String name);

    List<CategoryResponseDTO> getAllCategory();

    String updateCategory(Long id, String name);

    String deleteCategory(Long cateId);
}
