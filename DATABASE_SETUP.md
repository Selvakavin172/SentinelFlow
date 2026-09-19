# 🗄️ Sentinel AML - PostgreSQL Setup Instructions

## Error Fixed: GenerationType.IDENTITY → GenerationType.SEQUENCE

The `@GeneratedValue` annotations in all entity classes have been updated to use PostgreSQL `SEQUENCE` strategy instead of `IDENTITY`.

### Updated Entities:
- ✅ Customer.java
- ✅ Account.java
- ✅ Transaction.java
- ✅ Alert.java
- ✅ AmlCase.java

## 🚀 How to Setup PostgreSQL Database

### Step 1: Create Database

```sql
CREATE DATABASE sentinel_db;
```

### Step 2: Run the SQL Script

Execute the provided SQL script to create all sequences and tables:

```sql
-- On Windows PowerShell (as Administrator)
psql -U postgres -d sentinel_db -f "D:\Java\SentinelFlow\database\01_init_sequences_and_tables.sql"

-- OR from psql command line
\c sentinel_db
\i 'D:/Java/SentinelFlow/database/01_init_sequences_and_tables.sql'
```

### Step 3: Verify Setup

```sql
-- Check sequences were created
SELECT * FROM pg_sequences WHERE schemaname = 'public';

-- Check tables were created
SELECT tablename FROM pg_tables WHERE schemaname = 'public';

-- Quick verification query
SELECT COUNT(*) FROM pg_tables WHERE schemaname = 'public' AND tablename IN 
('customer', 'account', 'transaction', 'alert', 'case_table');
-- Should return 5
```

### Step 4: Update application.properties

Edit `D:\Java\SentinelFlow\src\main\resources\application.properties`:

```properties
# PostgreSQL Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/sentinel_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate should validate (not create) since we created tables via SQL
spring.jpa.hibernate.ddl-auto=validate
```

### Step 5: Rebuild & Run Application

```powershell
# Navigate to project
cd D:\Java\SentinelFlow

# Set JAVA_HOME if needed
$env:JAVA_HOME="C:\Users\hp\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_22.0.0.v20240322-1225\jre"

# Clean and rebuild
.\mvnw.cmd clean package -DskipTests

# Run the application
java -jar target/SentinelFlow-0.0.1-SNAPSHOT.jar
```

## 📝 What the SQL Script Creates

### Sequences (5 total):
1. `customer_id_seq` - For customer table
2. `account_id_seq` - For account table
3. `transaction_id_seq` - For transaction table
4. `alert_id_seq` - For alert table
5. `case_id_seq` - For case_table

### Tables (5 total):
1. **customer** - Customer KYC data (25 columns)
2. **account** - Bank account details (18 columns)
3. **transaction** - Transaction records (13 columns)
4. **alert** - AML detection alerts (12 columns)
5. **case_table** - Case management (11 columns)

### Indexes (17 total):
- 3 on customer table
- 3 on account table
- 4 on transaction table
- 5 on alert table
- 4 on case_table

## 🔧 PostgreSQL SEQUENCE vs IDENTITY

**Why we changed from IDENTITY to SEQUENCE:**

| Aspect | IDENTITY | SEQUENCE |
|--------|----------|----------|
| Support | Limited in PostgreSQL | Full PostgreSQL native support |
| Compatibility | MySQL-style (not optimal) | PostgreSQL-native (optimal) |
| Control | Limited | Full control over allocation |
| Performance | Good | Better with Hibernate |
| Recommended | No | ✅ **YES for PostgreSQL** |

## ✅ Verification Queries

```sql
-- Check all sequences exist
SELECT sequencename FROM pg_sequences 
WHERE schemaname = 'public' 
ORDER BY sequencename;

-- Check all tables exist with correct columns
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_type = 'BASE TABLE';

-- Test sequence generation
SELECT nextval('customer_id_seq');
SELECT nextval('account_id_seq');
SELECT nextval('transaction_id_seq');
SELECT nextval('alert_id_seq');
SELECT nextval('case_id_seq');

-- Check table structure (example for customer)
\d customer

-- List all indexes
SELECT tablename, indexname FROM pg_indexes 
WHERE schemaname = 'public' 
ORDER BY tablename, indexname;
```

## 🐛 Troubleshooting

### Error: "sequence does not exist"
```sql
-- Create missing sequence
CREATE SEQUENCE IF NOT EXISTS sequence_name_seq START WITH 1;
```

### Error: "relation 'table_name' does not exist"
```sql
-- Recreate table from SQL script
\i 'D:/Java/SentinelFlow/database/01_init_sequences_and_tables.sql'
```

### Error: "column does not exist"
```sql
-- Check table structure
\d table_name

-- Add missing columns if needed
ALTER TABLE table_name ADD COLUMN column_name data_type;
```

## 📊 Expected Startup Output

Once database is configured correctly, you should see:

```
2026-09-19T12:08:44.410+05:30  INFO ... Spring Data repository scanning in 80 ms. Found 5 JPA repository interfaces.
2026-09-19T12:08:45.260+05:30  INFO ... Tomcat initialized with port 8080 (http)
2026-09-19T12:08:45.440+05:30  INFO ... Root WebApplicationContext: initialization completed
2026-09-19T12:08:45.600+05:30  INFO ... Started SentinelFlowApplication in 3.XXX seconds
```

## 🎯 Next Steps After Setup

1. ✅ Database created with sequences and tables
2. ✅ Application configured to use PostgreSQL
3. ⏳ Create Services layer
4. ⏳ Build REST Controllers
5. ⏳ Implement Detection Engine
6. ⏳ Add unit tests

---

**File Location**: `D:\Java\SentinelFlow\database\01_init_sequences_and_tables.sql`

**Status**: 🟢 Ready to setup PostgreSQL database
