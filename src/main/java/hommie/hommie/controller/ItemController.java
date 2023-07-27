package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.CreateItemRequestDTO;
//import hommie.hommie.dto.requestDTO.UpdateItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.ItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateItemRequestDTO;
import hommie.hommie.dto.responseDTO.*;
import hommie.hommie.service.serviceinterface.ItemDetailService;
import hommie.hommie.service.serviceinterface.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemDetailService itemDetailService;

    @PostMapping("create-item")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> createItem(@RequestBody @Validated CreateItemRequestDTO createItemRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        CreateItemResponseDTO itemResponseDTO = itemService.createItem(createItemRequestDTO);
        responseDTO.setData(itemResponseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create-item-detail")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> createItemDetail(@RequestParam @Valid Long itemId,
                                                        @RequestBody @Validated ItemDetailRequestDTO itemDetailRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        ItemDetailResponseDTO itemResponseDTO = itemDetailService.createItemDetail(itemId,itemDetailRequestDTO);
        responseDTO.setData(itemResponseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-item")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateItem(@RequestBody @Validated UpdateItemRequestDTO updateItemRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(itemService.updateItem(updateItemRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-item-detail")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateItemDetail(@RequestBody @Validated UpdateItemDetailRequestDTO updateItemRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(itemDetailService.updateItemDetail(updateItemRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateStatus(@RequestParam @Validated Long itemId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(itemService.updateStatus(itemId));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-status-item-detail")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateItemDetailStatus(@RequestParam @Validated Long itemDetailId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(itemDetailService.updateItemDetailStatus(itemDetailId));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-item-by")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllItemBy(@RequestParam(required = false) @Validated Long cateId,
                                                    @RequestParam(required = false) @Validated Long subId,
                                                    @RequestParam(required = false) @Validated String keyWord){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ItemResponseDTO> itemResponseDTOS = itemService.getAllItemBy(cateId,subId, keyWord);
        responseDTO.setData(itemResponseDTOS);
        responseDTO.setResult(itemResponseDTOS.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-top-new-item") // chua chan a ko detail
    @PermitAll
    public ResponseEntity<ResponseDTO> getTopItem(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ItemResponseDTO> list = itemService.getTopItem();
        responseDTO.setData(list);
        responseDTO.setResult(list.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-details-by-itemId")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllItemDetailByItemId(@RequestParam @Validated Long itemId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<DetailsResponse> dto = itemDetailService.getAllDetail(itemId);
        responseDTO.setData(dto);
        responseDTO.setResult(dto.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping ("get-All-Item")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllItem(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ItemResponseDTO> list = itemService.getAllItem();
        responseDTO.setData(list);
        responseDTO.setResult(list.size());
        return ResponseEntity.ok().body(responseDTO);
    }
}
