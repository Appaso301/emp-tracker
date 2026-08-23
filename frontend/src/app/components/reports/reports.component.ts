import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  employees: any[] = [];
  locations: any[] = [];

  selectedEmployeeId: number | null = null;
  selectedLocationId: number | null = null;

  startDate = '';
  endDate = '';

  employeeReport: any[] = [];
  locationReport: any[] = [];

  // Total net pay for selected location
  totalLocationNetPay: number = 0;

  consolidatedReport: any = null;

  constructor(private api: ApiService) {}

  ngOnInit(): void {

    this.api.getEmployees().subscribe(
      (list: any[]) => this.employees = list
    );

    this.api.getLocations().subscribe(
      (list: any[]) => this.locations = list
    );
  }

  loadEmployeeReport() {

    if (
      !this.selectedEmployeeId ||
      !this.startDate ||
      !this.endDate
    ) {
      return;
    }

    this.api.getEmployeeReport(
      this.selectedEmployeeId,
      this.startDate,
      this.endDate
    ).subscribe(
      report => this.employeeReport = report
    );
  }

  loadLocationReport() {

    if (
      !this.selectedLocationId ||
      !this.startDate ||
      !this.endDate
    ) {
      return;
    }

    this.api.getLocationReport(
      this.selectedLocationId,
      this.startDate,
      this.endDate
    ).subscribe((response: any) => {

      // Employee records
      this.locationReport = response.employees;

      // Total net pay
      this.totalLocationNetPay = response.totalNetPay;

    });
  }

  loadConsolidatedReport() {

    if (!this.startDate || !this.endDate) {
      return;
    }

    this.api.getConsolidatedReport(
      this.startDate,
      this.endDate
    ).subscribe(
      report => this.consolidatedReport = report
    );
  }
  
  downloadLocationReportPdf(): void {

  if (!this.locationReport || this.locationReport.length === 0) {
    return;
  }

  const doc = new jsPDF();

  // Title
  doc.setFontSize(18);
  doc.text('Location Report', 14, 20);

  // Location name
  const locationName =
    this.locationReport[0]?.locationName || 'Location';

  doc.setFontSize(11);
  doc.text(`Location: ${locationName}`, 14, 28);

  // Date range
  doc.text(
    `Date: ${this.startDate} to ${this.endDate}`,
    14,
    35
  );

  // Table data
  const tableData = this.locationReport.map(row => [
    row.employeeName,
    row.totalHours,
    row.totalOvertime,
    row.advances,
    row.grossPay,
    row.netPay
  ]);

  // Add total row
  tableData.push([
    '',
    '',
    '',
    '',
    '',
    `Total Net Pay: ${this.totalLocationNetPay}`
  ]);

  autoTable(doc, {
    startY: 42,

    head: [[
      'Employee',
      'Number Of Days',
      'Overtime',
      'Advance',
      'Gross Pay',
      'Net Pay'
    ]],

    body: tableData,

    theme: 'grid',

    styles: {
      fontSize: 9,
      cellPadding: 3
    },

    headStyles: {
      fontStyle: 'bold'
    },

    columnStyles: {
      0: { cellWidth: 40 },
      1: { cellWidth: 25 },
      2: { cellWidth: 25 },
      3: { cellWidth: 25 },
      4: { cellWidth: 30 },
      5: { cellWidth: 35 }
    },

    didParseCell: function (data) {

      // Last row = Total Net Pay
      if (
        data.section === 'body' &&
        data.row.index === tableData.length - 1
      ) {
        data.cell.styles.fontStyle = 'bold';
      }
    }
  });

  // Download PDF
  doc.save(
    `Location-Report-${locationName}-${this.startDate}-to-${this.endDate}.pdf`
  );
}
}