package com.emptracker.service;

import com.emptracker.model.Quotation;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuotationPdfService {

    private final QuotationService quotationService;

    @Value("${quotation.company.address:}")
    private String companyAddress;

    @Value("${quotation.company.phone:}")
    private String companyPhone;

    @Value("${quotation.company.email:}")
    private String companyEmail;

    @Value("${quotation.company.pan:}")
    private String companyPan;

    @Value("${quotation.terms.2:}")
    private String term2;

    @Value("${quotation.terms.3:}")
    private String term3;

    @Value("${quotation.terms.4:}")
    private String term4;

    @Value("${quotation.terms.5:}")
    private String term5;

    @Value("${quotation.terms.6:}")
    private String term6;

    @Value("${quotation.payment.1:}")
    private String payment1;

    @Value("${quotation.payment.2:}")
    private String payment2;

    @Value("${quotation.payment.3:}")
    private String payment3;

    @Value("${quotation.payment.4:}")
    private String payment4;

    public QuotationPdfService(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @Transactional(readOnly = true)
    public byte[] generate(Long quotationId) throws Exception {

        Quotation quotation = quotationService.get(quotationId);

        ClassPathResource resource =
                new ClassPathResource("reports/quotation.jrxml");

        try (InputStream input = resource.getInputStream()) {

            JasperReport report =
                    JasperCompileManager.compileReport(input);

            Map<String, Object> params = new HashMap<>();

            // =========================================
            // COMPANY DETAILS
            // =========================================

            params.put("companyAddress", companyAddress);
            params.put("companyPhone", companyPhone);
            params.put("companyEmail", companyEmail);
            params.put("companyPan", companyPan);

            // =========================================
            // QUOTATION DETAILS
            // =========================================

            params.put(
                    "quotationNo",
                    quotation.getQuotationNo()
            );

            params.put(
                    "quotationDate",
                    quotation.getQuotationDate()
                            .format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
            );

            params.put(
                    "clientName",
                    quotation.getClientName()
            );

            // =========================================
            // DYNAMIC TO & FRO CHARGES
            // =========================================

            BigDecimal toAndFroCharges =
                    quotation.getToAndFroCharges();

            String toAndFroText = "";

            if (toAndFroCharges != null) {

                DecimalFormat decimalFormat =
                        new DecimalFormat("#,##0.##");

                toAndFroText =
                        "To and fro charges @ Rs."
                                + decimalFormat.format(toAndFroCharges)
                                + "/- per technician you have to pay.";
            }

            params.put(
                    "toAndFroCharges",
                    toAndFroText
            );

            // =========================================
            // OTHER TERMS & CONDITIONS
            // =========================================

            params.put("term2", term2);
            params.put("term3", term3);
            params.put("term4", term4);
            params.put("term5", term5);
            params.put("term6", term6);

            // =========================================
            // PAYMENT TERMS
            // =========================================

            params.put("payment1", payment1);
            params.put("payment2", payment2);
            params.put("payment3", payment3);
            params.put("payment4", payment4);

            // =========================================
            // DYNAMIC BANK DETAILS
            // =========================================

            params.put(
                    "bankAccountHolderName",
                    quotation.getBankAccountHolderName()
            );

            params.put(
                    "bankAccountNo",
                    quotation.getBankAccountNo()
            );

            params.put(
                    "bankName",
                    quotation.getBankName()
            );

            params.put(
                    "bankBranch",
                    quotation.getBankBranch()
            );

            params.put(
                    "bankIfscCode",
                    quotation.getBankIfscCode()
            );

            params.put(
                    "companyName",
                    quotation.getComanyName()
            );
            // =========================================
            // QUOTATION ITEMS
            // =========================================

            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(
                            quotation.getItems()
                    );

            // =========================================
            // GENERATE PDF
            // =========================================

            JasperPrint print =
                    JasperFillManager.fillReport(
                            report,
                            params,
                            dataSource
                    );

            return JasperExportManager.exportReportToPdf(print);
        }
    }
}