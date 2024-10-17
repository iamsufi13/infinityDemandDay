package com.contenttree.solutionsets;

import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolutionSetsService {
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    CategoryRepository categoryRepository;


    public String uploadSolutionSets(MultipartFile file, long vendorId, long categoryId) {

        String uploadDir = "src/main/resources/uploads/";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();

        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

        } catch (IOException e) {
            return "File Upload failed: " + e.getMessage();
        }

        Vendors vendor = vendorsService.getVendorsById(vendorId);
        if (vendor == null) {
            return "Vendor not found";
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return "Category not found";
        }

        SolutionSets solutionSets = SolutionSets.builder()
                .name(fileName)
                .fileType(fileType)
                .uploadedBy(vendor)
                .status(SolutionSetsStatus.PENDING)
                .category(category)
                .filePath(uploadDir + fileName)
                .build();

        solutionSetsRepository.save(solutionSets);

        return "File Uploaded Successfully: " + fileName;
    }

    public byte[] downloadPdf(long name){
        Optional<SolutionSets> dbPdfData = solutionSetsRepository.findById(name);

        return dbPdfData.map(SolutionSets::getFilePath).orElse(null).getBytes();
    }

    public List<SolutionSets> getLatestUploadedSolutionSets(){
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();
        List<SolutionSets> solutionSets1 = solutionSetsRepository.findAll().stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(SolutionSets::getDt1)))
                .limit(5)
                .collect(Collectors.toList());
        List<SolutionSetDto> solutionSetDtos = solutionSets1.stream().map(SolutionSetMapper::toSolutionSetDto)
                .toList();

        return solutionSets;


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
    public List<SolutionSets> getAllSolutionSets(){
        return solutionSetsRepository.findAll();
    }
    public List<SolutionSets> getSolutionSetsByCategory(String category)
    {
        return solutionSetsRepository.findByCategoryIgnoreCase(category);
    }

    public List<SolutionSets> getByCategoryId(long id) {
        List<SolutionSets> solutionSets= solutionSetsRepository.findAll();
        List<SolutionSets> solutionSets1 = solutionSets.stream()
                .filter(solutionSet -> solutionSet.getCategory().getId()==id)
                .toList();
        return solutionSets1;
    }
}
