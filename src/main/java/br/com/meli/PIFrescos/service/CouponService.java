package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> listAllCoupons(){
        List<Coupon> couponsList = couponRepository.findAll();
        if(couponsList.isEmpty()){
            throw new EntityNotFoundException("Coupons list is empty.");
        }
        return couponsList;
    }

    public Coupon createCoupon(Coupon coupon){
        if(couponRepository.findCouponByCouponName(coupon.getCouponName()) != null){
            throw new RuntimeException("Coupon already exists.");
        }
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Coupon coupon){
        if(!couponRepository.findById(coupon.getId()).isPresent()){
            throw new RuntimeException("Coupon not found.");
        }
        return couponRepository.save(coupon);
    }

}
