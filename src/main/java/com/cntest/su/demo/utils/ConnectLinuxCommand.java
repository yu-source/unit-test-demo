package com.cntest.su.demo.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.cntest.su.demo.entity.RemoteConnect;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 连接linux服务器并执行相关的shell命令.
 *
 * @author arjun
 */
@Component
public class ConnectLinuxCommand {

    private static final Logger logger = Logger.getLogger(ConnectLinuxCommand.class);

    private static String DEFAULTCHARTSET = "UTF-8";

    private static Connection conn;

    /**
     * 静态注入
     */
    private static RemoteConnect remoteConnect;

    @Autowired
    public void setRemoteConnect(RemoteConnect remoteConnect) {
        ConnectLinuxCommand.remoteConnect = remoteConnect;
    }


    public static void main(String[] args) {
//        boolean flg = false;
//        try {
//            conn = new Connection(ip);
//            // 连接
//            conn.connect();
//            //判断身份是否已经认证
//            if (!conn.isAuthenticationComplete()) {
//                //加锁，防止多线程调用时线程间判断不一致，导致出现重复认证
//                synchronized (this) {
//                    if (!conn.isAuthenticationComplete()) {
//                        //进行身份认证
//                        flg = conn.authenticateWithPassword(userName, userPwd);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
//        return flg;
    }


    public static void main1(String[] args) {
        String hostname = "192.168.192.128";
        String username = "root";
        String password = "root";

        try {
            Connection conn = new Connection(hostname);
            conn.connect();
            //进行身份认证
            boolean isAuthenticated = conn.authenticateWithPassword(
                    username, password);
            if (isAuthenticated == false) {
                throw new IOException("Authentication failed.");
            }
            //开启一个Session
            Session sess = conn.openSession();
            //执行具体命令
            sess.execCommand("cat haha.txt");
            //获取返回输出
            InputStream stdout = new StreamGobbler(sess.getStdout());
            //返回错误输出
            InputStream stderr = new StreamGobbler(sess.getStderr());
            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(
                    new InputStreamReader(stderr));

            System.out.println("Here is the output from stdout:");
            while (true) {
                String line = stdoutReader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }

            System.out.println("Here is the output from stderr:");
            while (true) {
                String line = stderrReader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            //关闭Session
            sess.close();
            //关闭Connection
            conn.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }


// ----------------------------------工具方法-----------------------------------------------------------------------------------------


    /**
     * 用户名密码方式  远程登录linux服务器.
     *
     * @param remoteConnect 服务器信息
     * @return Boolean
     */
    public static Boolean login(RemoteConnect remoteConnect) {
        boolean flag = false;
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();// 连接
            flag = conn.authenticateWithPassword(remoteConnect.getUsername(), remoteConnect.getPassword());// 认证
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 秘钥方式  远程登录linux服务器.
     *
     * @param remoteConnect 服务器信息
     * @param keyFile       一个文件对象指向一个文件，该文件包含OpenSSH密钥格式的用户的DSA或RSA私钥
     *                      (PEM，不能丢失"-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"标签
     * @param keyFilePass   如果秘钥文件加密 需要用该参数解密，如果没有加密可以为null
     * @return Boolean
     */
    public static Boolean loginByFileKey(RemoteConnect remoteConnect, File keyFile, String keyFilePass) {
        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUsername(), keyFile, keyFilePass);
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 秘钥方式  远程登录linux服务器.
     *
     * @param remoteConnect 服务器信息
     * @param keys          一个字符[]，其中包含用户的DSA或RSA私钥(OpenSSH密匙格式，
     *                      您不能丢失“----- begin DSA私钥-----”或“-----BEGIN RSA PRIVATE KEY-----“标签。char数组可以包含换行符/换行符。
     * @param keyPass       如果秘钥字符数组加密  需要用该字段解密  否则不需要可以为null
     * @return Boolean
     */
    public static Boolean loginByCharsKey(RemoteConnect remoteConnect, char[] keys, String keyPass) {
        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUsername(), keys, keyPass);
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 远程执行shll脚本或者命令.
     *
     * @param cmd 脚本命令
     * @return 命令执行完毕返回结果
     */
    public static String execute(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            //使用多个命令用分号";"隔开
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            // 如果为得到标准输出为空，说明脚本执行出错了
            if (StringUtils.isBlank(result)) {
                result = processStdout(session.getStderr(), DEFAULTCHARTSET);
            }
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 远程执行shell脚本或者命令.
     *
     * @param cmd shell脚本或者命令
     * @return 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     */
    public static String executeSuccess(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            conn.close();
            //连接的Session和Connection对象都需要关闭
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 解析脚本执行的返回结果
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     * 通过用户名和密码关联linux服务器.
     *
     * @param ip
     * @param userName
     * @param password
     * @param commandStr
     * @return
     */
    public static boolean connectLinux(String ip, String userName, String password, String commandStr) {

        logger.info("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  commandStr:"
                + commandStr);

        String returnStr = "";
        boolean result = true;
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUsername(userName);
        remoteConnect.setPassword(password);
        try {
            if (login(remoteConnect)) {
                returnStr = execute(commandStr);
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(returnStr)) {
            result = false;
        }
        return result;
    }


    /**
     * 从其他服务器获取文件到本服务器指定目录.
     *
     * @param ip         ip(其他服务器)
     * @param userName   用户名(其他服务器)
     * @param password   密码(其他服务器)
     * @param remoteFile 文件位置(其他服务器)
     * @param localDir   本服务器目录
     * @throws IOException
     */
    public static void scpGet(String ip, String userName, String password, String remoteFile, String localDir)
            throws IOException {

        logger.info("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  remoteFile:"
                + remoteFile + "  localDir:" + localDir);
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUsername(userName);
        remoteConnect.setPassword(password);
        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            //fixme: client.get(remoteFile, localDir);
            conn.close();
        }
    }


    /**
     * 将文件复制到其他计算机中
     *
     * @param ip
     * @param userName
     * @param password
     * @param localFile
     * @param remoteDir
     * @throws IOException
     */
    public static void scpPut(String ip, String userName, String password, String localFile, String remoteDir)
            throws IOException {
        logger.info("ConnectLinuxCommand  scpPut===" + "ip:" + ip + "  userName:" + userName + "  localFile:"
                + localFile + "  remoteDir:" + remoteDir);
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUsername(userName);
        remoteConnect.setPassword(password);
        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            // fixme: client.put(localFile, remoteDir);
            conn.close();
        }
    }


    // -----------------------------------------------------------------------------------------

}
