package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.ItemDetailRequestDTO;
import hommie.hommie.dto.requestDTO.ItemImageRequestDTO;
import hommie.hommie.dto.requestDTO.UpdateItemDetailRequestDTO;
import hommie.hommie.dto.responseDTO.DetailInfoResponseDTO;
import hommie.hommie.dto.responseDTO.DetailsResponse;
import hommie.hommie.dto.responseDTO.ItemDetailResponseDTO;
import hommie.hommie.dto.responseDTO.ItemImageResponseDTO;
import hommie.hommie.entity.Item;
import hommie.hommie.entity.ItemDetail;
import hommie.hommie.entity.ItemImage;
import hommie.hommie.repository.ItemDetailRepo;
import hommie.hommie.repository.ItemImageRepo;
import hommie.hommie.repository.ItemRepo;
import hommie.hommie.service.serviceinterface.ItemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemDetailServiceImpl implements ItemDetailService {
    @Autowired
    ItemDetailRepo itemDetailRepo;

    @Autowired
    ItemImageRepo itemImageRepo;

    @Autowired
    ItemRepo itemRepo;

    @Override
    public ItemDetailResponseDTO createItemDetail(Long itemId, ItemDetailRequestDTO itemDetailRequestDTO) {
        Item item = itemRepo.findById(itemId).get();
        ItemDetail detail = ItemDetail.builder()
                .item(item)
                .color(itemDetailRequestDTO.getColor())
                .size(itemDetailRequestDTO.getSize())
                .price(itemDetailRequestDTO.getPrice())
                .quantity(itemDetailRequestDTO.getQuantity())
                .description(itemDetailRequestDTO.getDescription())
                .status("ACTIVE")
                .build();
        itemDetailRepo.save(detail);
        List<String> listI = itemDetailRequestDTO.getImageLists();
        for (String im: listI) {
            if (im.equals(null)){
                String i = "https://taiminh.edu.vn/phan-mem-dowload-mien-phi/imager_1029.jpg";
                ItemImage image = ItemImage.builder()
                        .image(i)
                        .itemDetail(detail)
                        .build();
                itemImageRepo.save(image);
            }else {
                ItemImage image = ItemImage.builder()
                        .image(im)
                        .itemDetail(detail)
                        .build();
                itemImageRepo.save(image);
            }
        }
        List<ItemImageResponseDTO> list = new ArrayList<>();
        List<ItemImage> imageList = itemImageRepo.findAllByItemDetail_Id(detail.getId());
        for (ItemImage itemImage: imageList) {
            ItemImageResponseDTO imageResponseDTO = ItemImageResponseDTO.builder()
                    .id(itemImage.getId())
                    .image(itemImage.getImage())
                    .build();
            list.add(imageResponseDTO);
        }
        ItemDetailResponseDTO itemDetailResponseDTO = ItemDetailResponseDTO.builder()
                .id(detail.getId())
                .color(detail.getColor())
                .size(detail.getSize())
                .price(detail.getPrice())
                .quantity(detail.getQuantity())
                .description(detail.getDescription())
                .itemImages(list)
                .build();
        return itemDetailResponseDTO;
    }

    @Override
    public List<DetailsResponse> getAllDetail(Long itemId) {
        List<DetailsResponse> list = new ArrayList<>();
        List<ItemDetail> itemDetails = itemDetailRepo.findAllByItem_Id(itemId);
        for (ItemDetail itemDetail: itemDetails) {
           List<DetailInfoResponseDTO> dtos = new ArrayList<>();
           DetailInfoResponseDTO detailInfoResponseDTO = DetailInfoResponseDTO.builder()
                   .color(itemDetail.getColor())
                   .quantity(itemDetail.getQuantity())
                   .build();
           dtos.add(detailInfoResponseDTO);
           List<ItemImageResponseDTO> iResult = new ArrayList<>();
           List<ItemImage> itemImages = itemImageRepo.findAllByItemDetail_Id(itemDetail.getId());
            for (ItemImage i: itemImages) {
                ItemImageResponseDTO imageResponseDTO = ItemImageResponseDTO.builder()
                        .id(i.getId())
                        .image(i.getImage())
                        .build();
                iResult.add(imageResponseDTO);
            }
           DetailsResponse itemDetailResponseDTO = DetailsResponse.builder()
                   .id(itemDetail.getId())
                   .size(itemDetail.getSize())
                   .color(itemDetail.getColor())
                   .quantity(itemDetail.getQuantity())
                   .price(itemDetail.getPrice())
                   .description(itemDetail.getDescription())
                   .listImage(iResult)
                   .build();
            list.add(itemDetailResponseDTO);
        }
        return list;
    }

    @Override
    public String updateItemDetail(UpdateItemDetailRequestDTO updateItemRequestDTO) {
        String mess = "Cập Nhập Thất Bại";
        ItemDetail detail = itemDetailRepo.findById(updateItemRequestDTO.getItemDetailId()).get();
        detail.setPrice(updateItemRequestDTO.getPrice());
        detail.setQuantity(updateItemRequestDTO.getQuantity());
        itemDetailRepo.save(detail);
        List<ItemImage> imageList = itemImageRepo.findAllByItemDetail_Id(detail.getId());
        List<ItemImageResponseDTO> dtos = updateItemRequestDTO.getListImage();
        for (ItemImage itemImage: imageList) {
            for (ItemImageResponseDTO imageResponseDTO: dtos) {
                if ((itemImage.getId()==imageResponseDTO.getId())){
                    ItemImage image = itemImageRepo.findById(itemImage.getId()).get();
                    image.setImage(imageResponseDTO.getImage());
                    itemImageRepo.save(image);
                }
            }
        }
        mess="Cập Nhập Thành Công";
        return mess;
    }

    @Override
    public String updateItemDetailStatus(Long itemDetailId) {
        String mess = "Cập Nhập Thất Bại";
        ItemDetail detail = itemDetailRepo.findById(itemDetailId).get();
        if (detail!=null){
            if (detail.getStatus().equals("DEACTIVE")){
                detail.setStatus("ACTIVE");
                itemDetailRepo.save(detail);
            }else if (detail.getStatus().equals("ACTIVE")){
                detail.setStatus("DEACTIVE");
                itemDetailRepo.save(detail);
            }
            mess = "Cập Nhập Thành Công ";
        }
        return mess;
    }
}
