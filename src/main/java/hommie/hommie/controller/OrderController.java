package hommie.hommie.controller;

import hommie.hommie.dto.responseDTO.OrderRequestDTO;
import hommie.hommie.dto.responseDTO.OrderResponseDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.entity.CartItem;
import hommie.hommie.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("create-order-with-prepay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createOrderWithMomo(@RequestParam @Validated BigDecimal totalPrice,
                                                           @RequestParam @Validated String paymentType,
                                                           @RequestParam @Validated String shipAddress,
                                                           @RequestParam @Validated BigDecimal feeShip,
                                                           @RequestParam @Validated Long userId,
                                                           @RequestParam @Validated String phoneNumber,
                                                           @RequestParam @Validated String promoCode,
                                                           @RequestParam String userReceive){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.createLinkOrder(feeShip,totalPrice,paymentType,shipAddress,userId,phoneNumber,promoCode,userReceive));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create-order-with-postpaid")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createOrder(@RequestParam String userId,
                                                   @RequestParam BigDecimal feeShip,
                                                   @RequestParam String paymentType,
                                                   @RequestParam String shipAddress,
                                                   @RequestParam String phoneNumber,
                                                   @RequestParam String promoCode,
                                                   @RequestParam String userReceive){
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setData(orderService.createOrder(Long.parseLong(userId),feeShip,paymentType,shipAddress,phoneNumber,promoCode,userReceive));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-my-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllMyOrder(@RequestParam @Validated Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.getAllMyOrder(userId));
        responseDTO.setResult(orderService.getAllMyOrder(userId).size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-order")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllOrder(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.getAllOrder());
        responseDTO.setResult(orderService.getAllOrder().size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-user-info-in-order")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllUserInfoInOrder(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.getAllOrder());
        responseDTO.setResult(orderService.getAllOrder().size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-order-By-Status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllOrderByStatus(@RequestParam @Validated String status){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.getAllOrderByStatus(status));
        responseDTO.setResult(orderService.getAllOrderByStatus(status).size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("convert-order-status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> convertOrderStatus(@RequestParam @Validated Long orderId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.convertOrderStatus(orderId));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("cancel-order")
    @PreAuthorize("hasAnyRole('OWNER','CUSTOMER')")
    public ResponseEntity<ResponseDTO> cancelOrder(@RequestParam @Validated Long orderId,
                                                   @RequestParam @Validated String cancelReason){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.cancelOrder(orderId,cancelReason));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-revenue")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> revenueCalculation(@RequestParam String month,
                                                          @RequestParam String year){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.revenueCalculation(month,year));
        responseDTO.setResult(orderService.revenueCalculation(month,year).size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-order-by-order-code")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> searchOrder(@RequestParam String orderCode){
        ResponseDTO responseDTO  = new ResponseDTO();
        responseDTO.setData(orderService.searchOrderByOrderCode(orderCode));
//        responseDTO.setResult(orderService.searchOrderByOrderCode(orderCode).size());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-item-in-order")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllItemInOrder(@RequestParam String orderCode){
        ResponseDTO responseDTO  = new ResponseDTO();
        responseDTO.setData(orderService.getAllItemInOrder(orderCode));
        responseDTO.setResult(orderService.getAllItemInOrder(orderCode).size());
        return ResponseEntity.ok().body(responseDTO);
    }

}
