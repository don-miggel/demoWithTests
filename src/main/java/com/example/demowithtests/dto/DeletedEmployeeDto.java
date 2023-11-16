package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class DeletedEmployeeDto{

    @Schema(description = "Id of deleted employee")
    private final Integer id;

    @Schema(description = "Message about deletion")
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Date and time of deletion")
    private final LocalDateTime deletionTime;


    public DeletedEmployeeDto(Employee empToDel) {
        this.id = empToDel.getId();
        this.deletionTime = LocalDateTime.now();
        this.message = String.format("Employee with id= %d has been deleted at %s !"
                ,empToDel.getId(),
                 deletionTime.toString().substring(0, deletionTime.toString().lastIndexOf('.')
                         )
                         .replace('T',' ')
        );
    }
}
