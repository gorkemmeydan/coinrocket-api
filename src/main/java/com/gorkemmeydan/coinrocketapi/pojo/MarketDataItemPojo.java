package com.gorkemmeydan.coinrocketapi.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MarketDataItemPojo implements Serializable {
    private String id;
    private Integer market_cap_rank;
    private String image;
    private String name;
    private String symbol;
    private Double current_price;
    private Double price_change_24h;
    private Double price_change_percentage_1h_in_currency;
    private Double price_change_percentage_24h_in_currency;
    private Double price_change_percentage_7d_in_currency;
    private Double market_cap;
    private List<Double> sparkline_in_7d = new ArrayList<Double>();
}
