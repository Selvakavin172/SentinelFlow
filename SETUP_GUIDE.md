# Sentinel AML - Setup & Quick Start Guide

## 🎉 Project Build Status

✅ **Build Successful!** The project has been successfully compiled and packaged.

## 📦 Build Information

- **Project Name**: SentinelFlow
- **Version**: 0.0.1-SNAPSHOT
- **JAR File**: `D:\Java\SentinelFlow\target\SentinelFlow-0.0.1-SNAPSHOT.jar`
- **Java Version**: Java 22 (OpenJDK)
- **Spring Boot Version**: 4.1.1
- **Build Time**: ~24 seconds

## 🚀 How to Start the Application

### Prerequisites

1. **PostgreSQL Database** - Must be installed and running
2. **Java 17+** - Already verified on your system (Java 22)
3. **Maven** - Using Maven wrapper (already in project)

### Step 1: Create PostgreSQL Database

```sql
-- Create the database
CREATE DATABASE sentinel_db;

-- Connect to the database
\c sentinel_db;

-- Create tables (run your table creation script)
```

### Step 2: Update Database Configuration

Edit `D:\Java\SentinelFlow\src\main\resources\application.properties`:

```properties
# Update these to your PostgreSQL credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/sentinel_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
```

### Step 3: Run the Application

**Option A: Direct JAR Execution**
```powershell
cd D:\Java\SentinelFlow
$env:JAVA_HOME="C:\Users\hp\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_22.0.0.v20240322-1225\jre"
java -jar target/SentinelFlow-0.0.1-SNAPSHOT.jar
```

**Option B: Maven Run**
```powershell
cd D:\Java\SentinelFlow
$env:JAVA_HOME="C:\Users\hp\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_22.0.0.v20240322-1225\jre"
.\mvnw spring-boot:run
```

### Step 4: Verify Application is Running

Once started, you should see:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/ :: Spring Boot :: (v4.1.1)

Started SentinelFlowApplication in X.XXX seconds
```

## 📍 Application Endpoints

Once running, the application will be available at:

- **Base URL**: `http://localhost:8080/sentinel`
- **API Docs**: `http://localhost:8080/sentinel/swagger-ui.html`
- **Health Check**: `http://localhost:8080/sentinel/actuator/health`

## 🗄️ Database Schema

The application expects the following tables (which you've already created):

1. **customer** - Customer KYC data
2. **account** - Bank accounts
3. **transaction** - Transaction records
4. **alert** - AML detection alerts
5. **case_table** - Case management for analysts

## 📝 Project Structure

```
D:\Java\SentinelFlow\
├── src/
│   ├── main/
│   │   ├── java/com/sentinelflow/
│   │   │   ├── models/           (5 JPA Entities)
│   │   │   ├── repositories/     (5 Spring Data Repositories)
│   │   │   ├── services/         (Ready for implementation)
│   │   │   ├── controllers/      (Ready for implementation)
│   │   │   └── detection/        (Ready for rule engine)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml                        (Maven configuration)
├── mvnw / mvnw.cmd               (Maven wrapper)
└── target/
    └── SentinelFlow-0.0.1-SNAPSHOT.jar
```

## ✅ Created Components

### Models (5 JPA Entities)
- ✅ `Customer.java` - KYC and risk data
- ✅ `Account.java` - Account details
- ✅ `Transaction.java` - Transaction records
- ✅ `Alert.java` - Detection alerts
- ✅ `AmlCase.java` - Case management

### Repositories (5 Spring Data JPA)
- ✅ `CustomerRepository` - Custom queries for customers
- ✅ `AccountRepository` - Account queries
- ✅ `TransactionRepository` - Transaction analysis
- ✅ `AlertRepository` - Alert management
- ✅ `AmlCaseRepository` - Case management

### Configuration
- ✅ `application.properties` - Comprehensive configuration
- ✅ `pom.xml` - Dependencies configured
- ✅ `PROJECT_ARCHITECTURE.md` - Architecture documentation

## 🔄 Next Steps to Complete the Application

1. **Services Layer** - Create business logic services
   ```
   - CustomerService
   - AccountService
   - TransactionService
   - AlertService
   - AmlCaseService
   ```

2. **REST Controllers** - API endpoints
   ```
   - CustomerController (/api/v1/customers)
   - AccountController (/api/v1/accounts)
   - TransactionController (/api/v1/transactions)
   - AlertController (/api/v1/alerts)
   - CaseController (/api/v1/cases)
   ```

3. **Detection Engine** - AML rule evaluation
   ```
   - RuleEngine
   - StructuringRule (Smurfing)
   - RapidMovementRule (Layering)
   - HighRiskJurisdictionRule
   - BehavioralDeviationRule
   - RoundNumberRule
   ```

4. **Security & Auth** - Authentication & authorization

5. **Testing** - Unit & integration tests

## 🐛 Troubleshooting

### Error: Failed to determine a suitable driver class
**Cause**: PostgreSQL driver not in classpath or database config missing
**Fix**: Ensure PostgreSQL is configured in `application.properties`

### Error: Connection refused to localhost:5432
**Cause**: PostgreSQL server not running
**Fix**: Start PostgreSQL service

### Error: FATAL: database "sentinel_db" does not exist
**Cause**: Database not created
**Fix**: Create database using SQL script

## 📚 Useful Commands

```powershell
# Clean and rebuild
cd D:\Java\SentinelFlow
.\mvnw clean package -DskipTests

# Run application
java -jar target/SentinelFlow-0.0.1-SNAPSHOT.jar

# View logs
Get-Content logs/sentinel-aml.log -Tail 50

# Check if port 8080 is in use
netstat -ano | findstr :8080
```

## 🎯 Architecture Summary

The application follows a **Layered Architecture**:

```
┌─────────────────────────┐
│   REST Controllers      │
├─────────────────────────┤
│   Service Layer         │
├─────────────────────────┤
│   Detection Engine      │
├─────────────────────────┤
│   Repository Layer      │
├─────────────────────────┤
│   Entity Models (JPA)   │
├─────────────────────────┤
│   PostgreSQL Database   │
└─────────────────────────┘
```

## 📊 Key Features Ready

✅ Entity models with proper relationships
✅ Database schema with indexes
✅ Spring Data JPA repositories with custom queries
✅ Comprehensive logging configuration
✅ Swagger/OpenAPI documentation support
✅ Connection pooling configured
✅ Transaction management configured
✅ Validation framework ready

## 💡 Configuration Properties Included

- **Server**: Port 8080, Context path `/sentinel`
- **Database**: PostgreSQL with HikariCP connection pool
- **Logging**: File-based and console logging with rotation
- **Swagger**: Auto-generated API documentation
- **Monitoring**: Actuator endpoints for health checks
- **AML Rules**: Configurable detection thresholds

---

**Status**: 🟢 Ready for Service Layer Implementation

**Next Task**: Create REST Controllers or Service Layer implementation
