package com.arjun.subjective.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

/**
 * 处理视频，将视频提取成帧图片.
 *
 * @author arjun
 */
@Slf4j
public class VideoProcessing {

    /**
     * 存放截取视频某一帧的图片
     */
    public static String videoFramesPath = "D:/test";

    /**
     * 将视频文件帧处理并以“jpg”格式进行存储。 依赖FrameToBufferedImage方法：将frame转换为bufferedImage对象
     *
     * @param path
     */
    public static byte[] grabberVideoFramer(String path) {
        // Frame对象
        Frame frame = null;
        // 标识
        int flag = 0;
        try {
            // 获取视频文件
            URL url = new URL(path);
            // 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(inputStream);
            fFmpegFrameGrabber.start();
            // 视频旋转角度，可能是null
            String rotate_old = fFmpegFrameGrabber.getVideoMetadata("rotate");

            // 获取视频总帧数
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
            System.out.println("时长 " + ftp / fFmpegFrameGrabber.getFrameRate() / 60);

            while (flag <= ftp) {
                frame = fFmpegFrameGrabber.grabImage();
                // 对视频的第五帧进行处理，避免出现全黑的图片，依自己情况而定
                if (frame != null && flag == 5) {
                    BufferedImage bufferedImage = frameToBufferedImage(frame);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", out);
                    byte[] b = out.toByteArray();

                    fFmpegFrameGrabber.stop();
                    // 有需要旋转
                    if (rotate_old != null && !rotate_old.isEmpty()) {
                        int rotate = Integer.parseInt(rotate_old);
                        VideoProcessing.rotatePhonePhoto(bufferedImage, rotate);
                    }
                    fFmpegFrameGrabber.close();
                    return b;
                }
                flag++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage frameToBufferedImage(Frame frame) {
        // 创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = converter.getBufferedImage(frame);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            converter.close();
        }
        return bufferedImage;
    }

    /**
     * 旋转照片
     *
     * @return
     */
    public static String rotatePhonePhoto(BufferedImage bufferedImage, int angel) {
        BufferedImage src;
        try {
            src = bufferedImage;
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            int swidth = src_width;
            int sheight = src_height;
            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }
            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res, "jpg", new File("D:/test/90.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 测试： 1、在D盘中新建一个test文件夹，test中再分成video和img，在video下存入一个视频，并命名为test D:/test/video D:/test/img
     *
     * @param args
     */
    public static void main(String[] args) {
//        byte[] bytes = grabberVideoFramer("http://172.16.205.240:11935/group1/M00/00/07/rBEABV-PiJiEFeyOAAAAAAAAAAA149.mp4");
//        System.out.println(bytes);


        File file = new File("C:\\Users\\Lee\\Desktop\\fastdfs\\WeChat_20210629143055.mp4");
        fetchPicture(file, "C:\\Users\\Lee\\Desktop\\fastdfs\\image");


    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param file      源视频文件
     * @param framePath 截取帧的图片存放路径
     */
    public static void fetchPicture(File file, String framePath) {
        try {
//            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(new FileInputStream(file));
            ff.start();
            int length = ff.getLengthInFrames();
            log.info("总帧数：{}", length);
            File targetFile = new File(framePath);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            File frameFile = new File(targetFile.getPath() + "\\" + UUID.randomUUID().toString() + ".jpg");
            int i = 0;
            Frame frame = null;
            while (i < length) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                frame = ff.grabFrame();
                if ((i > 100) && (frame.image != null)) {
                    break;
                }
                i++;
            }

            String imgSuffix = "jpg";
            if (framePath.indexOf('.') != -1) {
                String[] arr = framePath.split("\\.");
                log.info("数组：{}", Arrays.toString(arr));
                if (arr.length >= 2) {
                    imgSuffix = arr[1];
                }
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcBi = converter.getBufferedImage(frame);

            int owidth = srcBi.getWidth();
            int oheight = srcBi.getHeight();
            log.info("宽：{}，高：{}", owidth, oheight);
            // 对截取的帧进行等比例缩放
            int width = 800;
            int height = (int) (((double) width / owidth) * oheight);
            log.info("缩放后的高度：{}", height);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            try {
                ImageIO.write(bi, imgSuffix, frameFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            .close();
        }

    }



    /**
     * 获取视频时长，单位为秒
     *
     * @param video 源视频文件
     * @return 时长（s）
     */
    public static long getVideoDuration(File video) {
        long duration = 0L;
        FFmpegFrameGrabber ff = null;
        try {
            ff = new FFmpegFrameGrabber(video);
            ff.start();
            duration = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ff != null) {
                try {
                    ff.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return duration;
    }
}