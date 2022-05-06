package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.config.security.TokenService;
import br.com.meli.PIFrescos.controller.dtos.PurchaseOrderDTO;
import br.com.meli.PIFrescos.controller.dtos.TotalPriceDTO;
import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.service.CouponService;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/fresh-products/orders")
public class PurchaseOrderController {

    @Autowired
    private IPurchaseOrderService service;

    @Autowired
    private CouponService couponService;

    @Autowired
    private TokenService tokenService;

    /**
     * Insere nova compra e retorna o valor total do pedido.
     * @return TotalPriceDTO
     * @author Julio César Gama / Felipe Myose
     */

    @PostMapping("")
    public ResponseEntity<TotalPriceDTO> postOrder(@RequestBody PurchaseOrderForm purchaseOrderForm, @RequestParam(required = false) String coupon){

        PurchaseOrder order = purchaseOrderForm.convertToEntity(tokenService);
        PurchaseOrder savedOrder = service.save(order);


        if(!coupon.isEmpty()) {
            Coupon savedCoupon = couponService.getCouponByName(coupon);
            BigDecimal temporaryTotalPrice = service.calculateTotalPrice(savedOrder);
            List<ProductsCart> discountedProducts = couponService.setDiscount(savedOrder.getCartList(), savedCoupon, temporaryTotalPrice);

            savedOrder.setCartList(discountedProducts);

        }

        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity<>(new TotalPriceDTO(totalPrice), HttpStatus.CREATED);
    }

    /**
     * Atualiza compra e retorna o valor total do pedido.
     *
     * @return TotalPriceDTO
     * @author Juliano Alcione de Souza
     */

    @PutMapping
    public ResponseEntity<TotalPriceDTO> putOrder(@RequestBody PurchaseOrderForm form) {
        PurchaseOrder order = form.convertToEntity(tokenService);
        PurchaseOrder savedOrder = service.updateCartList(order);
        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity(new TotalPriceDTO(totalPrice), HttpStatus.OK);
    }


    /**
     * Este endpoint retorna todos os produtos de um pedido em aberto.
     * @return  OrderProductDTO
     * @author Antonio Hugo
     * Refactor: Ana Preis
     */

    @GetMapping("")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrder() {
        User userLogged = tokenService.getUserLogged();
        PurchaseOrder purchaseOrder = service.getPurchaseOrderByUserIdAndStatusIsOpened(userLogged.getId());
        return ResponseEntity.ok().body(PurchaseOrderDTO.convert(purchaseOrder));
    }

    /**
     * retorna a lista de todos os purchaseOrder do cliente, tanto OPENNED quanto FINISHED.
     * @return List<PurchaseOrderDTO>
     * @author Felipe Myose
     * Refactor Ana Preis
     */
    @GetMapping("/all")
    public ResponseEntity<List<PurchaseOrderDTO>> getAllPurchasesOrder() {
        User userLogged = tokenService.getUserLogged();
        List<PurchaseOrder> purchaseOrders = service.getAllByUserId(userLogged.getId());
        return ResponseEntity.ok().body(PurchaseOrderDTO.convert(purchaseOrders));
    }

    /**
     * Este endpoint atualiza o OrderStatus do pedido de compra para FINISHED.
     * @return  TotalPriceDTO
     * @author Ana Preis
     */
    @PutMapping("/finish")
    public ResponseEntity<TotalPriceDTO> updateOrderStatus() {
        User userLogged = tokenService.getUserLogged();
        PurchaseOrder purchaseOrder = service.getPurchaseOrderByUserIdAndStatusIsOpened(userLogged.getId());
        purchaseOrder = service.updateOrderStatus(purchaseOrder.getId());
        BigDecimal totalPrice = service.calculateTotalPrice(purchaseOrder);
        return ResponseEntity.ok().body(new TotalPriceDTO(totalPrice));
    }
}
