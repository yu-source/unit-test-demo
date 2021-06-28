package com.arjun.subjective.demo.fastdfs;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * FastDFS客户端包装类.
 *
 * @author arjun
 * @date 2021/6/9
 */
@Slf4j
@Component
public class FastDfsClient {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * 文件上传.
     *
     * @param file MultipartFile文件对象
     * @return 返回上传完成以后的文件路径
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 参1 文件输入流,参2 文件大小,参3 文件拓展名,参4 元数据集合
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }

    /**
     * 上传文件.
     *
     * @param file File文件对象
     * @return 文件访问地址
     */
    public String uploadFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        StorePath storePath = storageClient.uploadFile(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), null);
        return getResAccessUrl(storePath);
    }

    /**
     * 将一段字符串生成一个文件上传.
     *
     * @param content   文件内容
     * @param extension 文件扩展名
     * @return 文件访问地址
     */
    public String uploadFile(String content, String extension) {
        byte[] buff = content.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, extension, null);
        return getResAccessUrl(storePath);
    }

    /**
     * 文件上传.
     *
     * @param bytes     文件字节
     * @param fileSize  文件大小
     * @param extension 文件扩展名
     * @return 返回文件路径（卷名和文件名）
     */
    public String uploadFile(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        // 元数据
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("dateTime", LocalDateTime.now().toString()));
        StorePath storePath = storageClient.uploadFile(byteInputStream, fileSize, extension, metaDataSet);
        return getResAccessUrl(storePath);
    }


    /**
     * 上传图片并同时生成一个缩略图.
     *
     * @param file 图片文件对象
     * @return 文件路径
     */
    public String uploadImageAndCreateThumbImage(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }

    /**
     * 根据文件名获取缩略图路径.
     *
     * @param filePath 文件路径
     * @return 缩略图路径
     */
    public String getThumbImagePath(String filePath) {
        return thumbImageConfig.getThumbImagePath(filePath);
    }

    /**
     * 根据文件路径进行下载.
     *
     * @param filePath 文件路径
     * @return 返回一个byte[]数组 包含文件内容
     */
    public byte[] downloadFile(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), IOUtils::toByteArray);
    }

    /**
     * 根据文件路径，删除文件.
     *
     * @param filePath 文件路径
     */
    public void deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            log.info("文件路径为空...");
            return;
        }
        StorePath storePath = StorePath.parseFromUrl(filePath);
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    /**
     * 封装图片完整URL地址.
     *
     * @param storePath 文件路径
     * @return 完整HTTP访问地址
     */
    private String getResAccessUrl(StorePath storePath) {
        return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
    }

    /**
     * 封装图片完整URL地址.
     *
     * @param storePath String类型文件路径
     * @return 完整HTTP访问地址
     */
    public String getResAccessUrl(String storePath) {
        return fdfsWebServer.getWebServerUrl() + storePath;
    }
}
