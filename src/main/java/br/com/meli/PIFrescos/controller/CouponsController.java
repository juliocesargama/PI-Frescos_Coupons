package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.CouponsAvailableDTO;
import br.com.meli.PIFrescos.controller.forms.CouponsForm;
import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.service.CouponService;
import br.com.meli.PIFrescos.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fresh-products/coupons")
public class CouponsController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private InboundOrderService inboundOrderService;

    @GetMapping
    public ResponseEntity<List<CouponsAvailableDTO>> getAvailableCoupons(){

        List<Coupon> availableCoupons = this.couponService.listAllCoupons();
        return new ResponseEntity(CouponsAvailableDTO.convertToList(availableCoupons), HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){

        List<Coupon> allCoupons = this.couponService.listAllCoupons();
        return new ResponseEntity(allCoupons,HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody @Valid CouponsForm form){

        Coupon coupon = this.couponService.createCoupon(form.convert());
        return new  ResponseEntity(coupon,HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> update(@PathVariable Integer id, @Valid @RequestBody CouponsForm form){

        form.setId(id);
        Coupon coupon = this.couponService.updateCoupon(form.convert());
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);

    }

}
