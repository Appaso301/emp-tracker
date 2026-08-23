package com.emptracker.dto;

import java.math.BigDecimal;

public class ReportSummary {

    private Long employeeId;
    private String employeeName;
    private String locationName;
    private Double totalHours;
    private Double totalOvertime;
    private BigDecimal grossPay;
    private BigDecimal advances;
    private BigDecimal expenses;
    private BigDecimal travelCharges;
    private BigDecimal netPay;

    public ReportSummary() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(Double totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }

    public BigDecimal getAdvances() {
        return advances;
    }

    public void setAdvances(BigDecimal advances) {
        this.advances = advances;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getTravelCharges() {
        return travelCharges;
    }

    public void setTravelCharges(BigDecimal travelCharges) {
        this.travelCharges = travelCharges;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }
}
