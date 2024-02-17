package io.naddral.adessotest.dto;

import io.naddral.adessotest.util.StatusOrder;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1072696872862842436L;
    private String number;
    private Date date;
    private String customer;
    private StatusOrder status = StatusOrder.PENDING;
    private BigDecimal totalPrice;
    private List<PizzaDTO> pizzas;

}
