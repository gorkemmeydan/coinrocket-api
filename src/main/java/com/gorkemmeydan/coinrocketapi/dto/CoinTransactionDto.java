package com.gorkemmeydan.coinrocketapi.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class CoinTransactionDto {
    private Long id;

    @NotNull
    private boolean isPositive;

    @NotNull
    private Double quantity;

    @NotNull
    private Date transactionDate;
}
