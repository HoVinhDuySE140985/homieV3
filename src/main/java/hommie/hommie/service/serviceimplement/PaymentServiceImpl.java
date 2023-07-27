package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.MomoDTO.MomoRequest;
import hommie.hommie.dto.responseDTO.momoDTO.MomoResponse;
import hommie.hommie.service.serviceinterface.PaymentService;
import hommie.hommie.shared.utils.Common;
import hommie.hommie.shared.utils.Utilities;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public ResponseEntity<MomoResponse> getPaymentMomoV1(String codeOrder, BigDecimal feeShip, Long userId, BigDecimal totalPrice, String paymentType, String shipAddress, String phoneNumber, String promoCode) {
        // request url
        String url = Common.MOMO_URI;
        String redirectUrl = Common.REDIRECT_URL_MOMO +  "/" +userId  + "/" +  feeShip + "/" + paymentType + "/"  + shipAddress+ "/"  +phoneNumber + "/"  + promoCode;
        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object

        // build the request
        MomoRequest momoReq = new MomoRequest();
        // CustomerInfoMomoRequest customerInfo = new CustomerInfoMomoRequest("dat",
        // "0123456789", "dat@gmail.com");
//        String orderId = String.join("-", oList); //12 //23-12-23-32
        // String requestId =

        // long amount = 200000;
        // byte[] array = new byte[10]; // length is bounded by 7
        // new Random().nextBytes(array);


        // String requestId = new String(array, Charset.forName("UTF-8"));
        // String requestId = String.valueOf(order.getId());

        DecimalFormat df = new DecimalFormat("#");

        String amount = String.valueOf(df.format(totalPrice));

        String sign = "accessKey=" + Common.ACCESS_KEY + "&amount=" + amount + "&extraData="
                + "&ipnUrl=" + Common.IPN_URL_MOMO + "&orderId=" + codeOrder + "&orderInfo="
                + "Thanh toan momo"
                + "&partnerCode=" + Common.PARTNER_CODE + "&redirectUrl=" + redirectUrl
                + "&requestId=" + codeOrder + "&requestType=captureWallet";

        // accessKey=$accessKey&amount=$amount&extraData=$extraData
        // &ipnUrl=$ipnUrl&orderId=$orderId&orderInfo=$orderInfo
        // &partnerCode=$partnerCode&redirectUrl=$redirectUrl
        // &requestId=$requestId&requestType=$requestType

        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        momoReq.setPartnerCode(Common.PARTNER_CODE);
        momoReq.setSignature(signatureHmac);
        momoReq.setAmount(totalPrice);
        momoReq.setExtraData("");
        momoReq.setIpnUrl(Common.IPN_URL_MOMO);
        momoReq.setLang("vi");
        momoReq.setOrderId(codeOrder);
        momoReq.setOrderInfo("Thanh toan momo");
        momoReq.setRedirectUrl(redirectUrl); //* */
        momoReq.setRequestId(codeOrder);
        momoReq.setRequestType("captureWallet");

        HttpEntity<MomoRequest> req = new HttpEntity<>(momoReq, headers);

        // send POST request
        try {
            ResponseEntity<MomoResponse> response = restTemplate.postForEntity(url, req, MomoResponse.class);

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            String arr[] = String.valueOf(e.getMessage()).split(",");
            String ar[] = arr[1].split(":");
            String message = ar[1].replaceAll("\"", "");
            System.out.println(" " + e.getMessage());
            // throw new AppException(HttpStatus.BAD_REQUEST.value(),
            //         new CustomResponseObject(Common.ADDING_FAIL, message));
            e.printStackTrace();

        }
        return null;
    }
}
