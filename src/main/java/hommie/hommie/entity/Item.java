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
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40)
    private String name;
    private String avatar;
    @Column(length = 100)
    private String material;
    private LocalDate createDate;
    private String status;

//    @JsonIgnore
//    @OneToMany(mappedBy = "item")
//    private List<ItemImage> itemImages;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subCate_id")
    private SubCategory subCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<ItemDetail> details;
}
