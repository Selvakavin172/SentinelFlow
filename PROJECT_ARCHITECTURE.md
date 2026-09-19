# Sentinel AML - Project Architecture & Structure

## 📦 Project Package Hierarchy

```
com.sentinelflow/
├── models/                    # Entity classes (JPA entities)
│   ├── Customer.java          # Customer entity with KYC data
│   ├── Account.java           # Account entity linked to customers
│   ├── Transaction.java       # Transaction entity for monitoring
│   ├── Alert.java             # Alert entity for AML detection results
│   └── AmlCase.java           # Case entity for analyst workflow
│
├── repositories/              # Data access layer (Spring Data JPA)
│   ├── CustomerRepository.java    # Custom queries for customers
│   ├── AccountRepository.java     # Account queries
│   ├── TransactionRepository.java # Transaction analysis queries
│   ├── AlertRepository.java       # Alert retrieval and sorting
│   └── AmlCaseRepository.java     # Case management queries
│
├── services/                  # Business logic layer (to be created)
│   ├── CustomerService.java       # Customer operations
│   ├── AccountService.java        # Account operations
│   ├── TransactionService.java    # Transaction processing
│   ├── AlertService.java          # Alert management
│   └── AmlCaseService.java        # Case workflow management
│
├── controllers/               # REST API layer (to be created)
│   ├── CustomerController.java    # Customer endpoints
│   ├── AccountController.java     # Account endpoints
│   ├── TransactionController.java # Transaction ingestion/query
│   ├── AlertController.java       # Alert management endpoints
│   └── CaseController.java        # Case management endpoints
│
├── detection/                 # AML Detection Engine (to be created)
│   ├── RuleEngine.java            # Core detection rule execution
│   ├── StructuringRule.java       # Smurfing detection
│   ├── RapidMovementRule.java     # Layering detection
│   ├── HighRiskJurisdictionRule.java # Sanction list checking
│   ├── BehavioralDeviationRule.java  # Anomaly detection
│   └── RoundNumberRule.java       # Pattern matching
│
├── dto/                       # Data Transfer Objects (to be created)
│   ├── CustomerDTO.java
│   ├── TransactionDTO.java
│   ├── AlertDTO.java
│   └── CaseDTO.java
│
└── SentinelFlowApplication.java  # Spring Boot main application
```

## 🗄️ Database Schema (PostgreSQL)

### Tables Created:
1. **customer** - Customer KYC & risk data
2. **account** - Bank accounts linked to customers
3. **transaction** - Individual transactions for monitoring
4. **alert** - Detection alerts with risk scores
5. **case_table** - Analyst case management

### Indexes:
- All tables include strategic indexes on foreign keys, status fields, and frequently searched fields
- Risk score index on alerts for sorting by priority
- Transaction datetime index for time-range queries

## 🛠️ Technologies & Dependencies

### Core:
- **Spring Boot 4.1.1** with Spring Data JPA
- **Java 17+**
- **PostgreSQL** database

### Utilities:
- **Lombok** - Reduce boilerplate code
- **Jackson** - JSON/JSONB processing
- **Hibernate Validator** - Input validation

### API & Documentation:
- **SpringDoc OpenAPI** - Auto-generated Swagger docs

### Testing:
- **JUnit 5** - Unit testing framework
- **Mockito** - Mock objects for testing
- **H2** (optional) - In-memory testing database

## 📊 Entity Relationships

```
Customer (1) ─── (*) Account
   │
   ├─── (*) Transaction
   │
   ├─── (*) Alert
   │
   └─── (*) AmlCase

Account (1) ─── (*) Transaction
   │
   └─── (*) Alert

Transaction (1) ─── (*) Alert
```

## 🔍 Key Repository Query Methods

### CustomerRepository
- `findByEmail()` - Lookup by email
- `findHighRiskCustomers()` - Filter by HIGH/VERY_HIGH risk
- `findExposedHighRiskCustomers()` - PEP + High Risk
- `findVerifiedCustomers()` - Email & phone verified

### TransactionRepository
- `findTransactionsByAccountAndTimeRange()` - Time-window analysis
- `findHighValueTransactionsByAccountAndTimeRange()` - Amount thresholds
- `sumTransactionAmountByAccountAndTimeRange()` - Aggregation
- `findTransactionsByTypeAndTimeRange()` - Transaction type filtering

### AlertRepository
- `findOpenAlertsSortedByRiskScore()` - Prioritized alert queue
- `findCriticalAlerts()` - High-risk alerts only
- `findAlertsByDateRange()` - Time-period reporting
- `countOpenAlerts()` - Dashboard metrics

### AmlCaseRepository
- `findOpenCasesSortedBySeverity()` - Case prioritization
- `findOpenCasesAssignedToAnalyst()` - Analyst workload
- `findHighSeverityCases()` - Escalation tracking
- `findUnassignedOpenCases()` - Distribution queue

## 🏗️ Layered Architecture

```
┌─────────────────────────────────────┐
│   REST Controllers (API Layer)      │
│   /api/v1/customers                │
│   /api/v1/accounts                 │
│   /api/v1/transactions             │
│   /api/v1/alerts                   │
│   /api/v1/cases                    │
└─────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│   Service Layer (Business Logic)    │
│   - CustomerService                │
│   - AccountService                 │
│   - TransactionService             │
│   - AlertService                   │
│   - CaseService                    │
└─────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│   Detection Engine                  │
│   - Rule Evaluation                 │
│   - Risk Scoring                    │
│   - Alert Generation                │
└─────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│   Repository Layer (Data Access)    │
│   - CustomerRepository              │
│   - AccountRepository               │
│   - TransactionRepository           │
│   - AlertRepository                 │
│   - CaseRepository                  │
└─────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│   Entity Layer (Domain Models)      │
│   - Customer                        │
│   - Account                         │
│   - Transaction                     │
│   - Alert                           │
│   - AmlCase                         │
└─────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│   PostgreSQL Database               │
└─────────────────────────────────────┘
```

## 🎯 Next Steps

1. **Create Service Layer** - Business logic implementation
2. **Build REST Controllers** - API endpoints with proper HTTP methods
3. **Implement Detection Engine** - AML rule evaluation
4. **Add DTO/Response Objects** - API response formatting
5. **Create Unit Tests** - Test coverage for rules
6. **Configure application.properties** - PostgreSQL connection
7. **Add Database Migration Scripts** - Flyway/Liquibase
8. **Implement Security** - Role-based access control
9. **Create Frontend** - Angular/React dashboard

## 📝 AML Rules to Implement

1. **CTR Rule** (Currency Transaction Report)
   - Flag transactions ≥ $10,000 automatically

2. **Structuring/Smurfing Rule**
   - 3+ transactions $9,000-$9,999 within 24 hours

3. **Rapid Movement Rule** (Layering)
   - 80% of deposit transferred out within 48 hours

4. **High-Risk Jurisdiction Rule**
   - Any transaction to/from sanctioned countries

5. **Behavioral Deviation Rule**
   - Transaction volume exceeds 3x 90-day average

6. **Round Number Rule**
   - Repeated suspiciously round amounts pattern

## 🔐 Security Considerations

- PII masking in list views
- Full PII visibility only in detail views for authorized roles
- Environment variables for DB credentials
- Audit logging for all state transitions
- All alerts marked as immutable (never deleted)

---

**Project Status**: ✅ Models & Repositories Created | ⏳ Services & Controllers Pending
