package com.arjun.subjective.demo.fastdfs;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * FastFDS文件系统 - 控制器
 *
 * @author arjun
 * @date 2021/6/8
 */
@Slf4j
@Api(value = "附件文件的上传下载，预览", tags = "文件系统管理")
@RestController
@RequestMapping("fastDfs")
public class FastDfsController {

    /**
     * 文件格式
     */
    private static final String DOT = ".";
    private static final String JPG = "jpg";
    private static final String PNG = "png";
    private static final String PDF = "pdf";
    private static final String DOCX = "docx";
    private static final String DOC = "doc";
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final String AVI = "avi";
    private static final String MP4 = "mp4";

    @Autowired
    private FastDfsClient fastDfsClient;


    @ApiOperation(value = "单个文件上传", notes = "FastDFS实现单个文件的上传并返回文件路径")
    @ApiImplicitParam(name = "file", required = true, value = "选中的文件", paramType = "form")
    @PostMapping("uploadFile")
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        // 拿到文件名称
        String fileName = file.getOriginalFilename();
        log.info("上传文件为：{}", fileName);
        // 扩展名
        String extension = "";
        if (fileName.contains(DOT)) {
            extension = fileName.substring(fileName.lastIndexOf(".")).replace(".", "");
        }
        // 图片，生成缩略图
        if (JPG.equals(extension) || PNG.equals(extension)) {
            try {
                // 源图地址
                String imagePath = fastDfsClient.uploadImageAndCreateThumbImage(file);
                // 缩略图地址
                String thumbImagePath = fastDfsClient.getThumbImagePath(imagePath);
                // TODO：将地址持久化到数据库中，文件名加地址
                // 转换HTTP访问地址
                return fastDfsClient.getResAccessUrl(imagePath);
            } catch (IOException e) {
                log.error("{}，图片上传失败！", fileName);
                return "图片上传失败！";
            }
        }
        // pdf、word、excel格式
        if (PDF.equals(extension) || DOCX.equals(extension) || DOC.equals(extension) || XLS.equals(extension) || XLSX.equals(extension)) {
            try {
                String path = fastDfsClient.uploadFile(file);
                // TODO：将地址持久化到数据库中，文件名加地址
                // 转换HTTP访问地址
                return fastDfsClient.getResAccessUrl(path);
            } catch (IOException e) {
                log.error("{}，文件上传失败！", fileName);
                return "文件上传失败！";
            }
        }
        // 视频avi、mp4格式
        if (AVI.equals(extension) || MP4.equals(extension)) {
            try {
                String path = fastDfsClient.uploadFile(file);
                // TODO：将地址持久化到数据库中，文件名加地址
                // 转换HTTP访问地址
                return fastDfsClient.getResAccessUrl(path);
            } catch (IOException e) {
                log.error("{}，视频上传失败！", fileName);
                return "视频上传失败！";
            }
        }
        log.error("文件扩展名为：{}，仅支持pdf、word、excel、jpg、png、avi、mp4格式的文件上传！", extension);
        return "仅支持pdf、word、excel、jpg、png、avi、mp4格式的文件上传！";

    }


    @ApiOperation(value = "文件下载-路径", notes = "将文件通过服务器上存储地址，下载到本地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", required = true, value = "文件地址", paramType = "query")
    })
    @GetMapping("downloadFile")
    public void downloadFileByPath(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        if (StringUtils.isEmpty(filePath)) {
            log.info("下载地址为空！");
        }
        // 格式化下载路径，访问路径更换为存储地址，方便查询文件名
        filePath = StorePath.parseFromUrl(filePath).getFullPath();
        // 使用浏览器下载
        ServletOutputStream outputStream = null;
        try {
            // 下载
            byte[] bytes = fastDfsClient.downloadFile(filePath);
            // 先查询，获取文件名
            //TODO:通过存储地址从数据库中获取文件名
            String fileName = "测试文件.txt";
            // 清空response
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            if (bytes != null) {
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (IOException e) {
            log.error("下载文件输出流异常：{}", e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("下载文件关闭流异常：{}", e.getMessage());
            }
        }
    }

    @ApiOperation(value = "删除文件-路径", notes = "根据文件路径删除FastDFS上文件")
    @ApiImplicitParam(name = "filePath", required = true, value = "文件路径")
    @DeleteMapping("deleteFile")
    public String deleteFileByPath(@RequestParam("filePath") String filePath) {
        //TODO:同步删除数据库中持久化的地址
        fastDfsClient.deleteFile(filePath);
        return "删除成功！";
    }


    @PostMapping("/uploadFileAndFormData")
    public List<String> uploadFileAndFormData(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = null;
        // 先判断 httpRequest 是否含有文件类型
        if (request instanceof MultipartHttpServletRequest) {
            // 将 httpRequest 转换为 MultipartHttpServletRequest 类型
            multipartRequest = (MultipartHttpServletRequest) request;
        }
        if (multipartRequest != null) {
            // 先走附件上传，多任务同时上传，批量保存
            List<CompletableFuture<String>> uploadFutures = multipartRequest.getFiles("file").stream()
                    .map(file -> CompletableFuture.supplyAsync(() -> singleFileUpload(file))
                            .exceptionally(ex -> {
                                log.error(ex.getMessage());
                                // 捕获异常，空值放入集合
                                log.error("{}，上传失败！", file.getOriginalFilename());
                                return null;
                            })).collect(Collectors.toList());
            // 等待全部任务执行完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]));
            // 获取所有任务的结果
            CompletableFuture<List<String>> listCompletableFuture = allFutures
                    .thenApply(addr -> uploadFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
            List<String> filePaths = null;
            // 阻塞取值
            try {
                filePaths = listCompletableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("批量上传失败！{}", e.getMessage());
            }

            // 取出请求中的表单数据
            Map<String, Object> map = new HashMap<>();
            // 获取所有请求的参数名称
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                // 根据参数名获取参数值
                map.put(paramName, request.getParameter(paramName));
            }
            // TODO：关联数据，入库表单信息
            //转UserInfoEntity对象
            //UserInfoEntity user = JSON.parseObject(JSON.toJSONString(map), UserInfoEntity.class);
            return filePaths;
        }
        return null;
    }

}