package com.gorkemmeydan.coinrocketapi.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "portfolio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Portfolio implements Serializable {

    @Id
    @SequenceGenerator(name="seq_portfolio", allocationSize = 1)
    @GeneratedValue(generator = "seq_portfolio", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "coin_name", nullable = false)
    private String coinName;

    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private AppUser appUser;

    @OneToMany
    @JoinColumn(name= "portfolio_coin_transaction_id")
    private List<CoinTransaction> coinTransactions;
}
