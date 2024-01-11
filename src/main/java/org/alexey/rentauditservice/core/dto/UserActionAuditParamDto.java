package org.alexey.rentauditservice.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.alexey.rentauditservice.core.entity.Report;
import org.alexey.rentauditservice.core.entity.ReportType;

import java.time.LocalDate;

import static org.alexey.rentauditservice.core.entity.ReportStatus.PROGRESS;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserActionAuditParamDto {

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate from;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate to;

    public static Report toEntity(ReportType type, UserActionAuditParamDto dto) {
        return new Report()
                .setStatus(PROGRESS)
                .setType(type)
                .setUserId(dto.getUserId())
                .setFromDate(dto.getFrom())
                .setToDate(dto.getTo())
                .setDescription(type.getDescription() + " from " + dto.getFrom() + " to " + dto.getTo());
    }
}
