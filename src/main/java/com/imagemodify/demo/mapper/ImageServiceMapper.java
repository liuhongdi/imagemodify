package com.imagemodify.demo.mapper;

import com.imagemodify.demo.pojo.ImageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface ImageServiceMapper {
    int insertOneImage(ImageInfo image);
    List<ImageInfo> selectAllImage();
    ImageInfo selectOneImage(Long image_id);
    int updateOneImage(ImageInfo image);
}
