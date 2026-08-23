import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

interface QuotationRate {
  id: number;
  description: string;
  rate: number;
  unit: string;
  selected: boolean;
}

@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css']
})
export class QuotationComponent implements OnInit {

  clientName = '';
  quotationDate = '';

  // ==========================
  // DYNAMIC TERMS
  // ==========================

  toAndFroCharges: number | null = 1500;

  // ==========================
  // BANK DETAILS
  // ==========================

  bankAccountHolderName = '';
  bankAccountNo = '';
  bankName = '';
  bankBranch = '';
  bankIfscCode = '';
  companyName = '';

  // ==========================
  // QUOTATION ITEMS
  // ==========================

  rates: QuotationRate[] = [];

  message = '';
  loading = false;

  constructor(private api: ApiService) {}

  ngOnInit(): void {

    this.quotationDate =
      new Date().toISOString().split('T')[0];

    this.loadRates();
  }

  loadRates(): void {

    this.api.getQuotationRates().subscribe({

      next: (rates: any[]) => {

        this.rates = rates.map(rate => ({
          ...rate,
          selected: true
        }));
      },

      error: () => {

        this.message =
          'Unable to load standard quotation rates.';
      }
    });
  }

  generateQuotation(): void {

    this.message = '';

    // ==========================
    // VALIDATE CLIENT
    // ==========================

    if (!this.clientName.trim()) {

      this.message =
        'Please enter client name.';

      return;
    }

    // ==========================
    // VALIDATE TO & FRO
    // ==========================

    if (
      this.toAndFroCharges === null ||
      this.toAndFroCharges < 0 ||
      Number.isNaN(this.toAndFroCharges)
    ) {

      this.message =
        'Please enter valid To & Fro charges.';

      return;
    }

    // ==========================
    // VALIDATE BANK DETAILS
    // ==========================

    if (!this.bankAccountHolderName.trim()) {

      this.message =
        'Please enter bank account holder name.';

      return;
    }

    if (!this.bankAccountNo.trim()) {

      this.message =
        'Please enter bank account number.';

      return;
    }

    if (!this.bankName.trim()) {

      this.message =
        'Please enter bank name.';

      return;
    }

    if (!this.bankBranch.trim()) {

      this.message =
        'Please enter bank branch.';

      return;
    }

    if (!this.bankIfscCode.trim()) {

      this.message =
        'Please enter IFSC code.';

      return;
    }

    if (!this.companyName.trim()) {

      this.message =
        'Please enter company name.';

      return;
    } 

    // ==========================
    // SELECTED ITEMS
    // ==========================

    const selectedItems = this.rates
      .filter(rate => rate.selected)
      .map(rate => ({
        description: rate.description,
        rate: Number(rate.rate),
        unit: rate.unit
      }));

    if (!selectedItems.length) {

      this.message =
        'Please select at least one quotation item.';

      return;
    }

    // ==========================
    // VALIDATE RATES
    // ==========================

    if (
      selectedItems.some(
        item =>
          item.rate < 0 ||
          Number.isNaN(item.rate)
      )
    ) {

      this.message =
        'Please enter valid rates.';

      return;
    }

    // ==========================
    // START GENERATION
    // ==========================

    this.loading = true;

    const quotationRequest = {

      clientName:
        this.clientName.trim(),

      quotationDate:
        this.quotationDate,

      // Dynamic To & Fro
      toAndFroCharges:
        Number(this.toAndFroCharges),

      // Dynamic Bank Details
      bankAccountHolderName:
        this.bankAccountHolderName.trim(),

      bankAccountNo:
        this.bankAccountNo.trim(),

      bankName:
        this.bankName.trim(),

      bankBranch:
        this.bankBranch.trim(),

      bankIfscCode:
        this.bankIfscCode.trim(),

      companyName:
        this.companyName.trim(),

      // Items
      items:
        selectedItems
    };

    // ==========================
    // CREATE QUOTATION
    // ==========================

    this.api.createQuotation(
      quotationRequest
    ).subscribe({

      next: response => {

        this.api.downloadQuotationPdf(
          response.id
        ).subscribe({

          next: blob => {

            const url =
              window.URL.createObjectURL(blob);

            const anchor =
              document.createElement('a');

            anchor.href = url;

            anchor.download =
              `${response.quotationNo}.pdf`;

            anchor.click();

            window.URL.revokeObjectURL(url);

            this.message =
              `Quotation ${response.quotationNo} generated successfully.`;

            this.loading = false;
          },

          error: error => {

            console.error(error);

            this.message =
              'Quotation was saved, but PDF generation failed.';

            this.loading = false;
          }
        });
      },

      error: error => {

        console.error(error);

        this.message =
          'Unable to create quotation.';

        this.loading = false;
      }
    });
  }
}