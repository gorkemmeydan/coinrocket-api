package com.gorkemmeydan.coinrocketapi.dto;

import com.sun.istack.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class WatchlistDto {

    private Long id;

    @NotNull
    private String coinName;
}
