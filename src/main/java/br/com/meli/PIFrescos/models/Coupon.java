package br.com.meli.PIFrescos.models;


import lombok.*;

import javax.persistence.*;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity @Builder
@Table(name= "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String couponName;
    private String description;
    private StorageType sellingType;
    private Boolean active;
    private Double discountUnit;
    private DiscountType discountType;
    private Integer maxUsage;
    private Integer currentUsage;

}
