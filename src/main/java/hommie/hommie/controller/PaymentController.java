package hommie.hommie.controller;

import hommie.hommie.dto.responseDTO.OrderRequestDTO;
import hommie.hommie.dto.responseDTO.momoDTO.MomoConfirmResultResponse;
import hommie.hommie.dto.responseDTO.momoDTO.MomoResponse;
import hommie.hommie.service.serviceinterface.OrderService;
import hommie.hommie.service.serviceinterface.PaymentService;
import hommie.hommie.shared.utils.Common;
import hommie.hommie.shared.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderService orderService;

    @PostMapping("/momo")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<MomoResponse> paymentWithMomo(@RequestParam String codeOrder,
                                                        @RequestParam BigDecimal totalPrice,
                                                        @RequestParam Long userId,
                                                        @RequestParam String paymentType,
                                                        @RequestParam String shipAddress,
                                                        @RequestParam BigDecimal feeShip,
                                                        @RequestParam String phoneNumber,
                                                        @RequestParam String promoCode,
                                                        @RequestParam String userReceive) {
        return paymentService.getPaymentMomoV1(codeOrder, feeShip, userId, totalPrice, paymentType, shipAddress,phoneNumber,promoCode,userReceive);
    }


    @GetMapping("/MomoConfirm/{userId}/{feeShip}/{paymentType}/{shipAddress}/{phoneNumber}/{promoCode}//{userReceive}")
    @PermitAll
    public ResponseEntity<MomoConfirmResultResponse> momoConfirm(
            @PathVariable Long userId,
            @PathVariable BigDecimal feeShip,
            @PathVariable String paymentType,
            @PathVariable String shipAddress,
            @PathVariable String phoneNumber,
            @PathVariable String promoCode,
            @PathVariable String userReceive,
            @RequestParam("partnerCode") String partnerCode,
            @RequestParam("orderId") String orderId,
            @RequestParam("requestId") String requestId,
            @RequestParam("amount") long amount,
            @RequestParam("orderInfo") String orderInfo,
            @RequestParam("orderType") String orderType,
            @RequestParam("transId") long transId,
            @RequestParam("resultCode") int resultCode,
            @RequestParam("message") String message,
            @RequestParam("payType") String payType,
            @RequestParam("responseTime") String responseTime,
            @RequestParam("extraData") String extraData,
            @RequestParam("signature") String signature) {
        String redirectUrl = "https://www.youtube.com/"; // success
        String sign = "accessKey=" +
                Common.ACCESS_KEY + "&orderId=" + orderId + "&partnerCode=" + Common.PARTNER_CODE
                + "&requestId=" + requestId;
        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
            logger.error("Signiture HMAC loi!");
        }
        try {
            MomoConfirmResultResponse momoConfirmResultResponse = new MomoConfirmResultResponse(
                    partnerCode, orderInfo, responseTime, amount, orderInfo, orderType, transId,
                    resultCode, message, payType, resultCode, extraData, signatureHmac, Common.PARTNER_CODE);
            String msg = "";
            if (momoConfirmResultResponse.getResultCode() == 0) {
                msg = "giao dich thanh cong";
            } else if (momoConfirmResultResponse.getResultCode() == 9000) {
                msg = "giao dich duoc xac nhan, giao dich thang cong!";
                System.out.println(paymentType);
                if (paymentType.equalsIgnoreCase("Trả Trước")) {
                    orderService.createOrder(userId,feeShip,paymentType,shipAddress,phoneNumber,promoCode, userReceive);
                }
            } else if (momoConfirmResultResponse.getResultCode() == 1006) {
                msg = "người dùng từ chối giao dịch!";
                redirectUrl = "https://www.youtube.com/"; // fail
            }

            https://test-payment.momo.vn/v2/gateway/redirect?amount=30000&message=Giao%20d%E1%BB%8Bch%20%C4%91%C3%A3%20%C4%91%C6%B0%E1%BB%A3c%20x%C3%A1c%20nh%E1%BA%ADn%20th%C3%A0nh%20c%C3%B4ng.&orderId=Hommie6vQAtEh11j&partnerCode=MOMOM1IH20220922&requestType=captureWallet&resultCode=9000&sid=1V9ephvpNoAwKSGYmHFf68DD&subscriptionInfo=&subscriptionName=
            logger.info("" + msg);
            System.out.println(resultCode);
            System.out.println(msg);
            // accessKey=WehkypIRwPP14mHb&orderId=23&partnerCode=MOMODJMX20220717&requestId=48468005-6de1-4140-839f-5f2d8d77a001
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl)); // deploy lên thì chạy về trang cần trả về
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

}
