import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  employeeId = '';
  message = '';

  onLogin() {
    if (this.employeeId) {
      this.message = `Field worker ${this.employeeId} logged in. Use attendance page to record the day.`;
    } else {
      this.message = 'Enter your employee ID before login.';
    }
  }
}
