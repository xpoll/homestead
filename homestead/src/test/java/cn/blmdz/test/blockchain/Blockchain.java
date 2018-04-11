package cn.blmdz.test.blockchain;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;

/**
 * 区块链
 */
public class Blockchain {
    /**
     * 链
     */
    @Getter
    private List<Block> chain;
    /**
     * 增加难度
     */
    private Integer difficulty;
    
    /**
     * 交易组
     */
    private List<BlockTransaction> pendingTransactions;
    
    /**
     * 挖矿奖励
     */
    private Long miningReward = 100L;

    /**
     * 创建默认主链
     */
    private Block createGenesisBlock() {
        return new Block(0L, System.currentTimeMillis(), Lists.newArrayList(), null, true);
    }
    
    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        this.pendingTransactions = Lists.newArrayList();
        chain = Lists.newArrayList();
        chain.add(this.createGenesisBlock());
    }
    
    /**
     * 获取最后一个块
     */
    private Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
    
//    /**
//     * 增加块
//     */
//    public void addBlock(Block block) {
//        block.setPreviousHash(getLatestBlock().getHash());
//        block.setHash(block.buildHash());
//        block.mineBlock(difficulty);
//        chain.add(block);
//    }
    
    /**
     * 增加交易
     */
    public void createTransaction(BlockTransaction transaction) {
        // 这里应该有一些校验! TODO
        
        // 推入待处理交易数组
        this.pendingTransactions.add(transaction);
    }
    
    /**
     * 挖矿
     */
    public void minePendingTransactions (String miningRewardAddress) {
        
        Block block = new Block(0L, System.currentTimeMillis(), this.pendingTransactions, null, false);
        block.setPreviousHash(getLatestBlock().getHash());
        block.setHash(block.buildHash());
        block.mineBlock(difficulty);
        chain.add(block);
        this.pendingTransactions = Lists.newArrayList();
        this.pendingTransactions.add(new BlockTransaction("系统账户", miningRewardAddress, miningReward));
    }
    
    /**
     * 获取地址金额
     * @param address
     * @return
     */
    public Long getBalanceOfAddress (String address) {
        Long balance = 0L;
        for (Block block : chain) {
            for (BlockTransaction data : block.getData()) {
                if (address.equals(data.getFromAddress())) balance -= data.getAmount();
                else if (address.equals(data.getToAddress())) balance += data.getAmount();
            }
        }
        return balance;
    }
    
    
    /**
     * 校验
     */
    public boolean isChainValid() {
        if (chain.size() == 1) return true;
        for (int i = 1; i < chain.size(); i++) {
            // 自己hash验证通过并且与上节点hash一致
            if (chain.get(i).getHash().equals(chain.get(i).buildHash()) && chain.get(i - 1).getHash().equals(chain.get(i).getPreviousHash())) continue;
            return false;
        }
        return true;
    }
}
