package com.shcheglov.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;


/**
 * Account
 *
 * @author Anton
 */
@Entity(name = "account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", initialValue = 1000)
    @JsonProperty
    private Long id;

    @NotNull
    @Length(min = 1, max = 256)
    @Column(name = "holder_name")
    @JsonProperty
    private String holderName;

    @NotNull
    @Column(name = "creation_time")
    @JsonProperty
    private LocalDateTime creationTime;

    @NotNull
    @DecimalMin("0")
    @Column(name = "balance")
    @JsonProperty
    private Long balance;

    @NotNull
    @Column(name = "active")
    @JsonProperty
    private Boolean active;

    @Length(max = 1024)
    @Column(name = "description")
    @JsonProperty
    private String description;

    public Account() {
        // NOOP
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("holderName='" + holderName + "'")
                .add("creationTime=" + creationTime)
                .add("balance=" + balance)
                .add("active=" + active)
                .add("description='" + description + "'")
                .toString();
    }

}

