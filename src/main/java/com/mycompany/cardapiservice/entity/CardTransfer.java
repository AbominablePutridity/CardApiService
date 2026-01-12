package com.mycompany.cardapiservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "card_transfer")
public class CardTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne()
    @JoinColumn(name = "sender_card_id")
    private Card sender;
    
    @ManyToOne()
    @JoinColumn(name = "receiver_card_id")
    private Card receiver;
    
    @Column(name = "amount_of_money")
    private BigDecimal amountOfMoney;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_transfered")
    private boolean isTransfered;
    
    @Column(name = "transfer_date")
    private LocalDate transferDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getSender() {
        return sender;
    }

    public void setSender(Card sender) {
        this.sender = sender;
    }

    public Card getReceiver() {
        return receiver;
    }

    public void setReceiver(Card receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(BigDecimal amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsTransfered() {
        return isTransfered;
    }

    public void setIsTransfered(boolean isTransfered) {
        this.isTransfered = isTransfered;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }
}
