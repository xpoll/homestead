package cn.blmdz.test.blockchain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockTransaction {
    /**
     * 源账户地址
     */
    private String fromAddress;
    /**
     * 目的账户地址
     */
    private String toAddress;
    /**
     * 交易金额
     */
    private Long amount;
}
