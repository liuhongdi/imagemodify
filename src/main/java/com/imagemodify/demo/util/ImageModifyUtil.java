package com.imagemodify.demo.util;

import com.imagemodify.demo.constant.Constant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageModifyUtil {
    //按指定的边长生成缩略图
    public static boolean image_resize_by_long_side(String orig_path, String dest_path, String long_size,String imageType) {
        try {
        List<String> command = new ArrayList<>();
         String command_one = "";
        if (imageType.equals("gif")) {
            //计算得到目标宽高
            File gifFile = new File(orig_path);
            int gifWidth = 0;
            int gifHeight = 0;

            try {
                BufferedImage imageBuffer = ImageIO.read(gifFile);
                if (imageBuffer != null) {//如果image=null 表示上传的不是图片格式
                    gifWidth = imageBuffer.getWidth();
                    gifHeight = imageBuffer.getHeight();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int destWidth = 0;
            int destHeight = 0;
            if (gifWidth > gifHeight) {
                destWidth = Integer.parseInt( long_size );
                destHeight = (destWidth*gifHeight) / gifWidth;
            } else if  (gifWidth == gifHeight) {
                destWidth = Integer.parseInt( long_size );
                destHeight = destWidth;
            } else {
                destHeight = Integer.parseInt( long_size );
                destWidth = (destHeight*gifWidth) / gifHeight;
            }
            command_one = Constant.FFMPEG_CMD+" -i "+orig_path+" -s "+destWidth+"x"+destHeight+" "+dest_path+" 2>&1";
        } else {
            command_one = Constant.IMAGEMAGICK_DIR+"/convert -size "+long_size+"x"+long_size+" -resize "+long_size+"x"+long_size+" +profile '*' -quality 85 "+orig_path+" "+dest_path+" 2>&1";
        }

        System.out.println(command_one);
        command.add("sh");
        command.add("-c");
        command.add(command_one);

        // 执行cmd命令
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        Process process = builder.start();
        return true;
        } catch (Exception e) {
            System.out.println("save ioexception");
            e.printStackTrace();
            return false;
        }
    }

}
