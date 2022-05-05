package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Coupon;
import br.com.meli.PIFrescos.models.DiscountType;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponsForm {

    private Integer id;
    @NotNull(message = "O cupom não pode ser nulo.")
    @Size(min = 5, max =  10, message = "O cupom deve conter entre 5 a 10 caracteres.")
    private String couponName;
    @NotNull (message = "A descrição do cupom não pode ser nulo.")
    @Size(min = 5, max =  255, message = "A descrição do cupom deve conter entre 5 a 255 caracteres.")
    private String description;
    @NotNull (message = "O tipo de venda do cupom não pode ser nulo.")
    private StorageType sellingType;
    private Boolean active;
    @Positive(message = "A unidade de desconto deve ser um número maior que zero.")
    @NotNull (message = "A unidade de desconto do cupom não pode ser nulo.")
    private Double discountUnit;
    @NotNull (message = "O tipo de desconto do cupom não pode ser nulo.")
    private DiscountType discountType;
    @Positive(message = "A quantidade máxima de cupons deve ser um número maior que zero.")
    @NotNull (message = "A quantidade máxima de cupons não pode ser nulo.")
    private Integer maxUsage;
    private Integer currentUsage;

    public Coupon convert(){return new Coupon(id,couponName,description,sellingType,active,discountUnit,discountType,maxUsage,currentUsage);}

}
