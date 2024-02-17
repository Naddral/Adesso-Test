package io.naddral.adessotest.controller;

import io.naddral.adessotest.dto.BaseResponseDTO;
import io.naddral.adessotest.dto.OrderDTO;
import io.naddral.adessotest.exception.AwesomePizzaException;
import io.naddral.adessotest.service.OrderService;
import io.naddral.adessotest.util.StatusOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderModel) throws AwesomePizzaException {
        return ResponseEntity.ok(orderService.createOrder(orderModel));
    }

    @PostMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrder() throws AwesomePizzaException {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("orderNumber") String orderId) throws AwesomePizzaException{
        OrderDTO order = orderService.getOrder(orderId);
        if(order == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }
//
    @PutMapping("/{orderNumber}")
    public ResponseEntity<BaseResponseDTO> updateOrderStatus(
            @RequestParam("status") StatusOrder status,
            @PathVariable("orderNumber") String orderNumber) throws AwesomePizzaException{
        boolean updated = orderService.updateOrderStatus(orderNumber, status);
        if(!updated)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new BaseResponseDTO());
    }

    @GetMapping("/next")
    public ResponseEntity<OrderDTO> getNextOrder() throws AwesomePizzaException{
        OrderDTO order = orderService.getNextOrder();
        if(order == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

}
