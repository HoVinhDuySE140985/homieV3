package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.responseDTO.CategoryResponseDTO;
import hommie.hommie.entity.Category;
import hommie.hommie.entity.SubCategory;
import hommie.hommie.repository.CategoryRepo;
import hommie.hommie.repository.SubCategoryRepo;
import hommie.hommie.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    SubCategoryRepo subCategoryRepo;

    @Override
    public String createCategory(String name) {
        String mess = "Tạo Mới Thất Bại";
        Category category = categoryRepo.findByName(name);
        if (category != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Danh mục này đã tồn tại");
        } else {
            category = Category.builder()
                    .name(name)
                    .status("ACTIVE")
                    .build();
            categoryRepo.save(category);
            mess = "Tạo Mới Thành Công";
        }
        return mess;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        List<CategoryResponseDTO> list = new ArrayList<>();
        List<Category> categories = categoryRepo.findAll();
        for (Category category : categories) {
            CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
            list.add(categoryResponseDTO);
        }
        return list;
    }

    @Override
    public String updateCategory(Long id, String name) {
        String mess = "Cập NHật Thất Bại";
        Category category = categoryRepo.findById(id).get();
        if(category != null){
            Category nameCate = categoryRepo.findByName(name);
            if (nameCate != null) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "Tên Danh Mục Đã Tồn Tại");
            }else {
                category.setName(name);
                categoryRepo.save(category);
                mess = "Cập Nhập Thành Công";
            }
        }
        return mess;
    }

    @Override
    public String deleteCategory(Long cateId) {
        String mess = "Cập NHật Thất Bại";
        Category category = categoryRepo.findById(cateId).get();
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Tên Danh Mục Đã Tồn Tại");
        }else {
            List<SubCategory> subCategories = subCategoryRepo.findAllByCategory_Id(cateId);
            if (subCategories.isEmpty()){
                categoryRepo.delete(category);
                mess = "Cập Nhập Thành Công";
            }
        }
        return mess;
    }
}

