package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.*;
import hommie.hommie.dto.responseDTO.CreateItemResponseDTO;
import hommie.hommie.dto.responseDTO.ItemDetailResponseDTO;
import hommie.hommie.dto.responseDTO.ItemImageResponseDTO;
import hommie.hommie.dto.responseDTO.ItemResponseDTO;
import hommie.hommie.entity.*;
import hommie.hommie.repository.*;
import hommie.hommie.service.serviceinterface.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ItemImageRepo itemImageRepo;

    @Autowired
    SubCategoryRepo subCategoryRepo;

    @Autowired
    ItemDetailRepo itemDetailRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Override
    public CreateItemResponseDTO createItem(CreateItemRequestDTO createItemRequestDTO) {  // sua
        CreateItemResponseDTO itemResponseDTO = null;
        SubCategory subCategory = subCategoryRepo.findById(createItemRequestDTO.getSubCateId()).get();
        Item item = Item.builder()
                .name(createItemRequestDTO.getName())
                .material(createItemRequestDTO.getMaterial())
                .createDate(LocalDate.now())
                .avatar(createItemRequestDTO.getAvatar())
                .status("Còn Hàng")
                .subCategory(subCategory)
                .build();
        itemRepo.save(item);
            itemResponseDTO = CreateItemResponseDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .material(item.getMaterial())
                    .subId(item.getSubCategory().getId())
                    .build();
        return itemResponseDTO;
    }

    @Override
    public String updateItem(UpdateItemRequestDTO updateItemRequestDTO) {
        String mess = "Cập Nhập Thất Bại";
        Item item = itemRepo.findById(updateItemRequestDTO.getId()).get();
        if (item != null) {
            item.setName(updateItemRequestDTO.getName());
            item.setMaterial(updateItemRequestDTO.getMaterial());
            itemRepo.save(item);
            mess = "Cập Nhập Thành Công";
        }
        return mess;
    }


    @Override
    public String updateStatus(Long itemId) {
        String mess = "Cập Nhập Thất Bại";
        Item item = itemRepo.findById(itemId).get();
        List<ItemDetail> details = itemDetailRepo.findAllByItem_Id(item.getId());
        if (details.size()==0){
            if (item.getStatus().equalsIgnoreCase("Còn Hàng")) {
                item.setStatus("Tạm Hết Hàng");
                itemRepo.save(item);
                mess = "Cập Nhập Thành Công";
            } else if (item.getStatus().equalsIgnoreCase("Tạm Hết Hàng")){
                item.setStatus("Ngừng Kinh Doanh");
                itemRepo.save(item);
                mess = "Cập Nhập Thành Công";
            }
        }else {
            throw new ResponseStatusException(HttpStatus.valueOf(200),"Sản Phẩm Còn Hàng Không Thể Cập Nhập");
        }
        return mess;
    }

    @Override
    public List<ItemResponseDTO> getAllItemBy(Long cateId, Long subId, String keyWord) {
//        Boolean check = false;
        int count = 0;
        List<ItemResponseDTO> listItems = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Integer> itemByCateId = new ArrayList<>();
        if (cateId == null  && subId == null && keyWord == null) {
            items = itemRepo.findAll();
        }
        if (cateId != null  && subId == null && keyWord == null){
            itemByCateId = itemRepo.findAllByCateId(cateId);
            for (Integer i: itemByCateId) {
                Item ite = itemRepo.findById(Integer.toUnsignedLong(i)).get();
                items.add(ite);
            }
        }
        if (cateId != null  && subId != null && keyWord == null){
            items = itemRepo.findAllBySubCategory_Id(subId);
        }
        if (cateId == null  && subId == null && keyWord != null){
            items = itemRepo.findAllByKeyWord(keyWord);
        }
        Map<Long, Integer> map = new HashMap<>();
        List<OrderDetail> orderDetails = orderDetailRepo.findAll();
        for (OrderDetail orderDetail: orderDetails) {
            if (map.containsKey(orderDetail.getItemDetail().getId())){
                map.put(orderDetail.getItemDetail().getId(),map.get(orderDetail.getItemDetail().getId())+1);
            }else{
                map.put(orderDetail.getItemDetail().getId(),1);
            }
        }
        for (int i=0;i<items.size();i++){
            List<ItemDetail> iDetails = itemDetailRepo.findAllByItem_Id(items.get(i).getId());
            if (iDetails.size()==0 ){
                items.remove(items.get(i));
            }
        }
        for (Item item : items) {
            List<ItemDetail> iDetails = itemDetailRepo.findAllByItem_Id(item.getId());
            for (ItemDetail itemDetail: iDetails) {
                Set<Long> set = map.keySet();
                for (Long  key :set) {
                    if (key==itemDetail.getId()){
                        count += map.get(key);
                    }
                }
            }
            SubCategory subCategory = subCategoryRepo.findById(item.getSubCategory().getId()).get();
            Category category = categoryRepo.findById(subCategory.getCategory().getId()).get();
            if (!item.getStatus().equalsIgnoreCase("Ngừng Kinh Doanh")) {
                List<ItemDetailResponseDTO> detailList = new ArrayList<>();
                List<ItemDetail> details = itemDetailRepo.findAllByItem_Id(item.getId());
//                if (details.isEmpty()){
//                    check = true;
//                }
                for (ItemDetail iteDetail: details) {
                    List<ItemImageResponseDTO> listI = new ArrayList<>();
                    List<ItemImage> imageList = itemImageRepo.findAllByItemDetail_Id(iteDetail.getId());
                    for (ItemImage i: imageList) {
                        ItemImageResponseDTO imageResponseDTO = ItemImageResponseDTO.builder()
                                .id(i.getId())
                                .image(i.getImage())
                                .build();
                        listI.add(imageResponseDTO);
                    }
                    ItemDetailResponseDTO itemDetailResponseDTO = ItemDetailResponseDTO.builder()
                            .id(iteDetail.getId())
                            .size(iteDetail.getSize())
                            .color(iteDetail.getColor())
                            .quantity(iteDetail.getQuantity())
                            .itemImages(listI)
                            .price(iteDetail.getPrice())
                            .description(iteDetail.getDescription())
                            .build();
                    detailList.add(itemDetailResponseDTO);
                }
                ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                        .id(item.getId())
                        .avatar(item.getAvatar())
                        .name(item.getName())
                        .material(item.getMaterial())
                        .createDate(item.getCreateDate())
                        .details(detailList)
                        .subId(subCategory.getId())
                        .subName(subCategory.getName())
                        .cateId(category.getId())
                        .cateName(category.getName())
                        .buyNumber(count)
                        .status(item.getStatus())
//                        .checkNumber(check)
                        .build();
                listItems.add(itemResponseDTO);
                count = 0;
            }
        }
        return listItems;
    }

    @Override
    public List<ItemResponseDTO> getTopItem() {
        List<ItemResponseDTO> list = new ArrayList<>();
        List<Item> itemList =  itemRepo.findAll();
        for (int i=0;i<itemList.size();i++){
            List<ItemDetail> iDetails = itemDetailRepo.findAllByItem_Id(itemList.get(i).getId());
            if (iDetails.size()==0 ){
                itemList.remove(itemList.get(i));
            }
        }
        for (Item item: itemList) {
            SubCategory subCategory = subCategoryRepo.findById(item.getSubCategory().getId()).get();
            Category category = categoryRepo.findById(subCategory.getCategory().getId()).get();
            if (!item.getStatus().equalsIgnoreCase("Ngừng Kinh Doanh")) {
                List<ItemDetailResponseDTO> dtos = new ArrayList<>();
                List<ItemDetail> details = itemDetailRepo.findAllByItem_Id(item.getId());
                for (ItemDetail itemDetail: details) {
                    List<ItemImageResponseDTO> listI = new ArrayList<>();
                    List<ItemImage> imageList = itemImageRepo.findAllByItemDetail_Id(itemDetail.getId());
                    for (ItemImage i: imageList) {
                        ItemImageResponseDTO imageResponseDTO = ItemImageResponseDTO.builder()
                                .id(i.getId())
                                .image(i.getImage())
                                .build();
                        listI.add(imageResponseDTO);
                    }
                    ItemDetailResponseDTO itemDetailResponseDTO = ItemDetailResponseDTO.builder()
                            .id(itemDetail.getId())
                            .size(itemDetail.getSize())
                            .color(itemDetail.getColor())
                            .quantity(itemDetail.getQuantity())
                            .itemImages(listI)
                            .price(itemDetail.getPrice())
                            .description(itemDetail.getDescription())
                            .build();
                    dtos.add(itemDetailResponseDTO);
                }
                ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .avatar(item.getAvatar())
                        .material(item.getMaterial())
                        .createDate(item.getCreateDate())
                        .details(dtos)
                        .subId(subCategory.getId())
                        .subName(subCategory.getName())
                        .cateId(category.getId())
                        .cateName(category.getName())
                        .status(item.getStatus())
                        .build();
                list.add(itemResponseDTO);
            }
        }
        return list;
    }

    @Override
    public List<ItemResponseDTO> getAllItem() {
        Boolean check = true;
        List<ItemResponseDTO> list = new ArrayList<>();
        List<Item> itemList =  itemRepo.findAll();
        for (Item item: itemList) {
                SubCategory subCategory = subCategoryRepo.findById(item.getSubCategory().getId()).get();
                Category category = categoryRepo.findById(subCategory.getCategory().getId()).get();
                List<ItemDetailResponseDTO> dtos = new ArrayList<>();
                List<ItemDetail> details = itemDetailRepo.findAllByItem_Id(item.getId());
                if (details.size()==0){
                    check = false;
                }
                for (ItemDetail itemDetail: details) {
                    List<ItemImageResponseDTO> listI = new ArrayList<>();
                    List<ItemImage> imageList = itemImageRepo.findAllByItemDetail_Id(itemDetail.getId());
                    for (ItemImage i: imageList) {
                        ItemImageResponseDTO imageResponseDTO = ItemImageResponseDTO.builder()
                                .id(i.getId())
                                .image(i.getImage())
                                .build();
                        listI.add(imageResponseDTO);
                    }
                    ItemDetailResponseDTO itemDetailResponseDTO = ItemDetailResponseDTO.builder()
                            .id(itemDetail.getId())
                            .size(itemDetail.getSize())
                            .color(itemDetail.getColor())
                            .quantity(itemDetail.getQuantity())
                            .itemImages(listI)
                            .price(itemDetail.getPrice())
                            .description(itemDetail.getDescription())
                            .build();
                    dtos.add(itemDetailResponseDTO);
                }
                ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .avatar(item.getAvatar())
                        .material(item.getMaterial())
                        .createDate(item.getCreateDate())
                        .details(dtos)
                        .subId(subCategory.getId())
                        .subName(subCategory.getName())
                        .cateId(category.getId())
                        .cateName(category.getName())
                        .status(item.getStatus())
                        .checkNumber(check)
                        .build();
                list.add(itemResponseDTO);
        }
        return list;
    }
}
