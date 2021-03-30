package com.arjun.subjective.demo.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 在java.net包中，已经提供访问HTTP协议的基本功能类：HttpURLConnection，可以向其他系统发送GET，POST访问请求.
 * https://www.cnblogs.com/zjshd/p/9149643.html
 *
 * @author arjun
 * @date 2021/02/01
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * GET方式调用.
     */
    private void httpURLGETCase() {
        String methodUrl = "http://110.32.44.11:8086/sp-test/usertest/1.0/query";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        try {
            URL url = new URL(methodUrl + "?mobile=15334567890&name=zhansan");
            connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
            connection.setRequestMethod("GET");// 默认GET请求
            connection.connect();// 建立TCP连接
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));// "\n"
                }
                System.out.println(result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
    }

    /**
     * POST方式调用.
     * 带授权的传递json格式参数调用.
     */
    private static void httpURLPOSTCase() {
        String methodUrl = "http://xxx.xxx.xx.xx:8280/xx/adviserxx/1.0 ";
        HttpURLConnection connection = null;
        OutputStream dataout = null;
        BufferedReader reader = null;
        String line = null;
        try {
            URL url = new URL(methodUrl);
            connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
            connection.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
            connection.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
            connection.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
            connection.setUseCaches(false);// post请求不能使用缓存设为false
            connection.setConnectTimeout(3000);// 连接主机的超时时间
            connection.setReadTimeout(3000);// 从主机读取数据的超时时间
            connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
            connection.setRequestProperty("charset", "utf-8");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer 66cb225f1c3ff0ddfdae31rae2b57488aadfb8b5e7");
            connection.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

            dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
            String body = "[{\"orderNo\":\"44921902\",\"adviser\":\"张怡筠\"}]";
            dataout.write(body.getBytes());
            dataout.flush();
            dataout.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));//
                }
                System.out.println(result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
    }

    /**
     * POST方式调用.
     * 传递键值对的参数.
     */
//    URL url = new URL(getUrl);
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setUseCaches(false);
//            connection.connect();
//
//    String body = "userName=zhangsan&password=123456";
//    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//            writer.write(body);
//            writer.close();


    /**
     * 在post请求上传文件.
     */
//    try{
//        URL url = new URL(getUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.setUseCaches(false);
//        connection.setRequestProperty("Content-Type", "file/*");//设置数据类型
//        connection.connect();
//
//        OutputStream outputStream = connection.getOutputStream();
//        FileInputStream fileInputStream = new FileInputStream("file");//把文件封装成一个流
//        int length = -1;
//        byte[] bytes = new byte[1024];
//        while ((length = fileInputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, length);//写的具体操作
//        }
//        fileInputStream.close();
//        outputStream.close();
//
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            InputStream inputStream = connection.getInputStream();
//            String result = is2String(inputStream);//将流转换为字符串。
//            Log.d("kwwl", "result=============" + result);
//        }
//
//    } catch (Exception e){
//        e.printStackTrace();
//    }

    /**
     * 上传到服务器的数据除了键值对数据和文件数据外，还有其他字符串，使用这些这些字符串来拼接一定的格式。
     *
     * 那么我们只要模拟这个数据，并写入到Http请求中便能实现同时传递参数和文件。
     */
