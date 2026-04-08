# 🏦 Banking Activity Simulation Platform

A full-stack Banking Activity Simulation Platform built with **Spring Boot** (backend) and **HTML/CSS/JavaScript** (frontend). The platform simulates real-world banking operations including account management, deposits, withdrawals, fund transfers, transaction history, and an automated bulk activity simulator.

---

## 📁 Project Structure

```
Banking-Activity-Simulation-Platform/
├── backend/          → Spring Boot REST API (Maven)
├── frontend/         → Vanilla HTML/CSS/JS UI
├── LICENSE
└── README.md
```

---

## ✨ Features

| Feature | Description |
|---|---|
| 👤 Create Account | Open a new bank account with name, email, and opening balance |
| 💰 Deposit | Add funds to any existing account |
| 💸 Withdraw | Withdraw funds with balance validation |
| 🔄 Transfer | Move money between two accounts atomically |
| 🔍 View Account | Look up account details and current balance |
| 📋 All Accounts | View all registered accounts in a table |
| 📊 Transaction Stats | Get counts and volumes grouped by transaction type |
| 🤖 Simulation Engine | Bulk-generate accounts and fire random transactions automatically |
| 🔔 Low Balance Alert | Email notification when balance falls below a threshold |
| 📝 File Reports | All transactions logged to `reports/transactions.txt` |

---

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2** (Web, Data JPA, Validation, Mail)
- **Maven** — dependency management & build
- **H2** — embedded in-memory database (no external DB needed)
- **JUnit 5 + Mockito** — unit testing

### Frontend
- **HTML5**
- **CSS3** (custom responsive design)
- **Vanilla JavaScript** (Fetch API)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- A modern browser (Chrome recommended)

### 1. Run the Backend

```bash
cd backend
mvn spring-boot:run
```

The server starts at **http://localhost:8080**

> **H2 Console** (database viewer): http://localhost:8080/h2-console
> - JDBC URL: `jdbc:h2:mem:bankdb`
> - Username: `sa` | Password: *(leave blank)*

### 2. Open the Frontend

Open `frontend/index.html` directly in your browser — no server required.

---

## 📡 REST API Reference

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/accounts` | Create a new account |
| `GET` | `/api/accounts` | List all accounts |
| `GET` | `/api/accounts/{accountNumber}` | Get account details |
| `POST` | `/api/accounts/{accountNumber}/deposit` | Deposit money |
| `POST` | `/api/accounts/{accountNumber}/withdraw` | Withdraw money |
| `GET` | `/api/accounts/{accountNumber}/transactions` | Transaction history |

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/transactions/transfer` | Transfer between accounts |
| `GET` | `/api/transactions/stats` | Get transaction statistics |

### Simulation

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/simulation/run` | Run a bulk transaction simulation |

#### Simulation Example Request
```json
POST /api/simulation/run
{
  "numberOfAccounts": 10,
  "numberOfTransactions": 50
}
```

---

## 🧪 Running Tests

```bash
cd backend
mvn test
```

Tests cover:
- Account creation, retrieval, and balance validation
- Deposit, withdrawal, and transfer business logic
- Insufficient funds and invalid amount error handling
- REST controller layer with MockMvc

---

## ⚙️ Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Low balance alert threshold
bank.alert.low-balance-threshold=1000

# Enable email alerts (set to true and configure SMTP)
bank.alert.email-enabled=false

# Transaction report output file
bank.report.file=reports/transactions.txt

# H2 Console
spring.h2.console.enabled=true
```

---

## 📂 Backend Package Structure

```
com.bank/
├── BankingApplication.java       ← Entry point
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── AccountController.java
│   ├── TransactionController.java
│   └── SimulationController.java
├── dto/                          ← Request & Response objects
├── exception/                    ← Custom exceptions + global handler
├── model/
│   ├── Account.java
│   ├── Transaction.java
│   └── TransactionType.java
├── repository/                   ← Spring Data JPA interfaces
└── service/
    ├── AccountService.java
    ├── TransactionService.java
    ├── AlertService.java
    ├── ReportService.java
    └── SimulationService.java
```

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

**Copyright © 2026 Jaknam Sahithi**

---

## 🙏 Acknowledgements

- Inspired by real-world banking system design patterns
- Built as a learning project to demonstrate Spring Boot REST API development
- Reference: [CHINNAKRISHNA143](https://github.com/CHINNAKRISHNA143) on GitHub
