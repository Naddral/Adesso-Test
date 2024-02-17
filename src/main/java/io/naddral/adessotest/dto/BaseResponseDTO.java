package io.naddral.adessotest.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BaseResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2467040047408691311L;
    private ErrorDTO error;

}
