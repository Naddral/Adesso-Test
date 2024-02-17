package io.naddral.adessotest.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class PizzaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8330312773768378390L;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String orderNumber;

}
