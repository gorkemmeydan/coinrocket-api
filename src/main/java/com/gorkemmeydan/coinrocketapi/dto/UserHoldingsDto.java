package com.gorkemmeydan.coinrocketapi.dto;

import com.gorkemmeydan.coinrocketapi.model.PortfolioItemPojo;
import com.gorkemmeydan.coinrocketapi.model.WatchListItemPojo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class UserHoldingsDto {
    private Long id;

    private String email;

    private List<WatchListItemPojo> watchList = new ArrayList<>();

    private List<PortfolioItemPojo> portfolio = new ArrayList<>();
}
