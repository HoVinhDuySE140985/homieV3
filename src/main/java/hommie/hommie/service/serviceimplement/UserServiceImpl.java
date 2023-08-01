package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.*;
import hommie.hommie.dto.requestDTO.LoginGoogleRequest.OAuth2Request;
import hommie.hommie.dto.responseDTO.EmailResponseDTO;
import hommie.hommie.dto.responseDTO.RegisterResponseDTO;
import hommie.hommie.dto.responseDTO.UserResponseDTO;
import hommie.hommie.entity.User;
import hommie.hommie.jwt.JwtConfig;
import hommie.hommie.repository.RoleRepo;
import hommie.hommie.repository.UserRepo;
import hommie.hommie.service.serviceinterface.EmailService;
import hommie.hommie.service.serviceinterface.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Override
    public RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO registerResponseDTO = null;
        User user = userRepo.findUserByEmail(registerRequestDTO.getEmail());
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Email đã được sử dụng !");
        } else {
            user = User.builder()
                    .name(registerRequestDTO.getName())
                    .email(registerRequestDTO.getEmail())
                    .phoneNumber(registerRequestDTO.getPhoneNumber())
                    .password(encoder.encode(registerRequestDTO.getPassword()))
                    .role(roleRepo.findById(2L).get())
                    .build();
            userRepo.save(user);

            registerResponseDTO = RegisterResponseDTO.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
        return registerResponseDTO;
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {

        String accesstoken = null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            User user = userRepo.findUserByEmail(authenticate.getName());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.valueOf(403), "Tài Khoản Hoặc Mật Khẩu Không Đúng");
            } else {
                user.setTokenDevice(loginRequestDTO.getFcmKey());
                userRepo.save(user);
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim("authorities", authenticate.getAuthorities())
                        .claim("id", user.getId())
                        .claim("name", user.getName())
                        .claim("email", user.getEmail())
                        .claim("phoneNumber", user.getPhoneNumber())
                        .claim("password", user.getPassword())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();
                accesstoken = jwtConfig.getTokenPrefix() + token;
            }
        }
        return accesstoken;
    }

    @Override
    public String loginWithGoogle(OAuth2Request data, String fcmKey) {
        String accesstoken = null;
        if (!userService.isExistUserByEmail(data.getEmail())) {
            RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(data.getFullname(), data.getEmail(), "",
                    data.getId());
            userService.createUser(registerRequestDTO);
        }else {
            Authentication authentication = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getId());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            if (authenticate.isAuthenticated()) {
                User user = userRepo.findUserByEmail(authenticate.getName());
                user.setTokenDevice(fcmKey);
                userRepo.save(user);
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), authenticate.getAuthorities())
                        .claim("id", user.getId())
                        .claim("name", user.getName())
                        .claim("email", user.getEmail())
                        .claim("phoneNumber", user.getPhoneNumber())
                        .claim("password", user.getPassword())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();
                accesstoken = jwtConfig.getTokenPrefix() + token;
            }
        }
        return accesstoken;
    }

    @Override
    public String updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        String mess = "Cập NHật Thất Bại";
        User user = userRepo.findById(updateUserRequestDTO.getId()).get();
        LocalDate dob = formatStringFromDate(updateUserRequestDTO.getDob());
        if (user != null) {
            user.setName(updateUserRequestDTO.getName());
            user.setPhoneNumber(updateUserRequestDTO.getPhoneNumber());
            user.setAddress(updateUserRequestDTO.getAddress());
            user.setGender(updateUserRequestDTO.getGender());
            user.setDob(dob);
            userRepo.save(user);
            mess = "Cập Nhập Thành Công";
        }
        return mess;
    }

    @Override
    public String changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String mess = "Cập NHật Thất Bại";
        User user = userRepo.findUserByEmail(changePasswordRequestDTO.getEmail());
        if (user != null) {
            Boolean check = encoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword());
            if (check) {
                if (changePasswordRequestDTO.getNewPassword().equalsIgnoreCase(changePasswordRequestDTO.getConfirmPassword())) {
                    user.setPassword(encoder.encode(changePasswordRequestDTO.getNewPassword()));
                    userRepo.save(user);
                    mess = "Cập Nhập Thành Công";
                }
            }
        }
        return mess;
    }

    @Override
    public UserResponseDTO getUserinfo(String email) {
        UserResponseDTO userResponseDTO = null;
        User user = userRepo.findUserByEmail(email);
        if (user.getDob() == null) {
            userResponseDTO = UserResponseDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .gender(user.getGender())
                    .dob(null)
                    .Address(user.getAddress())
                    .build();
        } else {
            userResponseDTO = UserResponseDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .gender(user.getGender())
                    .dob(formatDateFromString(user.getDob()))
                    .Address(user.getAddress())
                    .build();
        }
        return userResponseDTO;
    }

    @Override
    public String sendVerificationCode(String email) {
        String verificationCode = "TH" + randomPassword();
        User user = userRepo.findUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Email không tồn tại");
        } else {
            user.setVerificationCode(verificationCode);
            userRepo.save(user);
            EmailResponseDTO emailResponseDTO = EmailResponseDTO.builder()
                    .email(email)
                    .subject("Mã Xác Nhận Đổi Mật Khẩu" + email)
                    .massage("Xin chào " + user.getName() + ",\n" +
                            "\n" +
                            "Chúng tôi xin gửi đến bạn mã xác thực \n" +
                            "\n" +
                            "Mã Xác Thực: " + verificationCode + "\n" +
                            "\n" +
                            "Vui lòng không cung cấp mã xác thực cho người khác!\n" +
                            "\n" +
                            "Trân trọng,\n" +
                            "\n" +
                            "Phòng hỗ trợ khách hàng.\n" +
                            "(Đây là email được gửi tự động, Quý khách vui lòng không hồi đáp theo địa chỉ email này.)")
                    .build();
            emailService.sendSimpleMail(emailResponseDTO);
        }
        return verificationCode;
    }

    public String randomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    @Override
    public String forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        String mess = "Cập NHật Thất Bại";
        User user = userRepo.findUserByEmail(forgotPasswordRequestDTO.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Email Không Đúng Vui Lòng Nhập Lại !");
        } else {
            if (user.getVerificationCode().equalsIgnoreCase(forgotPasswordRequestDTO.getVerificationCode())) {
                user.setPassword(encoder.encode(forgotPasswordRequestDTO.getNewPassword()));
                user.setVerificationCode("null");
                userRepo.save(user);
                mess = "Cập Nhập Thành Công";
                return mess;
            }
        }
        return mess;
    }

    private LocalDate formatStringFromDate(String date) {
        String[] split = date.split("-");
        String day = split[0], month = split[1];
        if (Integer.parseInt(split[0]) < 10) {
            day = split[0].split("")[1];
        }
        if (Integer.parseInt(split[1]) < 10) {
            month = split[1].split("")[1];
        }
        LocalDate localDate = LocalDate.of(Integer.parseInt(split[2]), Integer.parseInt(month), Integer.parseInt(day));
        return localDate;
    }

    private String formatDateFromString(LocalDate date) {
        String day = null, month = null;
        if (date.getDayOfMonth() < 10) {
            day = 0 + "" + date.getDayOfMonth();
        } else {
            day = "" + date.getDayOfMonth();
        }
        if (date.getMonthValue() < 10) {
            month = 0 + "" + date.getMonthValue();
        } else {
            month = "" + date.getMonthValue();
        }
        return day + "-" + month + "-" + date.getYear();
    }

    @Override
    public Boolean isExistUserByEmail(String email) {
        User user = userRepo.findUserByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
