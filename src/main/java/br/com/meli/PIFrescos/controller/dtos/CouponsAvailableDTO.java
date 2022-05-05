package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.models.DiscountType;
import br.com.meli.PIFrescos.models.StorageType;

import java.util.List;
import java.util.stream.Collectors;

//DTO do UserStory do comprador
public class CouponsAvailableDTO {

    private String couponName;
    private String description;
    private StorageType sellingType;
    private Double discountUnit;
    private DiscountType discountType;

    public CouponsAvailableDTO(Coupon coupon){
        this.couponName = coupon.getCouponName();
        this.description = coupon.getDescription();
        this.sellingType = coupon.getSellingType();
        this.discountUnit = coupon.getDiscountUnit();
        this.discountType = coupon.getDiscountType();
    }

    public static List<CouponsAvailableDTO> convertToList(List<Coupon> coupons){
        return coupons.stream().map(CouponsAvailableDTO::new).collect(Collectors.toList());
    }
}
