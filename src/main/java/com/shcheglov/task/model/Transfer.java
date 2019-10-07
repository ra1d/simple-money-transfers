package com.shcheglov.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Transfer
 *
 * @author Anton
 */
@Entity(name = "transfer")
public class Transfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_seq")
    @SequenceGenerator(name = "transfer_seq", sequenceName = "transfer_seq", initialValue = 1000)
    @JsonProperty
    private Long id;

    @NotNull
    @Column(name = "creation_time")
    @JsonProperty
    private LocalDateTime creationTime;

    @Valid
    @NotNull
    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "from_account", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_from_account_id"))
    @JsonProperty
    private Account fromAccount;

    @Valid
    @NotNull
    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "to_account", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_to_account_id"))
    @JsonProperty
    private Account toAccount;

    @NotNull
    @DecimalMin("1")
    @Column(name = "amount")
    @JsonProperty
    private Long amount;

    @Length(max = 1024)
    @Column(name = "description")
    @JsonProperty
    private String description;

    public Transfer() {
        // NOOP
    }

    public Transfer(final LocalDateTime creationTime, final Account fromAccount, final Account toAccount, final Long amount, final String description) {
        this.creationTime = creationTime;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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
        Transfer transfer = (Transfer) o;
        return Objects.equals(id, transfer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transfer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("creationTime=" + creationTime)
                .add("fromAccount=" + fromAccount)
                .add("toAccount=" + toAccount)
                .add("amount=" + amount)
                .add("description='" + description + "'")
                .toString();
    }

}
