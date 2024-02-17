package io.naddral.adessotest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AwesomePizzaException extends Exception{

    private List<String> args = new ArrayList<>();
    private String code;
    private String detail;

}
