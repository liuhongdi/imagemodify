package com.imagemodify.demo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imagemodify.demo.constant.Constant;
import com.imagemodify.demo.constant.ResponseCode;
import com.imagemodify.demo.exception.ServiceException;
import com.imagemodify.demo.pojo.ImageInfo;
import com.imagemodify.demo.service.ImageService;
import com.imagemodify.demo.util.ImageDirUtil;
import com.imagemodify.demo.util.ImageDownUtil;
import com.imagemodify.demo.util.ResponseUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/image")
public class ImageController {

    @Resource
    private ImageService imageService;

    //图片上传页面：静态，无参数
    @GetMapping("/imageadd")
    public String imageAdd(ModelMap model) {

        return "image/imageadd";
    }


    //查看图片:参数:图片的id
    @GetMapping("/imageview")
    public ResponseEntity<InputStreamResource> imageView(
            @RequestParam(value="imageid",required = true,defaultValue = "0") Long imageId) {

        if (imageId <= 0) {
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"缺少imageid参数");
        }

        ImageInfo imageInfo = imageService.getOneImageById(imageId);

        if (imageInfo == null) {
            throw new ServiceException(ResponseCode.DATA_NOT_EXIST.getCode(),ResponseCode.DATA_NOT_EXIST.getMsg());
        }

        String destImagePath= ImageDirUtil.getImageLocalPath(imageId,imageInfo.getImage_sn(),imageInfo.getImage_type(),"orig");

        File file = new File(destImagePath);
        if(!file.exists()){
            throw new ServiceException(ResponseCode.FILE_NOT_EXIST.getCode(),ResponseCode.FILE_NOT_EXIST.getMsg());
        }

        return ImageDownUtil.dispImageByLocalPath(destImagePath);
    }

    //下载图片,参数:图片id,response
    @GetMapping("/imagedown")
    public void imageDown(@RequestParam(value="imageid",required = true,defaultValue = "0") Long imageId,
                          HttpServletResponse response) {

        if (imageId <= 0) {
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"缺少imageid参数");
        }

        ImageInfo imageInfo = imageService.getOneImageById(imageId);

        if (imageInfo == null) {
            throw new ServiceException(ResponseCode.DATA_NOT_EXIST.getCode(),ResponseCode.DATA_NOT_EXIST.getMsg());
        }

        String destImagePath= ImageDirUtil.getImageLocalPath(imageId,imageInfo.getImage_sn(),imageInfo.getImage_type(),"orig");

        String imageName = imageId+"_"+imageInfo.getImage_sn()+"."+imageInfo.getImage_type();

        File file = new File(destImagePath);
        if(!file.exists()){
            throw new ServiceException(ResponseCode.DATA_NOT_EXIST.getCode(),ResponseCode.FILE_NOT_EXIST.getMsg());
        }

        ImageDownUtil.downImageByLocalPath(response,destImagePath,imageName);
    }

    //下载图片
    //参数:图片id,response
    @GetMapping("/imageedit")
    public String imageEdit(Model model,
                            @RequestParam(value="imageid",required = true,defaultValue = "0") Long imageId) {

        if (imageId <= 0) {
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"缺少imageid参数");
        }

        ImageInfo imageInfo = imageService.getOneImageById(imageId);

        if (imageInfo == null) {
            throw new ServiceException(ResponseCode.DATA_NOT_EXIST.getCode(),ResponseCode.DATA_NOT_EXIST.getMsg());
        }

        model.addAttribute("image",imageInfo);

        return "image/imageedit";
    }


    //@PostMapping("/categoryedited")
    //@ResponseBody

    @PostMapping("/imageedited")
    @ResponseBody
    public Object imageEdited(Model model,
                 @RequestParam(value="imageid",required = true,defaultValue = "0") Long imageId,
                 @RequestParam(value="title",required = true,defaultValue = "") String title
                 ) {

        if (imageId <= 0) {
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"缺少imageid参数");
        }
        if (title.equals("")) {
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"缺少title参数不可为空");
        }

        ImageInfo image= new ImageInfo();
        //image.setCategory_id(categoryId);
        image.setTitle(title);
        image.setImage_id(imageId);

        int upNum = imageService.setOneImage(image);

        //System.out.println("-----------------upnum:"+upNum);

        if (upNum <= 0) {
            throw new ServiceException(ResponseCode.DATA_UP_FAILED.getCode(),ResponseCode.DATA_UP_FAILED.getMsg());
        }

        return ResponseUtil.success();
    }


    //图片入库
    //参数：图片文件，图标的说明标题
    @PostMapping("/imageadded")
    @ResponseBody
    public Object imageAddEd(@RequestParam("file") MultipartFile file,
                             @RequestParam(value="title",required = true,defaultValue = "") String title) {

        if (file.isEmpty()){
            throw new ServiceException(ResponseCode.FILE_NOT_EXIST.getCode(),ResponseCode.FILE_NOT_EXIST.getMsg());
        }

        if (title.equals("")) {
            //return ResponseUtil.errorMsg("category_name cannot be empty");
            throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"title cannot be empty");
        }

        String imageUrl = imageService.addOneImage(file,title);

        Map<String, String> mapData = new HashMap<String, String>();
        mapData.put("imageurl", imageUrl);

        return ResponseUtil.success("",mapData);

        //return res;
    }

    //图片列表
    //参数: currentPage: 当前显示第几页
    @GetMapping("/imagelist")
    public String imageList(Model model,
           @RequestParam(value="currentPage",required = false,defaultValue = "1") Integer currentPage) {


        PageHelper.startPage(currentPage, Constant.IMAGES_PAGE_SIZE);

        //System.out.println("-------------------------categoryId:"+categoryId);

        List<ImageInfo> image_list = imageService.getAllImage();

        model.addAttribute("image_list",image_list);

        PageInfo<ImageInfo> pageInfo = new PageInfo<>(image_list);
        model.addAttribute("pageInfo", pageInfo);

        //model.addAttribute("category_id", categoryId);

        return "image/imagelist";

    }



}

