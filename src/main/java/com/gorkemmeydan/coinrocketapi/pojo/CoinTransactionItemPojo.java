package com.gorkemmeydan.coinrocketapi.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CoinTransactionItemPojo {
    private Long id;

    private String coinName;

    private boolean isPositive;

    private Double quantity;

    private Date transactionDate;
}
