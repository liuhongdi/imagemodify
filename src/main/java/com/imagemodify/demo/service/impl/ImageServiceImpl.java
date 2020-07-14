package com.imagemodify.demo.service.impl;

import com.imagemodify.demo.constant.Constant;
//import com.imageadmin.demo.mapper.ImageMapper;
import com.imagemodify.demo.mapper.ImageServiceMapper;
import com.imagemodify.demo.pojo.ImageInfo;
import com.imagemodify.demo.service.ImageService;
import com.imagemodify.demo.util.ImageDirUtil;
import com.imagemodify.demo.util.ImageModifyUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageServiceMapper imageServiceMapper;

    //上传一张图片
    @Override
    public String addOneImage(MultipartFile file, String title) {
        // 文件名
        String fileName = file.getOriginalFilename();
        // 文件后缀
        int lastDot = fileName.lastIndexOf(".");
        lastDot++;
        String image_type = fileName.substring(lastDot);

        // 重新生成唯一文件名，避免被枚举方式抓取图片
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        int random = (int)(1000+Math.random()*(9999-1000+1));
        String image_sn = DigestUtils.md5Hex(dateString+"_"+random);

        Map<String, String> map2 = new HashMap<String, String>();
        //写入到数据库
        ImageInfo image = new ImageInfo();
        //image.setImage_id(imageId);
        image.setTitle(title);
        image.setImage_sn(image_sn);
        image.setImage_type(image_type);

        //图片得到大小/宽/高
        image.setSize(Long.toString(file.getSize()));

        int iconWidth = 0;
        int iconHeight = 0;

        try {
            BufferedImage imageBuffer = ImageIO.read(file.getInputStream());
            if (imageBuffer != null) {//如果image=null 表示上传的不是图片格式
                iconWidth = imageBuffer.getWidth();
                iconHeight = imageBuffer.getHeight();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setWidth(Integer.toString(iconWidth));
        image.setHeight(Integer.toString(iconHeight));

        //用户id,应该从session或spring security框架获取，此处的字串仅作为演示用
        String staff_id = "123";
        image.setStaff_id(staff_id);

        int insertNum = Integer.parseInt(imageServiceMapper.insertOneImage(image) + "");
        int image_id = image.getImage_id().intValue();//该对象的自增ID

        //创建文件
        String newFileName = Integer.toString(image_id)+image_sn+"."+image_type;
        //System.out.println("新的文件名： " + newFileName);
        String destImagePath= ImageDirUtil.getImageLocalPath(image_id,image_sn,image_type,"orig");
        String destTmbPath= ImageDirUtil.getImageLocalPath(image_id,image_sn,image_type,"tmb");

        File dest = new File(destImagePath);

        try {
            file.transferTo(dest);

            if(!dest.exists()){
            } else {
                //生成小图
                boolean make_tmb = ImageModifyUtil.image_resize_by_long_side(dest.getAbsolutePath(),destTmbPath, Constant.IMAGES_TMB_LONG,image_type);
            }

            map2.put("status", "0");
            map2.put("msg", "");
            map2.put("data", "");

            String img_url = ImageDirUtil.getImageWebPath(image_id,image_sn,image_type,"tmb");
            return img_url;
        } catch (IOException e) {
            //System.out.println("save ioexception");
            e.printStackTrace();
            return "file input error";
        }

    }

    //得到所有的图片
    @Override
    public List<ImageInfo> getAllImage() {
        List<ImageInfo> image_list = imageServiceMapper.selectAllImage();
        for (int i = 0; i < image_list.size(); i++) {
            ImageInfo s = (ImageInfo)image_list.get(i);
            String imageUrl = ImageDirUtil.getImageWebPath(s.getImage_id(),s.getImage_sn(),s.getImage_type(),"tmb");
            s.setImage_url(imageUrl);
            //写回
            image_list.set(i, s);
        }
        return image_list;
    }

    //得到一张图片的信息
    @Override
    public ImageInfo getOneImageById(Long image_id) {
        ImageInfo image_one = imageServiceMapper.selectOneImage(image_id);
        String imageUrl = ImageDirUtil.getImageWebPath(image_one.getImage_id(),image_one.getImage_sn(),image_one.getImage_type(),"tmb");
        image_one.setImage_url(imageUrl);
        return image_one;
    }

    //保存一张图片的信息
    @Override
    public int setOneImage(ImageInfo image) {
        int upNum = imageServiceMapper.updateOneImage(image);
        return upNum;
    }
}
