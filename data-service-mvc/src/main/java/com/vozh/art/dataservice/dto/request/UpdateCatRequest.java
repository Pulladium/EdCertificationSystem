package com.vozh.art.dataservice.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateCatRequest  extends CategoryRequest {
    //id for update
    @NotNull
    private Long id;
}
