package com.emptracker.dto;

import java.math.BigDecimal;
import java.util.List;

public class LocationReportResponse {

    private List<ReportSummary> employees;
    private BigDecimal totalNetPay;

    public LocationReportResponse() {
    }

    public LocationReportResponse(
            List<ReportSummary> employees,
            BigDecimal totalNetPay) {
        this.employees = employees;
        this.totalNetPay = totalNetPay;
    }

    public List<ReportSummary> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ReportSummary> employees) {
        this.employees = employees;
    }

    public BigDecimal getTotalNetPay() {
        return totalNetPay;
    }

    public void setTotalNetPay(BigDecimal totalNetPay) {
        this.totalNetPay = totalNetPay;
    }
}