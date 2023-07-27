package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.CartRequestDTO;
import hommie.hommie.dto.responseDTO.CartResponseDTO;
import hommie.hommie.entity.*;
import hommie.hommie.repository.CartItemRepo;
import hommie.hommie.repository.ItemDetailRepo;
import hommie.hommie.repository.ItemImageRepo;
import hommie.hommie.repository.UserRepo;
import hommie.hommie.service.serviceinterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ItemDetailRepo itemDetailRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    ItemImageRepo itemImageRepo;

    @Override
    public CartResponseDTO addToCart(CartRequestDTO cartRequestDTO) {
        Boolean exist = false;
        CartResponseDTO cartResponseDTO = null;
        ItemDetail itemDetail = itemDetailRepo.findById(cartRequestDTO.getItemDetailId()).get();
        User user = userRepo.findById(cartRequestDTO.getUserId()).get();
        List<CartItem> cartItems = cartItemRepo.findAllByUser_Id(user.getId());
        for (CartItem cartItem: cartItems) {
            if (cartItem.getItemDetail().getId().equals(cartRequestDTO.getItemDetailId())){
                cartItem.setQuantity(cartItem.getQuantity() + cartRequestDTO.getQuantity());
                cartItemRepo.save(cartItem);
                exist = true;
            }
        }
        if (exist == false){
            CartItem cartItem = CartItem.builder()
                    .itemDetail(itemDetail)
                    .quantity(cartRequestDTO.getQuantity())
                    .user(user)
                    .build();
            cartItemRepo.save(cartItem);
            ItemImage image = itemImageRepo.findFirstByItemDetail_Id(itemDetail.getId());
            cartResponseDTO = CartResponseDTO.builder()
                    .itemDetailId(itemDetail.getId())
                    .itemName(itemDetail.getItem().getName())
                    .material(itemDetail.getItem().getMaterial())
                    .itemImage(image.getImage())
                    .size(itemDetail.getSize())
                    .color(itemDetail.getColor())
                    .quantity(cartItem.getQuantity())
                    .price(itemDetail.getPrice())
                    .build();
        }
        return cartResponseDTO;
    }

    @Override
    public List<CartResponseDTO> viewMyCart(Long userId) {
        List<CartResponseDTO> cartResponseDTOS = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepo.findAllByUser_Id(userId);
        for (CartItem cartItem: cartItems) {
            ItemDetail detail = itemDetailRepo.findById(cartItem.getItemDetail().getId()).get();
            ItemImage image = itemImageRepo.findFirstByItemDetail_Id(detail.getId());
            CartResponseDTO cartResponseDTO = CartResponseDTO.builder()
                    .cartItemId(cartItem.getId())
                    .itemDetailId(detail.getId())
                    .itemName(detail.getItem().getName())
                    .itemImage(image.getImage())
                    .material(detail.getItem().getMaterial())
                    .size(detail.getSize())
                    .color(detail.getColor())
                    .quantity(cartItem.getQuantity())
                    .price(detail.getPrice())
                    .build();
            cartResponseDTOS.add(cartResponseDTO);
        }
        return cartResponseDTOS;
    }

    @Override
    public String updateQuantity(Long cartItemId, Integer quantity) {
        String mess = "Cập Nhập Thất Bại";
        CartItem cartItem = cartItemRepo.findById(cartItemId).get();
        if (cartItem!=null){
            if (quantity > 0){
                cartItem.setQuantity(quantity);
                cartItemRepo.save(cartItem);
            }else {
                cartItemRepo.delete(cartItem);
            }
            mess = "Cập Nhập Thành Công";
        }
        return mess;
    }

    @Override
    public String deleteCartItem(Long cartItemId, Long userId) {
        String mess = "Xóa Sản Phẩm Thất Bại";
        if (cartItemId != null){
            CartItem cartItem = cartItemRepo.findById(cartItemId).get();
            cartItemRepo.delete(cartItem);
            mess = "Xóa Sản Phẩm Thành Công";
        }else {
            List<CartItem> cartItems = cartItemRepo.findAllByUser_Id(userId);
            for (CartItem cartItem: cartItems) {
                cartItemRepo.delete(cartItem);
            }
            mess = "Xóa Sản Phẩm Thành Công";
        }
        return mess;
    }
}
