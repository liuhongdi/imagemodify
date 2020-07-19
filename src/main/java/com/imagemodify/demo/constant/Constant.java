package com.imagemodify.demo.constant;

public class Constant {
    //cankao
    public static final Integer YES = 1;
    public static final Integer NO = 0;
    public static final int defaultPageSize = 10;

    //图片从nginx访问时的host
    public static final String IMAGES_URL_HOST = "http://127.0.0.1:81";
    //默认原始图片的路径
    public static final String IMAGES_ORIG_DIR = "/data/file/html/images";
  //缩略图的文件路径
    public static final String IMAGES_TMB_DIR = "/data/file/html/tmb";
    //缩略图的长边长度
    public static final String IMAGES_TMB_LONG = "300";
    //分页显示时每页的显示数量
    public static final int IMAGES_PAGE_SIZE = 5;
    //ImageMagick命令的安装路径
    public static final String IMAGEMAGICK_DIR = "/usr/bin";
    //ffmpeg的完整路径
    public static final String FFMPEG_CMD = "/usr/bin/ffmpeg";

}
