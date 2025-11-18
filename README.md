📊 Personal Finance Tracker
A full-stack capstone project for managing accounts, tracking transactions, and setting savings goals. Built with Spring Boot (Java 17+), Angular, and MySQL, deployed on AWS.

🚀 Features
Secure registration & login with JWT authentication

Dashboard overview of accounts, transactions, and savings goals

Add, edit, and delete accounts or transactions

Filter transactions by name or category

Visual progress tracking for savings goals

🛠️ Prerequisites
Ensure the following tools are installed before setup:

Java 17+ – Backend runtime

Node.js & Angular CLI – Frontend build & dev server

MySQL Server – Relational database

Git – Version control

IntelliJ IDEA / VS Code – Recommended IDEs

📥 Installation
1. Clone the Repository
   bash
   git clone <your-gitlab-repo-url>
   cd personal-finance-tracker
2. Configure Environment Files
   Backend configs:

application.properties → general settings

application-local.properties → local secrets (excluded from Git)

Example application.properties:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_tracker
spring.jpa.hibernate.ddl-auto=update
Example application-local.properties:

properties
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
jwt.secret=your_secret_key
3. Run the Backend
   Open backend in IntelliJ (or IDE of choice)

Run the main Spring Boot application

JPA/Hibernate auto-generates schema

4. Run the Frontend
   bash
   cd frontend
   npm install
   ng serve
   Navigate to http://localhost:4200.

⚙️ Configuration
CORS
Managed in Spring Boot backend

Update allowedOrigins for production (e.g., AWS domain)

Security
Authentication & authorization via JWT

Key files:

SecurityConfig.java

JWTAuthenticationFilter.java

JwtUtils.java

Modify roles, access rules, and token expiration as needed

☁️ Deployment (AWS)
Frontend
bash
ng build --configuration production
Upload /dist/frontend/browser to S3 bucket (static hosting)

Backend
bash
mvn clean package
Deploy .jar from /target to AWS Elastic Beanstalk

Set environment variables: DB credentials, JWT secret, DB URL

🧪 Testing
Running Unit Tests
Written with JUnit 5 + Mockito

Run via IntelliJ or terminal:

Right-click AccountServiceTest → Run

Coverage
✅ Account creation success

❌ Account creation failure (invalid user)

Interpreting Results
Green ✔ = success

Red ✘ = failure (check stack trace & logs)

Example Fix
Initial tests revealed createAccount() did not handle null user IDs. Added validation to throw ResourceNotFoundException.

👤 User Guide
Getting Started
Register → Create account with username, email, password

Login → Access dashboard securely

Dashboard
Snapshot of accounts, transactions, and savings goals

Managing Accounts
Add, edit, delete accounts

Specify account type (Checking/Savings) & balance

Transactions
Add/edit/delete transactions per account

Fields: name, amount, date, type (Credit/Debit), category

Shared categories across users

Balances auto-update

Savings Goals
Define target amounts

Progress bar visualization

Logout
Securely log out via navigation bar

📌 Summary
This capstone demonstrates a production-ready full-stack app with modern tooling, CI/CD readiness, and AWS deployment. It balances developer setup instructions with end-user guidance for smooth onboarding.