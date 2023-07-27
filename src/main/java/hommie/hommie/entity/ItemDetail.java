package hommie.hommie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String color;
    @Column(length = 9)
    private String size;
    private BigDecimal price;
    private Integer quantity;
    @Column(length = 50 )
    private String description;
//    private Integer rate;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @OneToMany(mappedBy = "itemDetail")
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "itemDetail")
    private List<CartItem> cartItems;

    @JsonIgnore
    @OneToMany(mappedBy = "itemDetail")
    private List<ItemImage> itemImages;
}
