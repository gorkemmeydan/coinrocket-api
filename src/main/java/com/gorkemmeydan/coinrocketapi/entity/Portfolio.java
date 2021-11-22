package com.gorkemmeydan.coinrocketapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Portfolio implements Serializable {

    @Id
    @SequenceGenerator(name="seq_portfolio", allocationSize = 1)
    @GeneratedValue(generator = "seq_portfolio", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "coin_name", nullable = false)
    private String coinName;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_portfolio_id")
    private AppUser appUser;

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name= "portfolio_coin_transaction_id")
    private List<CoinTransaction> coinTransactions = new ArrayList<>();
}
