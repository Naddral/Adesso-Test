package io.naddral.adessotest.repository;

import io.naddral.adessotest.model.PizzaModel;
import org.springframework.data.repository.CrudRepository;

public interface PizzaRepository extends CrudRepository<PizzaModel, Long> {

}
