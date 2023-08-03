package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.CreateSubCateRequestDTO;
import hommie.hommie.dto.responseDTO.SubCategoryResponseDTO;
import hommie.hommie.entity.Category;
import hommie.hommie.entity.Item;
import hommie.hommie.entity.SubCategory;
import hommie.hommie.repository.CategoryRepo;
import hommie.hommie.repository.ItemRepo;
import hommie.hommie.repository.SubCategoryRepo;
import hommie.hommie.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    SubCategoryRepo subCategoryRepo;

    @Autowired
    ItemRepo itemRepo;

    @Override
    public SubCategoryResponseDTO createSubCategory(CreateSubCateRequestDTO createSubCateRequestDTO) {
        Category category = categoryRepo.findById(createSubCateRequestDTO.getCateId()).get();
        SubCategory subCategory = SubCategory.builder()
                .name(createSubCateRequestDTO.getName())
                .category(category)
                .status("ACTIVE")
                .build();
        subCategoryRepo.save(subCategory);
        SubCategoryResponseDTO categoryResponseDTO = SubCategoryResponseDTO.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .cateId(subCategory.getCategory().getId())
                .build();
        return categoryResponseDTO;
    }

    @Override
    public List<SubCategoryResponseDTO> getAllSubBy(Long cateId) {
        List<SubCategory> listSub = subCategoryRepo.findAllByCategory_Id(cateId);
        List<SubCategoryResponseDTO> listSubcategory = new ArrayList<>();
        for (SubCategory subCategory: listSub) {
            SubCategoryResponseDTO dto = SubCategoryResponseDTO.builder()
                    .id(subCategory.getId())
                    .name(subCategory.getName())
                    .status(subCategory.getStatus())
                    .cateId(subCategory.getCategory().getId())
                    .cateName(categoryRepo.findById(subCategory.getCategory().getId()).get().getName())
                    .build();
            listSubcategory.add(dto);
        }
        return listSubcategory;
    }

    @Override
    public List<SubCategoryResponseDTO> getAllSub() {
        List<SubCategoryResponseDTO> listSubcategory = new ArrayList<>();
        List<SubCategory> subCategories = subCategoryRepo.findAll();
        for (SubCategory subCate: subCategories) {
            SubCategoryResponseDTO dto = SubCategoryResponseDTO.builder()
                    .id(subCate.getId())
                    .name(subCate.getName())
                    .status(subCate.getStatus())
                    .cateId(subCate.getCategory().getId())
                    .cateName(categoryRepo.findById(subCate.getCategory().getId()).get().getName())
                    .build();
            listSubcategory.add(dto);
        }
        return listSubcategory;
    }

    @Override
    public String updateSubCateStatus(Long subCateId) {
        String mess = "Cập Nhập Thất Bại";
        SubCategory subCategory = subCategoryRepo.findById(subCateId).get();
        if (subCategory.getStatus().equalsIgnoreCase("ACTIVE")){
            subCategory.setStatus("DEACTIVE");
            subCategoryRepo.save(subCategory);
            mess = "Cập Nhập Thành Công";
            return mess;
        }
        if ((subCategory.getStatus().equalsIgnoreCase("DEACTIVE"))){
            subCategory.setStatus("ACTIVE");
            subCategoryRepo.save(subCategory);
            mess = "Cập Nhập Thành Công";
            return mess;
        }
        return mess;
    }

    @Override
    public String updateSubCate(Long subId, String name) {
        String mess = "Cập Nhập Thất Bại";
        SubCategory subCategory = subCategoryRepo.findById(subId).get();
        if(subCategory==null){
            throw new ResponseStatusException(HttpStatus.valueOf(404),"Khong Tim Thay The Loai Nay !");
        }
        else {
            subCategory.setName(name);
            subCategoryRepo.save(subCategory);
            mess = "Cập Nhập Thành Công";
        }
        return mess;
    }

    @Override
    public String deleteSubCate(Long subId) {
        String mess = "Cập Nhập Thất Bại";
        SubCategory subCategory = subCategoryRepo.findById(subId).get();
        if(subCategory==null){
            throw new ResponseStatusException(HttpStatus.valueOf(404),"Khong Tim Thay The Loai Nay !");
        }else {
            List<Item> itemList = itemRepo.findAllBySubCategory_Id(subId);
            if (itemList.isEmpty()){
                subCategoryRepo.delete(subCategory);
                mess = "Cập Nhập Thành Công";
            }
        }
        return mess;
    }
}
