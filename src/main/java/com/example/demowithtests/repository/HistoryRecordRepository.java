package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Long> {

    List<HistoryRecord> findHistoryRecordByDocument(Document document);

}
