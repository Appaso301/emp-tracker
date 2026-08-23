import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.css']
})
export class FinanceComponent implements OnInit {
  employees: any[] = [];
  selectedEmployeeId: number | null = null;
  amount = '';
  date = '';
  note = '';
  category = 'General';
  fromLocation = '';
  toLocation = '';
  message = '';

  expenseCategories = ['General', 'Meal', 'Fuel', 'Travel', 'Medical', 'Other'];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getEmployees().subscribe((list: any[]) => this.employees = list);
    this.date = new Date().toISOString().split('T')[0];
  }

  submitAdvance(): void {
    if (!this.selectedEmployeeId || !this.amount || !this.date) {
      this.message = 'Please choose an employee, add an amount, and set a date before saving an advance.';
      return;
    }

    const payload = {
      employeeId: this.selectedEmployeeId,
      amount: Number(this.amount),
      date: this.date,
      note: this.note || 'Advance entry'
    };

    this.api.saveAdvance(payload).subscribe(() => {
      this.message = 'Advance saved successfully.';
      this.resetForm();
    }, () => {
      this.message = 'Unable to save the advance entry.';
    });
  }

  submitExpense(): void {
    if (!this.selectedEmployeeId || !this.amount || !this.date) {
      this.message = 'Please choose an employee, add an amount, and set a date before saving an expense.';
      return;
    }

    const payload = {
      employeeId: this.selectedEmployeeId,
      amount: Number(this.amount),
      date: this.date,
      category: this.category,
      note: this.note || 'Expense entry'
    };

    this.api.saveExpense(payload).subscribe(() => {
      this.message = 'Expense saved successfully.';
      this.resetForm();
    }, () => {
      this.message = 'Unable to save the expense entry.';
    });
  }

  submitTravel(): void {
    if (!this.selectedEmployeeId || !this.amount || !this.date || !this.fromLocation || !this.toLocation) {
      this.message = 'Please provide employee, amount, date, source, and destination for the travel charge.';
      return;
    }

    const payload = {
      employeeId: this.selectedEmployeeId,
      amount: Number(this.amount),
      date: this.date,
      fromLocation: this.fromLocation,
      toLocation: this.toLocation,
      note: this.note || 'Travel charge'
    };

    this.api.saveTravel(payload).subscribe(() => {
      this.message = 'Travel charge saved successfully.';
      this.resetForm();
    }, () => {
      this.message = 'Unable to save the travel charge.';
    });
  }

  private resetForm(): void {
    this.amount = '';
    this.note = '';
    this.category = 'General';
    this.fromLocation = '';
    this.toLocation = '';
  }
}
