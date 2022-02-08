package com.gorkemmeydan.coinrocketapi.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TrendingMarketItemPojo implements Serializable {
    private Integer id;
    private String imageSrc;
    private String coinName;
    private String coinSymbol;
}
