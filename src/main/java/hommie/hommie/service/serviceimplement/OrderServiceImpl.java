package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.responseDTO.*;
import hommie.hommie.dto.responseDTO.momoDTO.MomoResponse;
import hommie.hommie.entity.*;
import hommie.hommie.repository.*;
import hommie.hommie.service.serviceinterface.CartService;
import hommie.hommie.service.serviceinterface.NotificationService;
import hommie.hommie.service.serviceinterface.OrderService;
import hommie.hommie.service.serviceinterface.PaymentService;
import hommie.hommie.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PromotionRepo promotionRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemDetailRepo itemDetailRepo;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ItemImageRepo itemImageRepo;

    @Autowired
    CartService cartService;

    @Autowired
    NotificationService notificationService;
    @Override
    public ResponseEntity<MomoResponse> createLinkOrder(BigDecimal feeShip, BigDecimal totalPrice, String paymentType, String shipAddress, Long userId, String phoneNumber, String promoCode,String userReceive) {
        ResponseEntity<MomoResponse> response = null;
        String orderCode = Utilities.randomAlphaNumeric(10);
        response = paymentService.getPaymentMomoV1(orderCode,feeShip,userId,totalPrice,paymentType,shipAddress,phoneNumber,promoCode,userReceive);
        return response;
    }

    @Override
    public OrderResponseDTO createOrder(Long userId, BigDecimal feeShip, String paymentType,String shipAddress, String phoneNumber, String promoCode, String userReceive) {
        OrderResponseDTO orderResponseDTO = null;
        String orderCode = Utilities.randomAlphaNumeric(10);
        BigDecimal totalPrice = BigDecimal.valueOf(0) ;
        BigDecimal discountPrice = BigDecimal.valueOf(0);
        List<CartItem> cartItemList = cartItemRepo.findAllByUser_Id(userId);
        for (CartItem cartItem: cartItemList) {
            CartItem _cartItem = cartItemRepo.findById(cartItem.getId()).get();
            ItemDetail detail = itemDetailRepo.findById(_cartItem.getItemDetail().getId()).get();
            totalPrice = totalPrice.add(detail.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
//            if (detail.getQuantity() >= cartItem.getQuantity()){
//                check = true;
//                detail.setQuantity(detail.getQuantity()-cartItem.getQuantity());
//                itemDetailRepo.save(detail);
//            }else {
//                throw new ResponseStatusException(HttpStatus.valueOf(400),"Hết Hàng Hoặc Không Đủ Số Lượng Yêu Cầu");
//            }
        }
//        if (check == true){
            User user = userRepo.findById(userId).get();
            Promotion promotion = promotionRepo.findByCode(promoCode);
            if (promotion!=null && promotion.getStatus().equalsIgnoreCase("DEACTIVE")){
                throw  new ResponseStatusException(HttpStatus.valueOf(400),"Voucher Đã Hết Hạn");
            }
            if (promotion != null && promotion.getStatus().equalsIgnoreCase("ACTIVE")){
                if (promotion.getType().equalsIgnoreCase("Mua Hàng")){
                    discountPrice = totalPrice.multiply(BigDecimal.valueOf(promotion.getValue()));
                    if (discountPrice.compareTo(BigDecimal.valueOf(promotion.getMaxValueDiscount()))>0){
                        totalPrice = totalPrice.subtract(BigDecimal.valueOf(promotion.getMaxValueDiscount())).add(feeShip);
                    }else {
                        totalPrice = totalPrice.subtract(discountPrice).add(feeShip);
                    }

                }else if(promotion.getType().equalsIgnoreCase("Vận Chuyển")){
                    totalPrice = totalPrice.add(feeShip).subtract(BigDecimal.valueOf(promotion.getValue()));
                }
                Order order = Order.builder()
                        .orderCode(orderCode)
                        .orderDate(LocalDate.now())
                        .phoneNumber(phoneNumber)
                        .shipAddress(shipAddress)
                        .status("Chờ Xử Lý")
                        .totalPrice(totalPrice)
                        .promotion(promotion)
                        .user(user)
                        .userReceive(userReceive)
                        .paymentType(paymentType)
                        .build();
                orderRepo.save(order);

                for (CartItem cartItem: cartItemList) {
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .itemDetail(cartItem.getItemDetail())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getItemDetail().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .build();
                    orderDetailRepo.save(orderDetail);
                }
                BigDecimal dp = BigDecimal.valueOf(0);
                if (promotion.getType().equalsIgnoreCase("Mua Hàng")){
                    if (discountPrice.compareTo(BigDecimal.valueOf(promotion.getMaxValueDiscount()))>0){
                        dp = BigDecimal.valueOf(promotion.getMaxValueDiscount());
                    }else {
                        dp = discountPrice;
                    }
                }else if(promotion.getType().equalsIgnoreCase("Vận Chuyển")){
                    dp = BigDecimal.valueOf(promotion.getValue());
                }

                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .orderDate(order.getOrderDate())
                        .phoneNumber(order.getPhoneNumber())
                        .shipAddress(order.getShipAddress())
                        .totalPrice(order.getTotalPrice())
                        .discountPrice(dp)
                        .feeShip(feeShip)
                        .userReceive(userReceive)
                        .paymentType(order.getPaymentType())
                        .build();
            }
            if (promotion == null){
                totalPrice = totalPrice.add(feeShip);
                Order order = Order.builder()
                        .orderCode(orderCode)
                        .orderDate(LocalDate.now())
                        .phoneNumber(phoneNumber)
                        .shipAddress(shipAddress)
                        .status("Chờ Xử Lý")
                        .totalPrice(totalPrice)
                        .user(user)
                        .paymentType(paymentType)
                        .build();
                orderRepo.save(order);

                for (CartItem cartItem: cartItemList) {
                    CartItem _cartItem = cartItemRepo.findById(cartItem.getId()).get();
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .itemDetail(_cartItem.getItemDetail())
                            .quantity(_cartItem.getQuantity())
                            .price(_cartItem.getItemDetail().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .build();
                    orderDetailRepo.save(orderDetail);
                }
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .orderDate(order.getOrderDate())
                        .phoneNumber(order.getPhoneNumber())
                        .shipAddress(order.getShipAddress())
                        .totalPrice(order.getTotalPrice())
                        .discountPrice(BigDecimal.valueOf(0))
                        .userReceive(userReceive)
                        .feeShip(feeShip)
                        .paymentType(order.getPaymentType())
                        .build();
            }
            List<CartItem> cartItems = cartItemRepo.findAllByUser_Id(userId);
            for (CartItem cartItem: cartItems) {
                cartItemRepo.delete(cartItem);
            }
        return orderResponseDTO;
    }

    @Override
    public List<OrderDetailResponseDTO> getAllMyOrder(Long userId) {
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
        List<Order> orderList = orderRepo.findAllByUser_Id(userId);
        for (Order order: orderList) {
            List<ItemDetailOrderResponse> itemDetailOrderResponses = new ArrayList<>();
            List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                ItemDetail itemDetail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
                Item item = itemRepo.findById(itemDetail.getItem().getId()).get();
                ItemDetailOrderResponse itemDetailOrderResponse = ItemDetailOrderResponse.builder()
                        .itemId(item.getId())
                        .itemName(item.getName())
                        .itemImage(itemImageRepo.findFirstByItemDetail_Id(itemDetail.getId()).getImage())
                        .orderQuantity(orderDetail.getQuantity())
                        .material(item.getMaterial())
                        .size(itemDetail.getSize())
                        .color(itemDetail.getColor())
                        .price(itemDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                        .build();
                itemDetailOrderResponses.add(itemDetailOrderResponse);
            }

            OrderDetailResponseDTO orderDetailResponseDTO = OrderDetailResponseDTO.builder()
                    .orderId(order.getId())
                    .orderCode(order.getOrderCode())
                    .orderDate(order.getOrderDate())
                    .listItems(itemDetailOrderResponses)
                    .totalPrice(order.getTotalPrice())
                    .orderStatus(order.getStatus())
                    .phoneNumber(order.getPhoneNumber())
                    .address(order.getShipAddress())
                    .userReceive(order.getUserReceive())
                    .build();
            orderDetailResponseDTOS.add(orderDetailResponseDTO);
        }
        return orderDetailResponseDTOS;
    }

    @Override
    public List<OrderDetailResponseDTO> getAllOrder() {
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
        List<Order> orderList = orderRepo.findAll();
        for (Order order: orderList) {
            List<ItemDetailOrderResponse> itemDetailOrderResponses = new ArrayList<>();
            List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                ItemDetail itemDetail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
                Item item = itemRepo.findById(itemDetail.getItem().getId()).get();
                ItemDetailOrderResponse itemDetailOrderResponse = ItemDetailOrderResponse.builder()
                        .itemId(item.getId())
                        .itemName(item.getName())
                        .itemImage(itemImageRepo.findFirstByItemDetail_Id(item.getId()).getImage())
                        .orderQuantity(orderDetail.getQuantity())
                        .material(item.getMaterial())
                        .size(itemDetail.getSize())
                        .color(itemDetail.getColor())
                        .price(itemDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                        .build();
                itemDetailOrderResponses.add(itemDetailOrderResponse);
            }
            OrderDetailResponseDTO orderDetailResponseDTO = OrderDetailResponseDTO.builder()
                    .orderId(order.getId())
                    .orderDate(order.getOrderDate())
                    .orderCode(order.getOrderCode())
                    .listItems(itemDetailOrderResponses)
                    .totalPrice(order.getTotalPrice())
                    .orderStatus(order.getStatus())
                    .build();
            orderDetailResponseDTOS.add(orderDetailResponseDTO);
        }
        return orderDetailResponseDTOS;
    }

    @Override
    public List<OrderDetailResponseDTO> getAllOrderByStatus(String status) {
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
        List<Order> orderList = orderRepo.findAllByStatus(status);
        for (Order order: orderList) {
            List<ItemDetailOrderResponse> itemDetailOrderResponses = new ArrayList<>();
            List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                ItemDetail itemDetail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
                Item item = itemRepo.findById(itemDetail.getItem().getId()).get();
                ItemDetailOrderResponse itemDetailOrderResponse = ItemDetailOrderResponse.builder()
                        .itemId(item.getId())
                        .itemName(item.getName())
                        .itemImage(itemImageRepo.findFirstByItemDetail_Id(itemDetail.getId()).getImage())
                        .orderQuantity(orderDetail.getQuantity())
                        .material(item.getMaterial())
                        .size(itemDetail.getSize())
                        .color(itemDetail.getColor())
                        .price(itemDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                        .build();
                itemDetailOrderResponses.add(itemDetailOrderResponse);
            }
            OrderDetailResponseDTO orderDetailResponseDTO = OrderDetailResponseDTO.builder()
                    .orderId(order.getId())
                    .orderDate(order.getOrderDate())
                    .orderCode(order.getOrderCode())
                    .listItems(itemDetailOrderResponses)
                    .totalPrice(order.getTotalPrice())
                    .orderStatus(order.getStatus())
                    .build();
            orderDetailResponseDTOS.add(orderDetailResponseDTO);
        }
        return orderDetailResponseDTOS;
    }

    @Override
    public String convertOrderStatus(Long orderId) {
        String mess = "Cập Nhập Thất Bại";
        Order order = orderRepo.findById(orderId).get();
        User user = userRepo.findById(order.getUser().getId()).get();
        if (order.getStatus().equalsIgnoreCase("Chờ Xử Lý")){
            order.setStatus("Đang Đóng Gói");
            orderRepo.save(order);
            mess = "Cập Nhập Thành Công";
            notificationService.sendNotification(user.getId(), "Xác Nhận", "Đơn Hàng Của Bạn Đang Được Xử Lý");

        }else if (order.getStatus().equalsIgnoreCase("Đang Đóng Gói")){
            order.setStatus("Đang Vận Chuyển");
            orderRepo.save(order);
            mess = "Cập Nhập Thành Công";
            notificationService.sendNotification(user.getId(), "Xác Nhận", "Đơn Hàng Của Bạn Đang Được Vận Chuyển");
        }else if (order.getStatus().equalsIgnoreCase("Đang Vận Chuyển")){
            order.setStatus("Hoàn Thành");
            orderRepo.save(order);
            mess = "Cập Nhập Thành Công";
            notificationService.sendNotification(user.getId(), "Xác Nhận", "Đơn Hàng Của Bạn Đã Hoàn Thành");
        }
        return mess;
    }

    @Override
    public String cancelOrder(Long orderId, String cancelReason) {
        String mess = "Hủy Đơn Hàng Thất Bại";
        Order order = orderRepo.findById(orderId).get();
        if (order.getStatus().equalsIgnoreCase("Chờ Xử Lý")){
            order.setStatus("Hủy Đơn Hàng");
            order.setCancelReason(cancelReason);
            orderRepo.save(order);
            mess = "Hủy Đơn Hàng Thành Công";
            List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(orderId);
            for (OrderDetail orderDetail: orderDetails) {
                ItemDetail itemDetail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
                itemDetail.setQuantity(itemDetail.getQuantity()+orderDetail.getQuantity());
                itemDetailRepo.save(itemDetail);
            }
        }
        return mess;
    }

    @Override
    public List<RevenueResponseDTO> revenueCalculation(String month, String year) {
        Map<Integer, BigDecimal> map = new HashMap<>();
        List<RevenueResponseDTO> revenueResponseDTOS = new ArrayList<>();
        List<Order> orderList = null;
        if (!month.equalsIgnoreCase("null") && !year.equalsIgnoreCase("null")){
            LocalDate date = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),1);
            Integer listDate = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            for (int i =1; i<=listDate; i++){
                map.put(i, BigDecimal.valueOf(0));
            }
            orderList = orderRepo.findAllByOrderDate(Integer.parseInt(year),Integer.parseInt(month));
            for (Order order: orderList) {
                if (map.containsKey(order.getOrderDate().getDayOfMonth())){
                    map.put(order.getOrderDate().getDayOfMonth(),map.get(order.getOrderDate().getDayOfMonth()).add(order.getTotalPrice()));
                }
            }
            Set set =map.keySet();
            for (Object key: set) {
                RevenueResponseDTO revenueResponseDTO = RevenueResponseDTO.builder()
                        .day(Integer.parseInt(key + ""))
                        .amount(map.get(key))
                        .build();
                revenueResponseDTOS.add(revenueResponseDTO);
            }
        }
        if (month.equalsIgnoreCase("null") && !year.equalsIgnoreCase("null")){
            for (int i = 1; i <= 12; i++) {
                map.put(i, BigDecimal.valueOf(0));
            }
            orderList = orderRepo.findAllByOrderDate(Integer.parseInt(year));
            for (Order order: orderList) {
                if (map.containsKey(order.getOrderDate().getMonthValue())){
                    map.put(order.getOrderDate().getMonthValue(),map.get(order.getOrderDate().getMonthValue()).add(order.getTotalPrice()));
                }
            }
            Set set =map.keySet();
            for (Object key: set) {
                RevenueResponseDTO revenueResponseDTO = RevenueResponseDTO.builder()
                        .day(Integer.parseInt(key + ""))
                        .amount(map.get(key))
                        .build();
                revenueResponseDTOS.add(revenueResponseDTO);
            }
        }
        return revenueResponseDTOS;
    }

    @Override
    public List<OrderDetailResponseDTO> searchOrderByOrderCode(String orderCode) {
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
        Order order = orderRepo.findByOrderCode(orderCode);
        List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(order.getId());
        List<ItemDetailOrderResponse> itemDetailOrderResponses = new ArrayList<>();
        for (OrderDetail orderDetail: orderDetails) {
            ItemDetail itemDetail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
            Item item = itemRepo.findById(itemDetail.getItem().getId()).get();
            ItemDetailOrderResponse itemDetailOrderResponse = ItemDetailOrderResponse.builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .itemImage(itemImageRepo.findFirstByItemDetail_Id(itemDetail.getId()).getImage())
                    .orderQuantity(orderDetail.getQuantity())
                    .material(item.getMaterial())
                    .size(itemDetail.getSize())
                    .color(itemDetail.getColor())
                    .price(itemDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                    .build();
            itemDetailOrderResponses.add(itemDetailOrderResponse);
        }
        OrderDetailResponseDTO orderDetailResponseDTO = OrderDetailResponseDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderCode(order.getOrderCode())
                .listItems(itemDetailOrderResponses)
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .build();
        orderDetailResponseDTOS.add(orderDetailResponseDTO);
        return orderDetailResponseDTOS;
    }

    @Override
    public List<ItemInOrderResponseDTO> getAllItemInOrder(String orderCode) {
        List<ItemInOrderResponseDTO> list = new ArrayList<>();
        Order order = orderRepo.findByOrderCode(orderCode);
        List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrder_Id(order.getId());
        for (OrderDetail orderDetail: orderDetails) {
            Item item = itemRepo.findById(orderDetail.getItemDetail().getItem().getId()).get();
            ItemImage image = itemImageRepo.findFirstByItemDetail_Id(orderDetail.getItemDetail().getId());
            ItemDetail detail = itemDetailRepo.findById(orderDetail.getItemDetail().getId()).get();
            ItemInOrderResponseDTO itemInOrderResponseDTO =ItemInOrderResponseDTO.builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .itemImage(image.getImage())
                    .material(item.getMaterial())
                    .size(detail.getSize())
                    .color(detail.getColor())
                    .orderQuantity(orderDetail.getQuantity())
                    .price(orderDetail.getPrice())
                    .build();
            list.add(itemInOrderResponseDTO);
        }
        return list;
    }
}
