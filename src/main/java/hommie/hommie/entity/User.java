package hommie.hommie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 60)
    private String email;
    @Column(length = 500)
    private String avatar;
    @Column(length = 10)
    private String phoneNumber;
    private String Address;
    private String gender;
    private LocalDate dob;
    private String password;
    private String tokenDevice;
    @Column(length = 20)
    private String verificationCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<FeedBack> feedBacks;



    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Report> reportList;
}
