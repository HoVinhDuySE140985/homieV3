package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.*;
import hommie.hommie.dto.requestDTO.LoginGoogleRequest.OAuth2Request;
import hommie.hommie.dto.responseDTO.RegisterResponseDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("register")
    @PermitAll
    public ResponseEntity<ResponseDTO> register(@RequestBody @Validated RegisterRequestDTO registerRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        RegisterResponseDTO registerResponseDTO = userService.createUser(registerRequestDTO);
        responseDTO.setData(registerResponseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("login")
    @PermitAll
    public ResponseEntity<ResponseDTO> login(@RequestBody @Validated LoginRequestDTO loginRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.login(loginRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("login-with-google")
    @PermitAll
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody @Valid OAuth2Request data,
                                                    @RequestParam @Valid String fcmKey) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.loginWithGoogle(data,fcmKey));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-user-info")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER')")
    public ResponseEntity<ResponseDTO> getUserInfo(@RequestParam @Valid String email){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.getUserinfo(email));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-user-info")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateUserInfo(@RequestBody @Validated UpdateUserRequestDTO updateUserRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.updateUser(updateUserRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("change-password")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody @Validated ChangePasswordRequestDTO changePasswordRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.changePassword(changePasswordRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("send-verificationCode")
    @PermitAll
    public ResponseEntity<ResponseDTO> sendVerificationCode(@RequestParam @Valid String email){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.sendVerificationCode(email));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("forgot-password")
    @PermitAll
    public ResponseEntity<ResponseDTO> forgotPassWord(@RequestBody @Validated ForgotPasswordRequestDTO forgotPasswordRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.forgotPassword(forgotPasswordRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("logout")
    @PreAuthorize("hasAnyRole('OWNER','CUSTOMER')")
    public ResponseEntity<ResponseDTO> logout(@RequestParam @Valid String userName){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userService.logout(userName));
        return ResponseEntity.ok().body(responseDTO);
    }
}
