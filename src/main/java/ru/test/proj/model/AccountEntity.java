package ru.test.proj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UsersEntity user;

    @Column(name = "balance", precision = 7, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "init_balance", precision = 7, scale = 2, nullable = false)
    private BigDecimal initialBalance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

}