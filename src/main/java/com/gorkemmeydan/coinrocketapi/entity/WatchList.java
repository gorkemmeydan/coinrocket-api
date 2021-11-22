package com.gorkemmeydan.coinrocketapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "watchlist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class WatchList implements Serializable {

    @Id
    @SequenceGenerator(name="seq_watchlist", allocationSize = 1)
    @GeneratedValue(generator = "seq_watchlist", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "coin_name", nullable = false)
    private String coinName;

    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "user_watchlist_id")
    private AppUser appUser;
}
