package com.emptracker.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotation")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quotation_no", nullable = false, unique = true, length = 50)
    private String quotationNo;

    @Column(name = "client_name", nullable = false, length = 255)
    private String clientName;

    @Column(name = "quotation_date", nullable = false)
    private LocalDate quotationDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /*
     * ================================
     * TO AND FRO CHARGES
     * ================================
     */
    @Column(name = "to_and_fro_charges", precision = 15, scale = 2)
    private BigDecimal toAndFroCharges;

    /*
     * ================================
     * BANK DETAILS
     * ================================
     */

    @Column(name = "bank_account_holder_name", length = 255)
    private String bankAccountHolderName;

    @Column(name = "bank_account_no", length = 100)
    private String bankAccountNo;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "bank_branch", length = 255)
    private String bankBranch;

    @Column(name = "bank_ifsc_code", length = 50)
    private String bankIfscCode;
    
    @Column(name = "compnay_name",length = 50)
    private String companyName;

    /*
     * ================================
     * QUOTATION ITEMS
     * ================================
     */

    @OneToMany(
            mappedBy = "quotation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuotationItem> items = new ArrayList<>();


    /*
     * ================================
     * JPA CALLBACK
     * ================================
     */

    @PrePersist
    void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (quotationDate == null) {
            quotationDate = LocalDate.now();
        }
    }


    /*
     * ================================
     * GETTERS / SETTERS
     * ================================
     */

    public Long getId() {
        return id;
    }


    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }


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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    /*
     * ================================
     * TO AND FRO CHARGES
     * ================================
     */

    public BigDecimal getToAndFroCharges() {
        return toAndFroCharges;
    }

    public void setToAndFroCharges(BigDecimal toAndFroCharges) {
        this.toAndFroCharges = toAndFroCharges;
    }


    /*
     * ================================
     * BANK DETAILS
     * ================================
     */

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
    
    public String getComanyName() {
        return companyName;
    }
    
    public void setComanyName(String companyName ) {
        this.companyName = companyName;
    }


    /*
     * ================================
     * ITEMS
     * ================================
     */

    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }
}