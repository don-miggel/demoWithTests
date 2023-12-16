package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Status;
import com.example.demowithtests.dto.DocumentDeletedDto;
import com.example.demowithtests.dto.DocumentRestoreDto;
import com.example.demowithtests.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceBean implements DocumentService {

    private final DocumentRepository documentRepository;

    /**
     * @param document
     * @return
     */
    @Override
    public Document create(Document document) {
        document.setExpireDate(LocalDateTime.now().plusYears(5));
        return documentRepository.save(document);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document getById(Integer id) {

        return documentRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Document with id: "+ id+ " is not found !")
        );
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document handlePassport(Integer id) {
        Document document = getById(id);
        if (document.getIsHandled()) {
            throw new RuntimeException();
        } else document.setIsHandled(Boolean.TRUE);
        return documentRepository.save(document);
    }

    /**
     * @param passportId
     * @param imageId
     * @return
     */
    @Override
    public Document addImage(Integer passportId, Integer imageId) {
        return null;
    }

    @Override
    public Document getDocumentBySerialNumber(String number) {

        return documentRepository.findDocumentByNumber(number).orElseThrow(
                ()-> new EntityNotFoundException("Document with serial number: "+ number+" is not found !")
        );
    }

    @Override
    public DocumentDeletedDto deleteDocumentById(Integer id) {

        Document doc = getById(id);
        String docNumber = doc.getNumber();

        if(doc.getStatus().equals(Status.DEACTIVATED))
            throw new RuntimeException("The "+ doc.getDocumentType() +" with number: "+ docNumber+" has already been deleted");

        doc.setStatus(Status.DEACTIVATED);
        documentRepository.save(doc);

        return DocumentDeletedDto
                .builder()
                .message("The "+ doc.getDocumentType() +" with number: "+ docNumber+" has successfully been deleted")
                .deletedDocumentNumber(docNumber)
                .build();
    }

    /*

    @Override
    public DocumentDeletedDto deleteDocumentByNumber(String number) {

        Document document = documentRepository.findDocumentByNumber(number).orElseThrow(
                ()-> new EntityNotFoundException("Document with number: " + number + " was not found! ")
        );
        return deleteDocumentById(document.getId());
    }

     */

    @Override
    public DocumentRestoreDto restoreDocument(Employee emp) {

        Document doc  = emp.getDocument();

        if(doc== null)
            throw new EntityNotFoundException("User with id: "+ emp.getId()+ " does not have any document!");

        doc = documentRepository.findDocumentByNumber(doc.getNumber()).orElseThrow(
                ()-> new EntityNotFoundException("Document with number: " + emp.getDocument().getNumber()
                        + " was not found! ")
        );

        if(doc == null || !doc.getStatus().equals(Status.DEACTIVATED))
            throw new RuntimeException("The document of the user with id: "+ emp.getId()
                    +" is either active or absent");

        doc.setStatus(Status.RESTORED);
        documentRepository.save(doc);

        return DocumentRestoreDto.builder()
                .restoreDate(LocalDateTime.now().toString())
                .number(doc.getNumber())
                .message("Document with number: " + doc.getNumber() + " has successfully been restored ! ")
                .build();

    }
}
