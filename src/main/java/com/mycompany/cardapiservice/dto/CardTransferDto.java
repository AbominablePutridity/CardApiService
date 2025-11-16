package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.CardTransfer;
import java.time.LocalDate;

/**
 *
 */
public class CardTransferDto {
    private Long id;
    private CardDto senderDto;
    private CardDto receiverDto;
    private Long amountOfMoney;
    private String description;
    private boolean isTransfered;
    private LocalDate transferDate;
    
    public CardTransferDto(CardTransfer cardTransfer)
    {
        id = cardTransfer.getId();
        senderDto = new CardDto(cardTransfer.getSender());
        receiverDto = new CardDto(cardTransfer.getReceiver());
        amountOfMoney = cardTransfer.getAmountOfMoney();
        description = cardTransfer.getDescription();
        isTransfered = cardTransfer.getIsTransfered();
        transferDate = cardTransfer.getTransferDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardDto getSenderDto() {
        return senderDto;
    }

    public void setSenderDto(CardDto senderDto) {
        this.senderDto = senderDto;
    }

    public CardDto getReceiverDto() {
        return receiverDto;
    }

    public void setReceiverDto(CardDto receiverDto) {
        this.receiverDto = receiverDto;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Long amountOfMoney) {
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
