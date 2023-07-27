package hommie.hommie.controller;

import hommie.hommie.dto.responseDTO.CategoryResponseDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("create-category")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> createCategory(@RequestParam @Validated String name){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(categoryService.createCategory(name));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-category")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllCategory(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<CategoryResponseDTO> categories = categoryService.getAllCategory();
        responseDTO.setData(categories);
        responseDTO.setResult(categories.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("update-category")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateCategory(@RequestParam @Validated Long id,
                                                      @RequestParam @Validated String name){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(categoryService.updateCategory(id,name));
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("delete-category")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> deleteCate(@RequestParam @Validated Long cateId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(categoryService.deleteCategory(cateId));
        return ResponseEntity.ok().body(responseDTO);
    }
}
