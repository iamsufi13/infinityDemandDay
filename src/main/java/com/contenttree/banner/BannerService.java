package com.contenttree.banner;

import com.contenttree.utils.ApiResponse1;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        byte[] fileBytes;

        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }

        BannerEntity bannerEntity = BannerEntity.builder()
                .name(fileName)
                .bannerType(file.getContentType())
                .filePath(fileBytes)
                .build();

        bannerRepository.save(bannerEntity);

        return "File uploaded successfully: " + fileName;
    }

    public byte[] downloadImage(String fileName) {
        Optional<BannerEntity> dbImageData = bannerRepository.findByName(fileName);

        if (dbImageData.isPresent()) {
            return dbImageData.get().getFilePath();
        }

        return null;
    }

    public byte[] downloadPdf(String fileName) {
        Optional<BannerEntity> dbPdfData = bannerRepository.findByName(fileName);

        if (dbPdfData.isPresent()) {
            return dbPdfData.get().getFilePath();
        }

        return null;
    }


    public List<BannerEntity> getAllBanners() {
        return bannerRepository.findAll();
    }

    public BannerEntity getById(long id) {
        return bannerRepository.findById(id).orElse(null);
    }

    public void deleteBannerById(long id) {
        bannerRepository.deleteById(id);
    }
}
