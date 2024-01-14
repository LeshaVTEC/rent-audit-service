package org.alexey.rentauditservice.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.alexey.rentauditservice.core.entity.EssenceType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AuditInfoDto {

    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("dt_create")
    private Long creationDate;

    @JsonProperty("user")
    private UserAuditDto userAuditDto;

    @JsonProperty("text")
    private String action;

    @JsonProperty("type")
    private EssenceType essenceType;

    @JsonProperty("id")
    private String essenceTypeId;
}
