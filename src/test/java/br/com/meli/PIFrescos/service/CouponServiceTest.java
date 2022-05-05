package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.models.DiscountType;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.CouponRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    CouponService couponService;

    List<Coupon> coupons = new ArrayList<>();
    Coupon coupon1 = new Coupon(1,"TESTE1","brief description", StorageType.FRESH,true, 10.0, DiscountType.PERCENTAGE,10,5);
    Coupon coupon2 = new Coupon(2,"TESTE2","brief description", StorageType.REFRIGERATED,true,2.0, DiscountType.CURRENCY,10,2);
    Coupon coupon3 = new Coupon(3,"TESTE3","brief description", StorageType.FROZEN,false,15.0, DiscountType.PERCENTAGE,20,20);

    @BeforeEach
    void SetUp(){
        coupons.add(coupon1);
        coupons.add(coupon2);
        coupons.add(coupon3);
    }

    @AfterEach
    void TearDown(){
        coupons.clear();
    }

    @Test
    @DisplayName("Test return all coupons")
    void ShouldReturnAllCoupons(){

        Mockito.when(couponRepository.findAll()).thenReturn(coupons);

        Assertions.assertEquals(coupons,couponService.listAllCoupons());
    }

    @Test
    @DisplayName("Test return exception coupons list is empty")
    void ShouldReturnAnExceptionForEmptyCouponList(){
        String message = "Coupons list is empty.";

        List<Coupon> emptyList = Collections.emptyList();
        Mockito.when(couponRepository.findAll()).thenReturn(emptyList);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> couponService.listAllCoupons());
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Test create a new coupon")
    void ShouldReturnACreatedCoupon(){
        Coupon newCoupon = new Coupon(4,"LIQUIDA10","Liquidação de congelados 10%", StorageType.FROZEN,
                true,10.0,DiscountType.PERCENTAGE,50,0);

        Mockito.when(couponRepository.save(newCoupon)).thenReturn(newCoupon);

        Assertions.assertEquals(couponRepository.save(newCoupon),couponService.createCoupon(newCoupon));
    }

    @Test
    @DisplayName("Test throws an exception of coupon already exists")
    void ShouldReturnAnExceptionCouponAlreadyExists(){

        String message = "Coupon already exists.";

        Mockito.when(couponRepository.findCouponByCouponName(coupon1.getCouponName())).thenReturn(coupon1);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> couponService.createCoupon(coupon1));
        assertThat(exception.getMessage()).isEqualTo(message);

    }

    @Test
    @DisplayName("Test update an existing coupon")
    void ShouldReturnAnUpdatedCoupon(){
        coupon1.setActive(false);

        Mockito.when(couponRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(coupon1));

        Assertions.assertEquals(couponRepository.save(coupon1),couponService.updateCoupon(coupon1));
    }

    @Test
    @DisplayName("Test coupon was not found")
    void ShouldReturnAnExceptionCupomNotFound(){
        String message = "Coupon not found.";

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->couponService.updateCoupon(coupon1));
        assertThat(exception.getMessage()).isEqualTo(message);
    }

}
