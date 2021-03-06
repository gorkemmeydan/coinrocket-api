package com.gorkemmeydan.coinrocketapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    @ToString.Exclude
    private String password;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name="user_watchlist_id")
    private List<WatchList> watchList = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name="user_portfolio_id")
    private List<Portfolio> portfolio = new ArrayList<>();
}
