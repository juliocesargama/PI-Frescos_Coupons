package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Coupon findCouponByCouponName(String coupon);

}
