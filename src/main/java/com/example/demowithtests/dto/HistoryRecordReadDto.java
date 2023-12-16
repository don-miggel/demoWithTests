package com.example.demowithtests.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class HistoryRecordReadDto {

    private String description;

    private String document;

    private String changeDate;

}
