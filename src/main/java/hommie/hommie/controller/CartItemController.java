package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.CartRequestDTO;
import hommie.hommie.dto.responseDTO.CartResponseDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/cart-item")
public class CartItemController {
    @Autowired
    CartService cartService;

    @PostMapping("add-to-cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> addToCart(@RequestBody @Validated CartRequestDTO cartRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        CartResponseDTO cartResponseDTO = cartService.addToCart(cartRequestDTO);
        responseDTO.setData(cartResponseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("view-my-cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> viewMyCart(@RequestParam @Validated Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<CartResponseDTO> myCart = cartService.viewMyCart(userId);
        responseDTO.setData(myCart);
        responseDTO.setResult(myCart.size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-cart-item-quantity")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateCartQuantity(@RequestParam @Validated Long cartItemId,
                                                          @RequestParam @Validated Integer quantity){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cartService.updateQuantity(cartItemId, quantity));
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("delete-cart-item")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> deleteCartItem(@RequestParam(required = false) Long cartItemId,
                                                      @RequestParam(required = false) Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cartService.deleteCartItem(cartItemId,userId));
        return ResponseEntity.ok().body(responseDTO);
    }
}
