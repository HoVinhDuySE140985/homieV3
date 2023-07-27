package hommie.hommie.dto.requestDTO.MomoDTO;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class MomoRequest {
    private String partnerCode;
      
    private String partnerName; // option
    
    private String storeId;   // option
    
    private String requestId; // == orderId
    
    private String requestType;
    
    private String ipnUrl;
    
    private String redirectUrl;
    
    private String orderId;
    
    private String orderInfo;
    
    private String extraData; // codebase64 - or ""
    
    private boolean autoCapture; // default is true. if false transaction is not auto capture
    
    private String lang; // vi - en
    
    private String signature;
    
    private BigDecimal amount;
    
    private CustomerInfoMomoRequest userInfo;
}
