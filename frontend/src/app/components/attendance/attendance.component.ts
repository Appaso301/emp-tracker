import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  employees: any[] = [];
  selectedEmployeeId: number | null = null;
  checkIn = '';
  checkOut = '';
  overtimeHours = 0;
  message = '';
  uploadMessage = '';
  selectedFile: File | null = null;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getEmployees().subscribe((list: any[]) => this.employees = list);
  }

  onFileChanged(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files && input.files.length > 0 ? input.files[0] : null;
  }

  uploadAttendanceWorkbook(): void {
    if (!this.selectedFile) {
      this.uploadMessage = 'Please choose an Excel workbook first.';
      return;
    }

    this.api.uploadAttendanceWorkbook(this.selectedFile).subscribe((resp: any) => {
      const count = resp?.rowsImported ?? resp?.rowsImported ?? 0;
      this.uploadMessage = 'Excel rows imported successfully: ' + count;
      this.selectedFile = null;
    }, (error: any) => {
      const backendMessage = error?.error?.message || 'Unable to upload the workbook.';
      this.uploadMessage = backendMessage;
    });
  }

  submitAttendance() {
    if (!this.selectedEmployeeId || !this.checkIn || !this.checkOut) {
      this.message = 'Please select employee and enter both check-in and check-out times.';
      return;
    }

    const checkInDate = new Date(this.checkIn);
    const checkOutDate = new Date(this.checkOut);
    const hoursWorked = Math.max((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60), 0);
    const normalHours = Math.min(hoursWorked, 8);
    const overtime = Math.max(hoursWorked - normalHours, 0);

    const payload = {
      employeeId: this.selectedEmployeeId,
      checkIn: this.checkIn,
      checkOut: this.checkOut,
      hoursWorked: Number(hoursWorked.toFixed(2)),
      overtimeHours: Number((this.overtimeHours || overtime).toFixed(2))
    };

    this.api.saveAttendance(payload).subscribe(() => {
      this.message = 'Attendance saved successfully.';
      this.checkIn = '';
      this.checkOut = '';
      this.overtimeHours = 0;
    }, () => {
      this.message = 'Unable to save attendance. Please verify the values.';
    });
  }
}
