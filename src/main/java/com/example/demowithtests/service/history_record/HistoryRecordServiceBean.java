package com.example.demowithtests.service.history_record;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.HistoryRecord;
import com.example.demowithtests.dto.HistoryRecordDto;
import com.example.demowithtests.dto.HistoryRecordReadDto;
import com.example.demowithtests.repository.HistoryRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryRecordServiceBean implements HistoryRecordService{

    private final HistoryRecordRepository historyRecordRepository;

    @Autowired
    public HistoryRecordServiceBean(HistoryRecordRepository historyRecordRepository) {
        this.historyRecordRepository = historyRecordRepository;
    }

    @Override
    public HistoryRecordReadDto addHistoryRecord(HistoryRecordDto historyRecordDto) {

        HistoryRecord newHistoryRecord = HistoryRecord
                                            .builder()
                                            .document(historyRecordDto.getDocument())
                                            .changeDescription(historyRecordDto.getDescription())
                                            .changeDate(LocalDateTime.now())
                                            .build();

        historyRecordDto.getDocument().getHistory().add(newHistoryRecord);

        historyRecordRepository.save(newHistoryRecord);

        HistoryRecord addedHistoryRecord =
                historyRecordDto.getDocument()
                        .getHistory()
                        .get(historyRecordDto.getDocument().getHistory().size()-1);

        return HistoryRecordReadDto.builder().changeDate(addedHistoryRecord.getChangeDate().toString())
                .document("Document number: "+ addedHistoryRecord.getDocument().getNumber() +" "+
                        "Document uuid: "+ addedHistoryRecord.getDocument().getNumber() )
                .description(addedHistoryRecord.getChangeDescription())
                .build();
    }

    @Override
    public List<HistoryRecordReadDto> getHistoryRecordsForDocument(Document document) {

        return historyRecordRepository
                .findHistoryRecordByDocument(document)
                .stream()
                .map(hr-> HistoryRecordReadDto.builder()
                        .description(hr.getChangeDescription())
                        .changeDate(hr.getChangeDate().toString())
                        .document(hr.getDocument().getNumber())
                        .build())
                .toList();
    }
}
