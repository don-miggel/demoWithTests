package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Document;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("select d from  Document d where d.number = ?1")
    @EntityGraph(attributePaths = {"history"})
    Optional<Document> findDocumentByNumber(String number);
}
