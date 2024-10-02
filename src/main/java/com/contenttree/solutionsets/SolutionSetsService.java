package com.contenttree.solutionsets;

import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
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
    @Autowired
    VendorsService vendorsService;

    public String uploadSolutionSets(MultipartFile file, long vendorId){

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte [] fileBytes;

        try {
            fileBytes= file.getBytes();
        }
        catch (IOException e){
            return "File Upload failed : " + e.getMessage();
        }

        Vendors v = vendorsService.getVendorsById(vendorId);
        SolutionSets solutionSets = SolutionSets.builder()
                .name(fileName)
                .fileType(fileType)
                .uploadedBy(v)
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

    public SolutionSets getById(long id) {
        return solutionSetsRepository.findById(id).orElse(null);
    }

    public List<SolutionSets> getSolutionSetsByVendorId(long vendorId) {
        return solutionSetsRepository.findByUploadedBy(vendorId);
    }
}
