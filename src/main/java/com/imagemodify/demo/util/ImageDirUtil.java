package com.imagemodify.demo.util;

import com.imagemodify.demo.constant.Constant;
import java.io.File;

public class ImageDirUtil {
    //得到图片本地文件的访问路径
    public static String getImageLocalPath(long image_id,String image_sn,String image_type,String size_type) {
        String imageBaseDir = Constant.IMAGES_ORIG_DIR;
        if (size_type.equals("tmb")) {
            imageBaseDir = Constant.IMAGES_TMB_DIR;
        }
        String dir = getImageDir(image_id);
        File base_dir = new File(imageBaseDir+"/"+dir);
        if (base_dir.exists()) {
        } else {
            boolean isMake = base_dir.mkdirs();
        }
        String fullPath = imageBaseDir+"/"+dir+"/"+image_id+"_"+image_sn+"."+image_type;
        return fullPath;
    }

    //得到图片的web访问路径
    public static String getImageWebPath(long image_id,String image_sn,String image_type,String size_type) {
        String BaseUrl = Constant.IMAGES_URL_HOST;
        String imageBaseDir = BaseUrl+"/images";
        if (size_type.equals("tmb")) {
                imageBaseDir = BaseUrl+"/tmb";
        }
        String dir = getImageDir(image_id);
        String fullPath = imageBaseDir+"/"+dir+"/"+image_id+"_"+image_sn+"."+image_type;
        return fullPath;
    }

    //得到图片所属的数字目录，目标是方便管理
    public static String getImageDir(long image_id) {
        if (image_id < 1000) {
            return "1";
        }
        String imageDir = "";
        String idStr = String.valueOf(image_id);
        StringBuffer strRev = new StringBuffer(idStr).reverse();
        String[] strArr = str_split(new String(strRev),3);

        int size = strArr.length;
        String strFinal = "";
        for (int i = size-1; i >0; i -= 1) {
            StringBuffer curDir = new StringBuffer(strArr[i]).reverse();
            strFinal += new String(curDir);
            if (i>1) {
                strFinal += "/";
            }
        }
        imageDir = size+"/"+strFinal;
        return imageDir;
    }

    //把字符串按指定长度拆分到数组中
    private static String[] str_split(String str, int length) {
         int len = str.length();
         String[] arr = new String[(len + length - 1) / length];
         for (int i = 0; i < len; i += length) {
            int n = len - i;
           if (n > length)
            n = length;
            arr[i / length] = str.substring(i, i + n);
         }
         return arr;
    }
}
