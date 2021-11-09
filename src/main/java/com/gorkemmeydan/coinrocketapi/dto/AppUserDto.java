package com.gorkemmeydan.coinrocketapi.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class AppUserDto {

    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private String password;

}
