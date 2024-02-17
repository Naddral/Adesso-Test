package io.naddral.adessotest.repository;

import io.naddral.adessotest.model.OrderModel;
import io.naddral.adessotest.util.StatusOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderModel, String> {

    OrderModel findFirstByStatusNotIn(List<StatusOrder> statusOrderList, Sort sort);

    List<OrderModel> findByOrderByDateAsc();

}
