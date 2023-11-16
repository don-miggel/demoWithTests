package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class UpdEmployeeNameDto {

    @Schema(description = "Id of the updated user")
    private final Integer id;

    @Schema(description = "Date and time of name being updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime dateOfUpd;

    @Schema(description = "Previous name before updating")
    private final String previousName;

    @Schema(description = "Current name after updating")
    private final String updatedName;

    @Schema(description = "Message to user about a name being updated")
    private final String message;

    public UpdEmployeeNameDto(String previousName, Employee employeeToUpd){
        id = employeeToUpd.getId();
        dateOfUpd = LocalDateTime.now();
        this.previousName = previousName;
        this.updatedName = employeeToUpd.getName();
        message = String.format("Employee with id= %d has changed name from  %s to %s at %s !"
                ,id, previousName, updatedName,
                dateOfUpd.toString().substring(0, dateOfUpd.toString().lastIndexOf('.')
                        )
                        .replace('T',' ') );
    }
}
