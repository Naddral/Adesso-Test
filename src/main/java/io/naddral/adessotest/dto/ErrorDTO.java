package io.naddral.adessotest.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ErrorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7572645826745158913L;
    private String code;

}
