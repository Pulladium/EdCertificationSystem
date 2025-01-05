package com.vozh.art.dataservice.dto.request;


import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateCatRequest extends CategoryRequest {

    private Long parentCategoryId;

}
