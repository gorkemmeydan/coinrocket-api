package com.gorkemmeydan.coinrocketapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "coin_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class CoinTransaction implements Serializable {

    @Id
    @SequenceGenerator(name="seq_coin_transaction", allocationSize = 1)
    @GeneratedValue(generator = "seq_coin_transaction", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "is_positive", nullable = false)
    private boolean isPositive;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name= "portfolio_coin_transaction_id")
    private Portfolio portfolio;
}
