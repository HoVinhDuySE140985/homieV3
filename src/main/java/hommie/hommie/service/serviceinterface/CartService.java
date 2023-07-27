package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.CartRequestDTO;
import hommie.hommie.dto.responseDTO.CartResponseDTO;

import java.util.List;

public interface CartService {
    CartResponseDTO addToCart(CartRequestDTO cartRequestDTO);

    List<CartResponseDTO> viewMyCart(Long userId);

    String updateQuantity(Long cartItemId, Integer quantity);

    String deleteCartItem(Long cartItemId, Long userId);
}
