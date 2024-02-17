package io.naddral.adessotest.model;

import io.naddral.adessotest.util.StatusOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Data
@Table(name = "order_model")
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name = "order_number", nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private StatusOrder status = StatusOrder.PENDING;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_timestamp", nullable = false, updatable = false)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_last_update")
    private Date lastUpdate;

    @Column(name = "order_customer")
    private String customer;

    @Column(name = "order_price")
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    List<PizzaModel> pizzas;

}
