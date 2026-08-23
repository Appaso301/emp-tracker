import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  // =====================================================
  // DASHBOARD DATA
  // =====================================================

  employeeCount = 0;
  locationCount = 0;
  loading = true;

  employees: any[] = [];
  locations: any[] = [];

  filteredEmployees: any[] = [];

  employeeSearch = '';

  showEmployeeTable = false;


  // =====================================================
  // EMPLOYEE FORM
  // =====================================================

  addEmployeeForm = {
    name: '',
    mobile: '',
    locationId: null as number | null,
    dailyRate: 0,
    hourlyRate: 1,
    overtimeRate: 1,
    active: true
  };


  // =====================================================
  // LOCATION FORM
  // =====================================================

  addLocationForm = {
    name: '',
    address: ''
  };


  // =====================================================
  // MESSAGES
  // =====================================================

  submitMessage = '';
  locationSubmitMessage = '';


  // =====================================================
  // EDIT MODE
  // =====================================================

  isEditMode = false;

  editingEmployeeId: number | null = null;


  // =====================================================
  // CONSTRUCTOR
  // =====================================================

  constructor(private api: ApiService,  private snackBar: MatSnackBar) {}
  showSnackBar(
  message: string,
  type: 'success' | 'error' | 'warning' = 'success'
): void {

  this.snackBar.open(message, 'Close', {
    duration: 4000,
    horizontalPosition: 'right',
    verticalPosition: 'top',
    panelClass: [`snackbar-${type}`]
  });

}

  // =====================================================
  // INITIALIZE
  // =====================================================

  ngOnInit(): void {
    this.loadDashboardData();
  }


  // =====================================================
  // SHOW / HIDE EMPLOYEE TABLE
  // =====================================================

  toggleEmployeeTable(): void {
    this.showEmployeeTable = !this.showEmployeeTable;

    // Clear search when closing table
    if (!this.showEmployeeTable) {
      this.employeeSearch = '';
      this.filteredEmployees = this.employees;
    }
  }


  // =====================================================
  // LOAD DASHBOARD DATA
  // =====================================================

  loadDashboardData(): void {

    // ---------------------------------------------------
    // LOAD EMPLOYEES
    // ---------------------------------------------------

    this.api.getEmployees().subscribe({

      next: (employees: any[]) => {

        this.employees = employees || [];

        this.filteredEmployees = this.employees;

        this.employeeCount = this.employees.length;

        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Load employees error:',
          error
        );

        this.employees = [];

        this.filteredEmployees = [];

        this.employeeCount = 0;

        this.loading = false;
      }
    });


    // ---------------------------------------------------
    // LOAD LOCATIONS
    // ---------------------------------------------------

    this.api.getLocations().subscribe({

      next: (locations: any[]) => {

        this.locations = locations || [];

        this.locationCount = this.locations.length;
      },

      error: (error) => {

        console.error(
          'Load locations error:',
          error
        );

        this.locations = [];

        this.locationCount = 0;
      }
    });
  }


  // =====================================================
  // SEARCH EMPLOYEE
  // =====================================================

  searchEmployee(): void {

    const searchText =
      this.employeeSearch
        .trim()
        .toLowerCase();


    // Show all employees when search is empty
    if (!searchText) {

      this.filteredEmployees = this.employees;

      return;
    }


    // Search by employee name
    this.filteredEmployees =
      this.employees.filter(employee =>

        employee.name
          ?.toLowerCase()
          .includes(searchText)

      );
  }


  // =====================================================
  // ADD / UPDATE EMPLOYEE
  // =====================================================

  submitEmployee(): void {

    const payload = {

      name: this.addEmployeeForm.name?.trim(),

      mobile: this.addEmployeeForm.mobile?.trim(),

      locationId: this.addEmployeeForm.locationId,

      dailyRate: Number(this.addEmployeeForm.dailyRate),

      hourlyRate: Number(this.addEmployeeForm.hourlyRate),

      overtimeRate: Number(this.addEmployeeForm.overtimeRate),

      active: this.addEmployeeForm.active
    };


    // ===================================================
    // VALIDATION
    // ===================================================

    if (!payload.name) {

     this.showSnackBar(
  'Please enter employee name.',
  'warning'
);

      return;
    }


    if (!payload.mobile) {

      this.showSnackBar(
    'Please enter mobile number.',
    'warning'
  );
      return;
    }


    if (!payload.locationId) {

       this.showSnackBar(
    'Please select a location.',
    'warning'
  );

      return;
    }


    if (
      payload.dailyRate === null ||
      payload.dailyRate === undefined ||
      payload.dailyRate < 0
    ) {

      this.submitMessage =
        'Please enter a valid daily rate.';

      return;
    }


    if (
      payload.hourlyRate === null ||
      payload.hourlyRate === undefined ||
      payload.hourlyRate < 0
    ) {

      this.submitMessage =
        'Please enter a valid hourly rate.';

      return;
    }


    if (
      payload.overtimeRate === null ||
      payload.overtimeRate === undefined ||
      payload.overtimeRate < 0
    ) {

      this.submitMessage =
        'Please enter a valid overtime rate.';

      return;
    }


    // ===================================================
    // UPDATE EMPLOYEE
    // ===================================================

    if (
      this.isEditMode &&
      this.editingEmployeeId !== null
    ) {

      this.api
        .updateEmployee(
          this.editingEmployeeId,
          payload
        )
        .subscribe({

          next: () => {

           this.showSnackBar(
  'Employee updated successfully.',
  'success'
);


            // Reload employees
            this.loadDashboardData();


            // Return form to ADD mode
            this.cancelEdit();
          },

          error: (error) => {

            console.error(
              'Update employee error:',
              error
            );

          this.showSnackBar(
  'Could not update employee.',
  'error'
);  
          }
        });

      return;
    }


    // ===================================================
    // ADD EMPLOYEE
    // ===================================================

    this.api
      .addEmployee(payload)
      .subscribe({

        next: () => {

        this.showSnackBar(
  'Employee added successfully.',
  'success'
);


          // Reset form after successful add
          this.resetEmployeeForm();


          // Reload employees
          this.loadDashboardData();
        },

        error: (error) => {

          console.error(
            'Add employee error:',
            error
          );

          this.showSnackBar(
        'Could not add employee. Check location and rates.',
        'error'
      );
        }
      });
  }


  // =====================================================
  // EDIT EMPLOYEE
  // =====================================================

  editEmployee(employee: any): void {

    if (!employee || !employee.id) {

      console.error(
        'Invalid employee selected for editing:',
        employee
      );

      return;
    }


    // Enable edit mode
    this.isEditMode = true;

    this.editingEmployeeId = employee.id;


    // Fill Add Employee form with employee data
    this.addEmployeeForm = {

      name: employee.name || '',

      mobile: employee.mobile || '',

      locationId:
        employee.location?.id ??
        employee.locationId ??
        null,

      dailyRate:
        employee.dailyRate ?? 0,

      hourlyRate:
        employee.hourlyRate ?? 1,

      overtimeRate:
        employee.overtimeRate ?? 1,

      active:
        employee.active !== false
    };


    // Clear previous message
    this.submitMessage = '';


    // Scroll to employee form
    window.scrollTo({

      top: 350,

      behavior: 'smooth'
    });
  }


  // =====================================================
  // DELETE EMPLOYEE
  // =====================================================

  deleteEmployee(employeeOrId: any): void {

    // Support both:
    // deleteEmployee(employee.id)
    // AND
    // deleteEmployee(employee)

    const employeeId =
      typeof employeeOrId === 'object'
        ? employeeOrId?.id
        : employeeOrId;


    // Prevent /undefined API call
    if (
      employeeId === null ||
      employeeId === undefined ||
      employeeId === ''
    ) {

      console.error(
        'Invalid employee ID:',
        employeeOrId
      );

     this.showSnackBar(
      'Could not delete employee. Invalid employee ID.',
      'error'
    );

      return;
    }


    // Find employee name for confirmation
    const employee =
      this.employees.find(
        e => e.id === employeeId
      );


    const employeeName =
      employee?.name || 'this employee';


    const confirmed = confirm(

      `Are you sure you want to delete ${employeeName}?`

    );


    if (!confirmed) {

      return;
    }


    // ===================================================
    // DELETE API
    // ===================================================

    this.api
      .deleteEmployee(employeeId)
      .subscribe({

        next: () => {

          this.showSnackBar(
          'Employee deleted successfully.',
          'success'
        );

          // Reload employee list
          this.loadDashboardData();
        },

        error: (error) => {

          console.error(
            'Delete employee error:',
            error
          );

          this.showSnackBar(
            'Could not delete employee.',
            'error'
          );
        }
      });
  }


  // =====================================================
  // CANCEL UPDATE / EDIT MODE
  // =====================================================

  cancelEdit(): void {

    this.isEditMode = false;

    this.editingEmployeeId = null;


    // Return form to Add Employee mode
    this.resetEmployeeForm();
  }


  // =====================================================
  // RESET EMPLOYEE FORM
  // =====================================================

  resetEmployeeForm(): void {

    this.addEmployeeForm = {

      name: '',

      mobile: '',

      locationId: null,

      dailyRate: 0,

      hourlyRate: 1,

      overtimeRate: 1,

      active: true
    };
  }


  // =====================================================
  // ADD LOCATION
  // =====================================================

  submitLocation(): void {

    const payload = {

      name:
        this.addLocationForm.name
          .trim(),

      address:
        this.addLocationForm.address
          .trim()
    };


    // ===================================================
    // VALIDATION
    // ===================================================

    if (!payload.name) {

      this.locationSubmitMessage =
        'Please enter a location name.';

      return;
    }


    // ===================================================
    // ADD LOCATION
    // ===================================================

    this.api
      .addLocation(payload)
      .subscribe({

        next: () => {

          this.locationSubmitMessage =
            'Location added successfully.';


          this.resetLocationForm();


          this.loadDashboardData();
        },

        error: (error) => {

          console.error(
            'Add location error:',
            error
          );

          this.locationSubmitMessage =
            'Could not add location. The name may already exist.';
        }
      });
  }


  // =====================================================
  // RESET LOCATION FORM
  // =====================================================

  resetLocationForm(): void {

    this.addLocationForm = {

      name: '',

      address: ''
    };
  }

}