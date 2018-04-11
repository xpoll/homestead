package cn.blmdz.test.blockchain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

public class BlockTest {

    public static void main(String[] args) {
//        Blockchain ccc = new Blockchain(5);
//        ccc.addBlock(new Block(1L, System.currentTimeMillis(), new BlockTransaction("127.0.0.1", "127.0.0.2", 20L), null, false));
//        ccc.addBlock(new Block(2L, System.currentTimeMillis(), new BlockTransaction("127.0.0.2", "127.0.0.3", 20L), null, false));
//        ccc.addBlock(new Block(3L, System.currentTimeMillis(), new BlockTransaction("127.0.0.3", "127.0.0.1", 20L), null, false));
//        System.out.println(ccc.isChainValid());
//        System.out.println(JSON.toJSONString(ccc.getChain()));
        
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        Blockchain ccc = new Blockchain(3);
        System.out.println(ccc.getBalanceOfAddress("本大爷"));
        ccc.minePendingTransactions("本大爷");
        System.out.println(ccc.getBalanceOfAddress("本大爷"));
        ccc.minePendingTransactions("本大爷");
        System.out.println(ccc.getBalanceOfAddress("本大爷"));
        ccc.minePendingTransactions("本大爷");
        System.out.println(ccc.getBalanceOfAddress("本大爷"));
        ccc.createTransaction(new BlockTransaction("本大爷", "啦啦啦", 20L));
        ccc.minePendingTransactions("本大爷");
        System.out.println(ccc.getBalanceOfAddress("本大爷"));
        System.out.println(ccc.getBalanceOfAddress("啦啦啦"));
        System.out.println(JSON.toJSONString(ccc.getChain()));
        
        
        
    }
}
