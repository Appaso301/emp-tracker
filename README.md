# EmpTrackerSystem

A simple employee attendance and payroll tracker using Angular frontend, Spring Boot backend, and MySQL.

## Structure

- `backend/` - Spring Boot application
- `frontend/` - Angular application

## Backend setup

1. Install Java 21 and Maven.
2. Update `backend/src/main/resources/application.properties` with your MySQL credentials.
3. Ensure a MySQL database exists at `emp_tracker` or change the JDBC URL.
4. Run the backend:

```bash
cd backend
mvn spring-boot:run
```

The backend listens on `http://localhost:8080`.

## Frontend setup

1. Install Node.js and npm.
2. Run the frontend:

```bash
cd frontend
npm install
npm start
```

The frontend opens at `http://localhost:4200`.

## Notes

- The backend includes sample location, employee, attendance, advance, expense, and travel charge data.
- The Angular app provides login, attendance entry, and reporting screens.
- Add employee, location, and finance endpoints as needed for full admin functionality.
