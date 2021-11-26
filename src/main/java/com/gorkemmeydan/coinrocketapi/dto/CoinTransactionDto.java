package com.gorkemmeydan.coinrocketapi.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class CoinTransactionDto {
    private Long id;

    private String email;

    @NotNull
    private String coinName;

    @NotNull
    private boolean isPositive;

    @NotNull
    private Double quantity;

    @NotNull
    private long unixTransactionDate;

    public boolean getisPositive() {
        return isPositive;
    }

    public void setisPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }
}
