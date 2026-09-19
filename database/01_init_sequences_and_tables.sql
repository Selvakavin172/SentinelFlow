-- ============================================================
-- Sentinel AML - PostgreSQL Sequence & Table Setup Script
-- ============================================================
-- Run this script in PostgreSQL to create sequences and tables
-- for the Sentinel AML application

-- ============================================================
-- CREATE SEQUENCES
-- ============================================================

-- Customer sequence
CREATE SEQUENCE IF NOT EXISTS customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Account sequence
CREATE SEQUENCE IF NOT EXISTS account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Transaction sequence
CREATE SEQUENCE IF NOT EXISTS transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Alert sequence
CREATE SEQUENCE IF NOT EXISTS alert_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Case sequence
CREATE SEQUENCE IF NOT EXISTS case_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- ============================================================
-- CREATE TABLES
-- ============================================================

-- Customer table
CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGINT PRIMARY KEY DEFAULT nextval('customer_id_seq'),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    date_of_birth DATE,
    age INT,
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(30),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    occupation VARCHAR(100),
    annual_income DECIMAL(18,2),
    marital_status VARCHAR(50),
    education VARCHAR(100),
    employment_type VARCHAR(50),
    customer_kyc_status VARCHAR(50) DEFAULT 'PENDING',
    risk_rating VARCHAR(20) DEFAULT 'LOW',
    is_politically_exposed BOOLEAN DEFAULT FALSE,
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    num_complaints INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Create indexes on customer
CREATE INDEX IF NOT EXISTS idx_email ON customer(email);
CREATE INDEX IF NOT EXISTS idx_risk_rating ON customer(risk_rating);
CREATE INDEX IF NOT EXISTS idx_kyc_status ON customer(customer_kyc_status);

-- Account table
CREATE TABLE IF NOT EXISTS account (
    account_id BIGINT PRIMARY KEY DEFAULT nextval('account_id_seq'),
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id),
    account_number VARCHAR(50) UNIQUE NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    opening_date DATE NOT NULL,
    status VARCHAR(30) DEFAULT 'ACTIVE',
    branch_code VARCHAR(30),
    current_balance DECIMAL(18,2) DEFAULT 0.00,
    avg_monthly_credit DECIMAL(18,2) DEFAULT 0.00,
    avg_monthly_debit DECIMAL(18,2) DEFAULT 0.00,
    credit_limit DECIMAL(18,2) DEFAULT 0.00,
    overdraft_limit DECIMAL(18,2) DEFAULT 0.00,
    card_type VARCHAR(50),
    joint_account BOOLEAN DEFAULT FALSE,
    linked_mobile VARCHAR(30),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes on account
CREATE INDEX IF NOT EXISTS idx_account_number ON account(account_number);
CREATE INDEX IF NOT EXISTS idx_account_customer ON account(customer_id);
CREATE INDEX IF NOT EXISTS idx_account_status ON account(status);

-- Transaction table
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id BIGINT PRIMARY KEY DEFAULT nextval('transaction_id_seq'),
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    counterparty_account VARCHAR(50),
    counterparty_name VARCHAR(150),
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    amount_inr DECIMAL(18,2),
    transaction_datetime TIMESTAMP NOT NULL,
    country_code VARCHAR(10),
    channel VARCHAR(50),
    merchant_category VARCHAR(100),
    narration TEXT,
    is_high_risk BOOLEAN DEFAULT FALSE,
    status VARCHAR(30) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes on transaction
CREATE INDEX IF NOT EXISTS idx_transaction_account ON transaction(account_id);
CREATE INDEX IF NOT EXISTS idx_transaction_datetime ON transaction(transaction_datetime);
CREATE INDEX IF NOT EXISTS idx_transaction_country ON transaction(country_code);
CREATE INDEX IF NOT EXISTS idx_transaction_amount ON transaction(amount);

-- Alert table
CREATE TABLE IF NOT EXISTS alert (
    alert_id BIGINT PRIMARY KEY DEFAULT nextval('alert_id_seq'),
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id),
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    transaction_id BIGINT REFERENCES transaction(transaction_id),
    rule_code VARCHAR(50) NOT NULL,
    rule_name VARCHAR(100) NOT NULL,
    risk_score INT NOT NULL,
    alert_status VARCHAR(30) DEFAULT 'OPEN',
    alert_reason TEXT,
    evidence_json JSONB,
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM'
);

-- Create indexes on alert
CREATE INDEX IF NOT EXISTS idx_alert_customer ON alert(customer_id);
CREATE INDEX IF NOT EXISTS idx_alert_account ON alert(account_id);
CREATE INDEX IF NOT EXISTS idx_alert_status ON alert(alert_status);
CREATE INDEX IF NOT EXISTS idx_alert_risk_score ON alert(risk_score);
CREATE INDEX IF NOT EXISTS idx_alert_triggered ON alert(triggered_at);

-- Case table
CREATE TABLE IF NOT EXISTS case_table (
    case_id BIGINT PRIMARY KEY DEFAULT nextval('case_id_seq'),
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id),
    case_title VARCHAR(200),
    case_status VARCHAR(30) DEFAULT 'OPEN',
    severity VARCHAR(20) DEFAULT 'MEDIUM',
    assigned_to VARCHAR(100),
    opened_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    disposition_reason VARCHAR(200),
    investigation_notes TEXT,
    created_by VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes on case_table
CREATE INDEX IF NOT EXISTS idx_case_customer ON case_table(customer_id);
CREATE INDEX IF NOT EXISTS idx_case_status ON case_table(case_status);
CREATE INDEX IF NOT EXISTS idx_case_severity ON case_table(severity);
CREATE INDEX IF NOT EXISTS idx_case_assigned ON case_table(assigned_to);

-- ============================================================
-- VERIFY SEQUENCES AND TABLES CREATED
-- ============================================================

-- List all sequences
SELECT * FROM pg_sequences WHERE schemaname = 'public';

-- List all tables
SELECT tablename FROM pg_tables WHERE schemaname = 'public';

-- ============================================================
-- DATABASE SETUP COMPLETE
-- ============================================================
-- All sequences and tables have been created successfully.
-- The Sentinel AML application is ready to run.
