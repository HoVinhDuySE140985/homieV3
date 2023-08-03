package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.*;
import hommie.hommie.dto.requestDTO.LoginGoogleRequest.OAuth2Request;
import hommie.hommie.dto.responseDTO.RegisterResponseDTO;
import hommie.hommie.dto.responseDTO.UserResponseDTO;

public interface UserService {

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    String login(LoginRequestDTO loginRequestDTO);

    String updateUser(UpdateUserRequestDTO updateUserRequestDTO);

    String changePassword(ChangePasswordRequestDTO changePasswordRequestDTO);

    UserResponseDTO getUserinfo(String email);

    String sendVerificationCode(String email);

    String forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO);
    Boolean isExistUserByEmail(String email);

    String loginWithGoogle(OAuth2Request data, String fcmKey);

    String logout(String userName);

}
