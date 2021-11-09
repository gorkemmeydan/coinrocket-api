package com.gorkemmeydan.coinrocketapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class AppUser {

    @Id
    @SequenceGenerator(name="seq_user", allocationSize = 1)
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="full_name",  nullable = false)
    private String fullName;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany
    @JoinColumn(name="user_watchlist_id")
    private List<WatchList> watchList;

    @OneToMany
    @JoinColumn(name="user_portfolio_id")
    private List<Portfolio> portfolio;
}
