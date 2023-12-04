package com.example.BinsPortfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    @Value("${imagePath}")
    private String defaultImagePath;
    @Value("${pdfPath}")
    private String defaultPdfPath;

    public void createFolder(Path directoryPath) {
        try {
            // 디렉토리 생성
            Files.createDirectories(directoryPath);

            System.out.println(directoryPath + " 디렉토리가 생성되었습니다.");

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String uploadImageFile(String path, String originalFileName, MultipartFile file) throws IOException {
        byte[] fileData = file.getBytes();
        Path path1 = Paths.get(defaultImagePath + path);
        System.out.println(defaultImagePath+path);

        createFolder(path1);

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = defaultImagePath + path + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath, String type) {

        File deleteFile = new File((type.equals("image") ? defaultImagePath : defaultPdfPath)+filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제 했습니다.");
        } else {
            log.info(filePath + "파일이 존재하지 않습니다.");
        }
    }
}
