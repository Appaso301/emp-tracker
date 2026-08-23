package com.emptracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class QuotationRequest {

    private String clientName;

    private LocalDate quotationDate;

    private BigDecimal toAndFroCharges;

    private String bankAccountHolderName;

    private String bankAccountNo;

    private String bankName;

    private String bankBranch;

    private String bankIfscCode;
    
    private String companyName;

    private List<QuotationItemRequest> items;


    // =========================
    // GETTERS / SETTERS
    // =========================

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }


    public BigDecimal getToAndFroCharges() {
        return toAndFroCharges;
    }

    public void setToAndFroCharges(BigDecimal toAndFroCharges) {
        this.toAndFroCharges = toAndFroCharges;
    }


    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }


    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }


    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }
 
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<QuotationItemRequest> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemRequest> items) {
        this.items = items;
    }
}