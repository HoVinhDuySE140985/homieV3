package hommie.hommie.shared.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Common {

    public static final String IPN_URL_MOMO = "https://tiemhommie-0835ad80e9db.herokuapp.com/api/v1/MomoConfirm";
//    public static final String REDIRECT_URL_MOMO = "https://lobster-app-uadur.ondigitalocean.app/api/v1/MomoConfirm";
    public static final String REDIRECT_URL_MOMO = "https://tiemhommie-0835ad80e9db.herokuapp.com/api/v1/MomoConfirm";
    public static final String PARTNER_CODE = "MOMOM1IH20220922";
    public static final String ACCESS_KEY = "CXSZrRmGBowTzMUv";
    public static final String SECRET_KEY = "S8vHx9fepIhC9Sr4lML7C5Graf4w5k2p";
    public static final String MOMO_URI = "https://test-payment.momo.vn/v2/gateway/api/create";


    // althogrithm
    public static final String HMACSHA256 = "HmacSHA256";
    public static final String HMACSHA512 = "HmacSHA512";

}
