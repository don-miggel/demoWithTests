package com.example.demowithtests.service.history_record;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.HistoryRecord;
import com.example.demowithtests.dto.HistoryRecordDto;
import com.example.demowithtests.dto.HistoryRecordReadDto;

import java.util.List;

public interface HistoryRecordService {

    HistoryRecordReadDto addHistoryRecord(HistoryRecordDto historyRecordDto);

    List<HistoryRecordReadDto> getHistoryRecordsForDocument(Document document);
}