//    try {
//
//        String BOUNDARY = java.util.UUID.randomUUID().toString();
//        String TWO_HYPHENS = "--";
//        String LINE_END = "\r\n";
//
//        URL url = new URL(URLContant.CHAT_ROOM_SUBJECT_IMAGE);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.setUseCaches(false);
//
//        //设置请求头
//        connection.setRequestProperty("Connection", "Keep-Alive");
//        connection.setRequestProperty("Charset", "UTF-8");
//        connection.setRequestProperty("Content-Type","multipart/form-data; BOUNDARY=" + BOUNDARY);
//        connection.setRequestProperty("Authorization","Bearer "+UserInfoConfigure.authToken);
//        connection.connect();
//
//        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//        StringBuffer strBufparam = new StringBuffer();
//        //封装键值对数据一
//        strBufparam.append(TWO_HYPHENS);
//        strBufparam.append(BOUNDARY);
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Disposition: form-data; name=\"" + "groupId" + "\"");
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Type: " + "text/plain" );
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Lenght: "+(""+groupId).length());
//        strBufparam.append(LINE_END);
//        strBufparam.append(LINE_END);
//        strBufparam.append(""+groupId);
//        strBufparam.append(LINE_END);
//
//        //封装键值对数据二
//        strBufparam.append(TWO_HYPHENS);
//        strBufparam.append(BOUNDARY);
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Disposition: form-data; name=\"" + "title" + "\"");
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Type: " + "text/plain" );
//        strBufparam.append(LINE_END);
//        strBufparam.append("Content-Lenght: "+"kwwl".length());
//        strBufparam.append(LINE_END);
//        strBufparam.append(LINE_END);
//        strBufparam.append("kwwl");
//        strBufparam.append(LINE_END);
//
//        //拼接完成后，一块写入
//        outputStream.write(strBufparam.toString().getBytes());
//
//
//        //拼接文件的参数
//        StringBuffer strBufFile = new StringBuffer();
//        strBufFile.append(LINE_END);
//        strBufFile.append(TWO_HYPHENS);
//        strBufFile.append(BOUNDARY);
//        strBufFile.append(LINE_END);
//        strBufFile.append("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + file.getName() + "\"");
//        strBufFile.append(LINE_END);
//        strBufFile.append("Content-Type: " + "image/*" );
//        strBufFile.append(LINE_END);
//        strBufFile.append("Content-Lenght: "+file.length());
//        strBufFile.append(LINE_END);
//        strBufFile.append(LINE_END);
//
//        outputStream.write(strBufFile.toString().getBytes());
//
//        //写入文件
//        FileInputStream fileInputStream = new FileInputStream(file);
//        byte[] buffer = new byte[1024*2];
//        int length = -1;
//        while ((length = fileInputStream.read(buffer)) != -1){
//            outputStream.write(buffer,0,length);
//        }
//        outputStream.flush();
//        fileInputStream.close();
//
//        //写入标记结束位
//        byte[] endData = (LINE_END + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();//写结束标记位
//        outputStream.write(endData);
//        outputStream.flush();
//
//        //得到响应
//        int responseCode = connection.getResponseCode();
//        if(responseCode == HttpURLConnection.HTTP_OK){
//            InputStream inputStream = connection.getInputStream();
//            String result = is2String(inputStream);//将流转换为字符串。
//            Log.d("kwwl","result============="+result);
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }

    /**
     * 示例
     *
     * @param card
     * @param methodUrl
     * @param fileBytes
     * @param file_id
     * @return
     */
    private static String imageIdentify(String card, String methodUrl, byte[] fileBytes, String file_id) {
        HttpURLConnection connection = null;
        OutputStream dataout = null;
        BufferedReader bf = null;
        String BOUNDARY = "----WebKitFormBoundary2NYA7hQkjRHg5WJk";
        String END_DATA = ("\r\n--" + BOUNDARY + "--\r\n");
        String BOUNDARY_PREFIX = "--";
        String NEW_LINE = "\r\n";
        try {
            URL url = new URL(methodUrl + "?card=" + card);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setDoOutput(true);// 设置连接输出流为true,默认false
            connection.setDoInput(true);// 设置连接输入流为true
            connection.setRequestMethod("POST");// 设置请求方式为post
            connection.setUseCaches(false);// post请求缓存设为false
            connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            connection.connect();// 建立连接

            dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
            StringBuilder sb2 = new StringBuilder();
            sb2.append(BOUNDARY_PREFIX);
            sb2.append(BOUNDARY);
            sb2.append(NEW_LINE);
            sb2.append("Content-Disposition:form-data; name=\"type\"");
            // 参数头设置完成后需要2个换行，才是内容
            sb2.append(NEW_LINE);
            sb2.append(NEW_LINE);
            sb2.append("0");
            sb2.append(NEW_LINE);
            dataout.write(sb2.toString().getBytes());

            // 读取文件上传到服务器
            StringBuilder sb1 = new StringBuilder();
            sb1.append(BOUNDARY_PREFIX);
            sb1.append(BOUNDARY);
            sb1.append(NEW_LINE);
            sb1.append("Content-Disposition:form-data; name=\"file\";filename=\"" + file_id + "\"");//文件名必须带后缀
            sb1.append(NEW_LINE);
            sb1.append("Content-Type:application/octet-stream");
            // 参数头设置完成后需要2个换行，才是内容
            sb1.append(NEW_LINE);
            sb1.append(NEW_LINE);
            dataout.write(sb1.toString().getBytes());
            dataout.write(fileBytes);// 写文件字节

            dataout.write(NEW_LINE.getBytes());
            dataout.write(END_DATA.getBytes());
            dataout.flush();
            dataout.close();

            bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 连接发起请求,处理服务器响应
            String line;
            StringBuilder result = new StringBuilder(); // 用来存储响应数据
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                result.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();
            connection.disconnect(); // 销毁连接
            return result.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }


    /**
     * 从服务器下载文件是比较简单的操作，只要得到输入流，就可以从流中读出数据。使用示例如下：
     */
//    try {
//        String urlPath = "https://www.baidu.com/";
//        URL url = new URL(urlPath);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//        int responseCode = connection.getResponseCode();
//        if(responseCode == HttpURLConnection.HTTP_OK){
//            InputStream inputStream = connection.getInputStream();
//            File dir = new File("fileDir");
//            if (!dir.exists()){
//                dir.mkdirs();
//            }
//            File file = new File(dir, "fileName");//根据目录和文件名得到file对象
//            FileOutputStream fos = new FileOutputStream(file);
//            byte[] buf = new byte[1024*8];
//            int len = -1;
//            while ((len = inputStream.read(buf)) != -1){
//                fos.write(buf, 0, len);
//            }
//            fos.flush();
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }

    /**
     * 测试页面路径.
     */
    public static void methodUrlTest() {
        String url10 = "https://blog.csdn.net/arjun_yu/article/details/115294665";
        String url09 = "https://blog.csdn.net/arjun_yu/article/details/115253517";
        String url08 = "https://blog.csdn.net/arjun_yu/article/details/113522919";
        String url07 = "https://blog.csdn.net/arjun_yu/article/details/113520371";
        String url06 = "https://blog.csdn.net/arjun_yu/article/details/113520317";
        String url05 = "https://blog.csdn.net/arjun_yu/article/details/112993354";
        String url04 = "https://blog.csdn.net/arjun_yu/article/details/112675985";
        String url03 = "https://blog.csdn.net/arjun_yu/article/details/110930574";
        String url02 = "https://blog.csdn.net/arjun_yu/article/details/110916988";
        String url01 = "https://blog.csdn.net/arjun_yu/article/details/107612205";

        List<String> urls = Lists.newArrayList(url01, url02, url03, url04, url05, url06, url07, url08, url09, url10);

        urls.forEach(url -> {
            Executors.newFixedThreadPool(6).execute(() -> {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String line = null;
                try {
                    System.out.println("正在连接......");
                    System.out.println(url);
                    URL u = new URL(url);
                    // 根据URL生成HttpURLConnection
                    connection = (HttpURLConnection) u.openConnection();
                    // 默认GET请求
                    connection.setRequestMethod("GET");
                    // 建立TCP连接
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 发送http请求
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//                        // 循环读流
//                        StringBuilder result = new StringBuilder();
//                        line = reader.readLine();
//                        while (line != null) {
//                            // "\n"
//                            result.append(line).append(System.getProperty("line.separator"));
//                        }
//                        System.out.println(result.toString());
                        System.out.println("访问完成......");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                connection.disconnect();
            });
        });
    }

//    public static void main(String[] args) {
//        Executors.newScheduledThreadPool(3).scheduleWithFixedDelay(() -> {
//            methodUrlTest();
//        }, 0, 2, TimeUnit.MINUTES);
//    }

}
