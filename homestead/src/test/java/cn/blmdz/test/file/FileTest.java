package cn.blmdz.test.file;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import cn.blmdz.aide.file.ImageServer;
import cn.blmdz.aide.file.aliyun.AliyunImageServer;

public class FileTest {

    public static ImageServer imageServer() {

        return new AliyunImageServer("http://oss-cn-shanghai.aliyuncs.com", "LTAIOucdAfA5jDC1",
                "3Cx6UXWh1bb8wnYK2D1hti7jYhYHeX", "xpoll");
    }

    public static void main(String[] args) {
        // ImageServer image = imageServer();
        // String path = image.write("home/123/logo.png", new File("c:\\logo.png"));
        // System.out.println(path);
        System.out.println(getFileMD5(new File("c:\\spring-mini-2.0.1.jar")));
    }

    // 计算文件的 MD5 值
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");// MD5 SHA-1
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) digest.update(buffer, 0, len);
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
