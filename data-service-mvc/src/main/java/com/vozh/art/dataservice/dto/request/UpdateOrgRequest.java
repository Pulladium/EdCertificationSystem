package com.vozh.art.dataservice.dto.request;

import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
public class UpdateOrgRequest extends OrganizationRequest{
    private Long organizationId;

    private List<Long> certificateIds;
}
