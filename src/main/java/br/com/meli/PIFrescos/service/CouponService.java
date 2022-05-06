package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.models.DiscountType;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

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

    public List<ProductsCart> setDiscount(List<ProductsCart> productsCart, Coupon coupon, BigDecimal totalPrice){

        List<ProductsCart> discountedProducts = new ArrayList<ProductsCart>();
        
        //Caso o desconto seja um valor fixo
        if(coupon.getDiscountType().equals(DiscountType.CURRENCY)){

            //Verifica se o valor do desconto é suficiente para ser retirado do valor total.
            if(coupon.getDiscountUnit() > totalPrice.doubleValue()){
                throw new RuntimeException("Insuficient products value on cart to apply discount.");
            }

            for (ProductsCart product: productsCart) {

                //Define um valor de desconto igual para cada item
                double discount = coupon.getDiscountUnit() / product.getQuantity();

                // Comparação de cada produto com o tipo de cupom
                if(product.getBatch().getProduct().getProductType().equals(coupon.getSellingType())){

                    //Subtrai o valor unitário do valor igualitário por produto
                    BigDecimal newUnitPrice = product.getBatch().getUnitPrice().subtract(BigDecimal.valueOf(discount));
                    product.getBatch().setUnitPrice(newUnitPrice);

                    discountedProducts.add(product);

                }
            }
        }
        //Caso o desconto seja uma porcentagem
        if(coupon.getDiscountType().equals(DiscountType.PERCENTAGE)){
            //Define a porcentagem por unidade
            Double percentagePerUnit = 1 - (coupon.getDiscountUnit() / 100);


            for (ProductsCart product: productsCart) {

                // Comparação de cada produto com o tipo de cupom
                if(product.getBatch().getProduct().getProductType().equals(coupon.getSellingType())){

                    //multiplica o valor unitário com a porcentagem
                    BigDecimal newUnitPrice = product.getBatch().getUnitPrice().multiply(BigDecimal.valueOf(percentagePerUnit));
                    product.getBatch().setUnitPrice(newUnitPrice);

                    discountedProducts.add(product);
                    newUnitPrice = BigDecimal.valueOf(0);
                }

            }
        }
        return productsCart;
    }

    public Coupon getCouponByName(String couponName){
        Optional<Coupon> coupon = Optional.ofNullable(couponRepository.findCouponByCouponName(couponName));
        if(!coupon.isPresent()){
            throw new EntityNotFoundException("Coupon not found or invalid.");
        }
        return coupon.get();
    }

}
