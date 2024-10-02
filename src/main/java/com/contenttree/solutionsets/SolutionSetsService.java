package com.contenttree.solutionsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SolutionSetsService {
    @Autowired
    SolutionSetsRepository solutionSetsRepository;

    public String uploadSolutionSets(MultipartFile file){

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte [] fileBytes;

        try {
            fileBytes= file.getBytes();
        }
        catch (IOException e){
            return "File Upload failed : " + e.getMessage();
        }

        SolutionSets solutionSets = SolutionSets.builder()
                .name(fileName)
                .fileType(fileType)
                .filePath(fileBytes).build();

        solutionSetsRepository.save(solutionSets);


        return "File Uploaded SuccessFully:"+ fileBytes;
    }
    public byte[] downloadPdf(String name){
        Optional<SolutionSets> dbPdfData = solutionSetsRepository.findByName(name);

        if (dbPdfData.isPresent()){
            return dbPdfData.get().getFilePath();
        }
        return null;
    }

    public List<SolutionSets> getAllSolutioinSets() {
        return solutionSetsRepository.findAll();
    }
}
