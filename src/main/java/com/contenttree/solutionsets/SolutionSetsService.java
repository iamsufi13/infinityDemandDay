package com.contenttree.solutionsets;

import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class SolutionSetsService {
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    CategoryRepository categoryRepository;


//    public String uploadSolutionSets(MultipartFile file, long vendorId, long categoryId,String desc,String title) {
//
//        String uploadDir = "src/main/resources/uploads/";
//
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String fileName = file.getOriginalFilename();
//        String fileType = file.getContentType();
//
//        try {
//            Path filePath = Paths.get(uploadDir + fileName);
//            Files.write(filePath, file.getBytes());
//
//        } catch (IOException e) {
//            return "File Upload failed: " + e.getMessage();
//        }
//
//        Vendors vendor = vendorsService.getVendorsById(vendorId);
//        if (vendor == null) {
//            return "Vendor not found";
//        }
//
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        if (category == null) {
//            return "Category not found";
//        }
//
//        SolutionSets solutionSets = SolutionSets.builder()
//                .name(fileName)
//                .title(title)
//                .fileType(fileType)
//                .uploadedBy(vendor)
//                .status(SolutionSetsStatus.PENDING)
//                .category(category)
//                .description(desc)
//                .description(descinfiniteb2b)
//                .filePath(uploadDir + fileName)
//                .build();
//
//        solutionSetsRepository.save(solutionSets);
//
//        return "File Uploaded Successfully: " + fileName;
//    }
//public String uploadSolutionSets(MultipartFile file, MultipartFile imageFile, long vendorId, long categoryId, String desc, String title,int value) {
//    String uploadDir = "/var/www/infiniteb2b/springboot/whitePapers";
//    String imageUploadDir = "/var/www/infiniteb2b/springboot/whitepapersImages";
//
//    File solutionSetDirectory = new File(uploadDir);
//    File imageDirectory = new File(imageUploadDir);
//
//    if (!solutionSetDirectory.exists() && !solutionSetDirectory.mkdirs()) {
//        return "Failed to create upload directory.";
//    }
//
//    if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
//        return "Failed to create image directory.";
//    }
//
//    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//    String filePath = uploadDir + File.separator + fileName;
//
//    try {
//        Files.write(Paths.get(filePath), file.getBytes());
//        System.out.println("PDF File uploaded successfully at: " + filePath);
//    } catch (IOException e) {
//        e.printStackTrace();
//        return "PDF Upload failed: " + e.getMessage();
//    }
//
//
//    String imageName = imageFile.getOriginalFilename();
//    String imagePath = imageUploadDir + File.separator + imageName;
////    imagePath =imagePath.replace(" ","-");
//    try {
//        Files.write(Paths.get(imagePath), imageFile.getBytes());
//    } catch (IOException e) {
//        e.printStackTrace();
//        return "Image upload failed: " + e.getMessage();
//    }
//
//    Vendors vendor = vendorsService.getVendorsById(vendorId);
//    if (vendor == null) {
//        return "Vendor not found";
//    }
//
//    Category category = categoryRepository.findById(categoryId).orElse(null);
//    if (category == null) {
//        return "Category not found";
//    }
//    SolutionSets solutionSets = new SolutionSets();
//
//    if (value == 1){SolutionSets.builder()
//            .name(fileName)
//            .title(title)
//            .fileType(file.getContentType())
//            .uploadedBy(vendor)
//            .status(SolutionSetsStatus.APPROVED)
//            .category(category)
//            .description(desc)
//            .filePath(filePath)
//            .imagePath(imagePath)
//            .build();} else if (value==2) {
//        SolutionSets.builder()
//                .name(fileName)
//                .title(title)
//                .fileType(file.getContentType())
//                .uploadedBy(vendor)
//                .status(SolutionSetsStatus.PENDING)
//                .category(category)
//                .description(desc)
//                .filePath(filePath)
//                .imagePath(imagePath)
//                .build();
//    }
//
//
//
//    solutionSetsRepository.save(solutionSets);
//
//    return "File and Image Uploaded Successfully: " + fileName + ", " + imageName;
//}
public String uploadSolutionSets(MultipartFile file, MultipartFile imageFile, long vendorId, long categoryId, String desc, String title, int value) {
    String uploadDir = "/var/www/infiniteb2b/springboot/whitePapers";
    String imageUploadDir = "/var/www/infiniteb2b/springboot/whitepapersImages";

    File solutionSetDirectory = new File(uploadDir);
    File imageDirectory = new File(imageUploadDir);

    if (!solutionSetDirectory.exists() && !solutionSetDirectory.mkdirs()) {
        return "Failed to create upload directory.";
    }

    if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
        return "Failed to create image directory.";
    }

    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    String filePath = uploadDir + File.separator + fileName;

    try {
        Files.write(Paths.get(filePath), file.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
        return "PDF Upload failed: " + e.getMessage();
    }

    String imageName = imageFile.getOriginalFilename();
    String imagePath = imageUploadDir + File.separator + imageName;

    try {
        Files.write(Paths.get(imagePath), imageFile.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
        return "Image upload failed: " + e.getMessage();
    }

    Vendors vendor = vendorsService.getVendorsById(vendorId);
    if (vendor == null) {
        return "Vendor not found";
    }

    Category category = categoryRepository.findById(categoryId).orElse(null);
    if (category == null) {
        return "Category not found";
    }

    SolutionSets solutionSets = new SolutionSets();

    if (value == 1) {
        solutionSets = SolutionSets.builder()
                .name(fileName)
                .title(title)
                .fileType(file.getContentType())
                .uploadedBy(vendor)
                .status(SolutionSetsStatus.APPROVED)
                .category(category)
                .description(desc)
                .filePath(filePath)
                .imagePath(imagePath)
                .slug(title.replace(" ","-"))
                .build();
    } else if (value == 2) {
        solutionSets = SolutionSets.builder()
                .name(fileName)
                .title(title)
                .fileType(file.getContentType())
                .uploadedBy(vendor)
                .status(SolutionSetsStatus.PENDING)
                .category(category)
                .description(desc)
                .filePath(filePath)
                .imagePath(imagePath)
                .slug(title.replace(" ","-"))
                .build();
    }

    solutionSetsRepository.save(solutionSets);

    return "File and Image Uploaded Successfully: " + fileName + ", " + imageName;
}
    public List<SolutionSets> getByCategoryName(String categoryName) {
        // Fetch category by slug (name)
        Category category = categoryRepository.findByNameIgnoreCase(categoryName);

        // If category exists, fetch solution sets, otherwise return empty list
        if (category != null) {
            return solutionSetsRepository.findByCategory(category);
        } else {
            return List.of(); // Return an empty list if the category is not found
        }
    }




    //    public byte[] downloadPdf1(long name){
//        Optional<SolutionSets> dbPdfData = solutionSetsRepository.findById(name);
//
//        return Objects.requireNonNull(dbPdfData.map(SolutionSets::getFilePath).orElse(null)).getBytes();
//    }
//public String downloadPdf(long name) {
//    Optional<SolutionSets> dbPdfData = solutionSetsRepository.findById(name);
//
//    return dbPdfData.map(SolutionSets::getFilePath)
//            .orElseThrow(() -> new RuntimeException("File not found for the given ID: " + name));
//}
public byte[] downloadPdf(long id) throws IOException {
    Optional<SolutionSets> dbPdfData = solutionSetsRepository.findById(id);

    String filePath = dbPdfData.map(SolutionSets::getFilePath)
            .orElseThrow(() -> new RuntimeException("File not found for the given ID: " + id));

    return Files.readAllBytes(Paths.get(filePath));
}

    public String downloadPdf(long name, HttpServletRequest request) {
    Optional<SolutionSets> dbPdfData = solutionSetsRepository.findById(name);

    String filePath = dbPdfData.map(SolutionSets::getFilePath)
            .orElseThrow(() -> new RuntimeException("File not found for the given ID: " + name));

    String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    String fileUrl = serverUrl + "/uploads/" + Paths.get(filePath).getFileName().toString();

    return fileUrl;
}


    public SolutionSets getSolutionSetById(long id){
        return solutionSetsRepository.findById(id).orElse(null);
    }
    @Autowired
    SolutionSetMapper solutionSetMapper;

    public List<SolutionSets> getLatestUploadedSolutionSets(){
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();
        List<SolutionSets> solutionSets1 = solutionSetsRepository.findAll().stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(SolutionSets::getDt1)))
                .limit(5)
                .toList();
        List<SolutionSetDto> solutionSetDtos = solutionSets1.stream().map(solutionSetMapper::toSolutionSetDto)
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
    public Category getSolutionSetsByCategoryName(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public List<SolutionSets> getByCategoryId(long id) {
        List<SolutionSets> solutionSets= solutionSetsRepository.findAll();
        return solutionSets.stream()
                .filter(solutionSet -> solutionSet.getCategory().getId()==id)
                .toList();
    }
}
