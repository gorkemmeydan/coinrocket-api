package com.gorkemmeydan.coinrocketapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PortfolioItemPojo {
    private Long id;

    private String coinName;

    private double[] lastWeeksHoldings = new double[7];

    private List<CoinTransactionItemPojo> coinTransactions = new ArrayList<>();

    public void addLastWeeksHoldingsByIndex(int index, double val) {
        if (index > 6) return;

        lastWeeksHoldings[index] += val;
    }
}
