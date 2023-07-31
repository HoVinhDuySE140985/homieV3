package hommie.hommie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 15)
    private String orderCode;
    private LocalDate orderDate;
    @Column(length = 80)
    private String shipAddress;
    @Column(length = 10)
    private String phoneNumber;
    private String status;
    private BigDecimal totalPrice;
    private String paymentType;
    private String cancelReason;
    private String userReceive;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promo_id")
    private Promotion promotion;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Report> reportList;
}
