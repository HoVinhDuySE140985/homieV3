package hommie.hommie.dto.requestDTO.MomoDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class MomoClientRequest {


//      private String partnerName; // option

//      private String storeId;   // option

//      private String requestId; // == orderId

//      private String ipnUrl;

//      private String redirectUrl;

    private Long amount;

    private Long orderId;


// private String orderInfo;

//      private String extraData; // codebase64 - or ""


//      private boolean autoCapture; // default is true. if false transaction is not auto capture

//      private String lang; // vi - en

//      private String signature; // encode by Hmac_SHA256 with string 
           /*
            accessKey=$accessKey
            &amount=$amount
            &extraData=$extraData
            &ipnUrl=$ipnUrl
            &orderId=$orderId
            &orderInfo=$orderInfo
            &partnerCode=$partnerCode
            &redirectUrl=$redirectUrl
            &requestId=$requestId
            &requestType=$requestType 
           */
}
