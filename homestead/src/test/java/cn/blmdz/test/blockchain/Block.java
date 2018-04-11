package cn.blmdz.test.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * 块
 */
@Data
public class Block {
    /**
     * 序号
     */
    private Long index;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 交易数据
     */
    private List<BlockTransaction> data;
    /**
     * 上一节点hash
     */
    private String previousHash;
    /**
     * 工作量
     */
    private Long nonce;
    /**
     * 本节点hash
     */
    private String hash;

    public Block(Long index, Long timestamp, List<BlockTransaction> data, String previousHash, boolean h) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.nonce = 0L;
        if (h) this.hash = buildHash();
    }
    
    /**
     * hash
     */
    public String buildHash() {
        return SHA(this.index + this.previousHash + this.timestamp + JSON.toJSONString(this.data) + this.nonce, "SHA-256");
    }

    /**
     * 计算hash
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();
                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    
    /**
     * 工作量机制
     */
    public boolean mineBlock (Integer difficulty) {
        if (difficulty <= 0 ) return true;
        String key = String.format("%0" + (difficulty) + "d", 0);
        while(!key.equals(this.hash.substring(0, difficulty))) {
            this.nonce ++;
            this.hash = buildHash();
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        System.out.println(JSON.toJSONString(this));
        return true;
    }
}
