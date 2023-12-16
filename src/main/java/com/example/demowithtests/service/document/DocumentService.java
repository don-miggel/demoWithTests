package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.DocumentDeletedDto;
import com.example.demowithtests.dto.DocumentRestoreDto;

public interface DocumentService {

    Document create(Document document);

    Document getById(Integer id);

    Document handlePassport(Integer id);

    Document addImage(Integer passportId, Integer imageId);

    Document getDocumentBySerialNumber(String number);

    DocumentDeletedDto deleteDocumentById(Integer id);

 //   DocumentDeletedDto deleteDocumentByNumber(String number);

    DocumentRestoreDto restoreDocument(Employee emp);
}
