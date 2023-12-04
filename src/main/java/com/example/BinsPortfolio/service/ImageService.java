package com.example.BinsPortfolio.service;

import com.example.BinsPortfolio.entity.Image;
import com.example.BinsPortfolio.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    public Image saveImage(MultipartFile file, String imageLocation) throws IOException {
        Image image = new Image();

        System.out.println("saveImage");
        System.out.println(image);
        System.out.println(file);
        String oriImgName = file.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        System.out.println(oriImgName);

        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadImageFile(imageLocation, oriImgName, file);
            System.out.println(imgName);
            imgUrl = "/images/" + imageLocation + "/" + imgName;
        }

        image.updateImage(oriImgName, imgName, imgUrl);
        System.out.println(image);
        return imageRepository.save(image);
    }

    public void deleteImage(Image image, String imageLocation) {
        fileService.deleteFile(imageLocation+"/"+image.getImgName(), "image");
        imageRepository.delete(image);
    }

}
