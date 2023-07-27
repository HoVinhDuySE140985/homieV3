package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.CreateSubCateRequestDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.dto.responseDTO.SubCategoryResponseDTO;
import hommie.hommie.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @PostMapping("create-subcategory")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> createSubcategory(@RequestBody CreateSubCateRequestDTO createSubCateRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(subCategoryService.createSubCategory(createSubCateRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-sub_cate-by")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllSubCateBy(@RequestParam @Validated Long cateId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<SubCategoryResponseDTO> listSubs = subCategoryService.getAllSubBy(cateId);
        responseDTO.setData(listSubs);
        responseDTO.setResult(listSubs.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-sub-cate-status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateSubCategoryStatus(@RequestParam @Validated Long subCateId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(subCategoryService.updateSubCateStatus(subCateId));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-sub_cate")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllSubCate(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<SubCategoryResponseDTO> listSubs = subCategoryService.getAllSub();
        responseDTO.setData(listSubs);
        responseDTO.setResult(listSubs.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-sub-cate")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateSubCateGory(@RequestParam @Validated Long subId,
                                                         @RequestParam @Validated String name){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(subCategoryService.updateSubCate(subId,name));
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("delete-sub-cate")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> deleteSubCate(@RequestParam @Validated Long subId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(subCategoryService.deleteSubCate(subId));
        return ResponseEntity.ok().body(responseDTO);
    }
}
