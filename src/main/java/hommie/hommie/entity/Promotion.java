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
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    @Column(length = 50)
    private String title;
    private String type;
    private String code;
    private LocalDate dateStart;
    private LocalDate dateExp;
    private float value;
    @Column(length = 500)
    private String description;
    private String status;
    private float minValueOrder;
    private float maxValueDiscount;
    private int quantity;

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    private List<Order> orderList;

}
