package com.imagemodify.demo.service;

import com.imagemodify.demo.pojo.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    //public int addOneImage(ImageInfo image);
    public String addOneImage(MultipartFile file,String title);
    public List<ImageInfo> getAllImage();
    public ImageInfo getOneImageById(Long image_id);
    public int setOneImage(ImageInfo image);
}
