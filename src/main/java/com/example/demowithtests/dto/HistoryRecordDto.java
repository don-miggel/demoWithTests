package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Document;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryRecordDto {

    @NotNull
    private String description;

    @NotNull
    private Document document;
}
