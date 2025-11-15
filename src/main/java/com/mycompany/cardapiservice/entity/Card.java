package com.mycompany.cardapiservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность карты
 */
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "number")
    private String number;
    
    @Column(name = "validity_period")
    private LocalDate validityPeriod;
    
    @Column(name = "balance")
    private Long balance;
    
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne()
    @JoinColumn(name = "status_card_id")
    private StatusCard statusCard;
    
    @ManyToOne()
    @JoinColumn(name = "currency_id")
    private Currency currency;
    
    @Column(name = "is_blocked")
    private boolean isBlocked = false;
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<CardTransfer> cardSenderTransfers = new ArrayList<>();
    
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<CardTransfer> cardReceiverTransfers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(LocalDate validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StatusCard getStatusCard() {
        return statusCard;
    }

    public void setStatusCard(StatusCard statusCard) {
        this.statusCard = statusCard;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    } 

    public boolean getIsIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public List<CardTransfer> getCardSenderTransfers() {
        return cardSenderTransfers;
    }

    public void setCardSenderTransfers(List<CardTransfer> cardSenderTransfers) {
        this.cardSenderTransfers = cardSenderTransfers;
    }

    public List<CardTransfer> getCardReceiverTransfers() {
        return cardReceiverTransfers;
    }

    public void setCardReceiverTransfers(List<CardTransfer> cardReceiverTransfers) {
        this.cardReceiverTransfers = cardReceiverTransfers;
    }
}
