package com.vozh.art.dataservice.dto.request;

import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.Organization;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String contactInfo;
}
