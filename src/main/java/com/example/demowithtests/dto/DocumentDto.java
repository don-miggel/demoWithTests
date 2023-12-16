package com.example.demowithtests.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    @NotNull
    private String number;

    @NotNull
    private String type;

    @NotNull
    private int expireYear;

    @NotNull
    private int expireMonth;

    @NotNull
    private int expireDay;

}
