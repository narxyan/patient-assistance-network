-- Person Table
CREATE TABLE Person (
    SSN CHAR(9) PRIMARY KEY,
    person_name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    profession VARCHAR(150),
    mailing_address VARCHAR(250),
    email_address VARCHAR(100),
    phone_no VARCHAR(15),
    on_mailing_list BIT
);

-- Emergency Contact Table
CREATE TABLE Emergency_Contact (
    SSN CHAR(9),
    emergency_name VARCHAR(150) NOT NULL,
    phone_number CHAR(15),
    relation VARCHAR(50),
    PRIMARY KEY (SSN, phone_number),
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

-- Index to optimize joins with Person in Emergency_Contact
CREATE INDEX idx_emergency_contact_ssn ON Emergency_Contact(SSN);

-- Client Table
CREATE TABLE Client (
    SSN CHAR(9) PRIMARY KEY,
    doctor_name VARCHAR(150),
    doctor_phone_no CHAR(15),
    date_assigned DATE,
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

-- Volunteer Table
CREATE TABLE Volunteer (
    SSN CHAR(9) PRIMARY KEY,
    date_joined DATE,
    date_of_training DATE,
    location_of_training VARCHAR(150),
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

-- Employee Table
CREATE TABLE Employee (
    SSN CHAR(9) PRIMARY KEY,
    salary DECIMAL(10, 2),
    marital_status VARCHAR(10),
    hire_date DATE,
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

-- Insurance Policy Table
CREATE TABLE Insurance_Policy (
    policy_id CHAR(100) PRIMARY KEY,
    provider_name VARCHAR(150),
    provider_address VARCHAR(200),
    policy_type VARCHAR(50)
);

-- Needs Table (Associates clients with specific needs and importance levels)
CREATE TABLE Needs (
    SSN CHAR(9),
    need_type VARCHAR(100),
    importance INT,
    PRIMARY KEY (SSN, need_type),
    FOREIGN KEY (SSN) REFERENCES Client(SSN)
);

-- Teams Table
CREATE TABLE Teams (
    team_name VARCHAR(50) PRIMARY KEY,
    team_type VARCHAR(50),
    date_formed DATE
);

-- Index on date_formed to optimize date-based queries on Teams
CREATE INDEX idx_teams_date_formed ON Teams(date_formed);

-- Employee Expenses Table
CREATE TABLE Emp_Expenses (
    SSN CHAR(9),
    expense_amount DECIMAL(10, 2),
    expense_date DATE,
    expense_description VARCHAR(100),
    PRIMARY KEY (SSN, expense_amount, expense_date, expense_description),
    FOREIGN KEY (SSN) REFERENCES Employee(SSN)
);

-- Index to optimize date-based retrievals on Emp_Expenses
CREATE INDEX idx_emp_expenses_ssn_date ON Emp_Expenses(SSN, expense_date);

-- Donor Table
CREATE TABLE Donor (
    SSN CHAR(9) PRIMARY KEY,
    anonymous BIT,
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

-- Check Donations Table
CREATE TABLE [Check] (
    SSN CHAR(9),
    donation_date DATE,
    donation_type VARCHAR(20),
    donation_amount DECIMAL(10, 2),
    campaign_name VARCHAR(50),
    check_number CHAR(10),
    PRIMARY KEY (SSN, donation_date, donation_type, donation_amount, campaign_name, check_number),
    FOREIGN KEY (SSN) REFERENCES Donor(SSN)
);

-- Credit Card Donations Table
CREATE TABLE Credit_Card (
    SSN CHAR(9),
    donation_date DATE,
    donation_type VARCHAR(20),
    donation_amount DECIMAL(10, 2),
    campaign_name VARCHAR(50),
    credit_card_no CHAR(16),
    credit_card_type VARCHAR(20),
    credit_card_expiry_date DATE,
    PRIMARY KEY (SSN, donation_date, donation_type, donation_amount, campaign_name, credit_card_no),
    FOREIGN KEY (SSN) REFERENCES Donor(SSN)
);

-- HasPolicy Table (Associates clients with insurance policies)
CREATE TABLE HasPolicy (
    SSN CHAR(9),
    policy_id CHAR(100),
    PRIMARY KEY (SSN, policy_id),
    FOREIGN KEY (SSN) REFERENCES Client(SSN),
    FOREIGN KEY (policy_id) REFERENCES Insurance_Policy(policy_id)
);

-- assignedTo Table (Associates clients with teams)
CREATE TABLE assignedTo (
    SSN CHAR(9),
    team_name VARCHAR(50),
    isActive VARCHAR(20),
    PRIMARY KEY (SSN, team_name),
    FOREIGN KEY (SSN) REFERENCES Client(SSN),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- worksIn Table (Associates volunteers with teams and tracks work hours)
CREATE TABLE worksIn (
    SSN CHAR(9),
    team_name VARCHAR(50),
    hours_worked INT,
    worked_month VARCHAR(50),
    isActive VARCHAR(10),
    PRIMARY KEY (SSN, team_name),
    FOREIGN KEY (SSN) REFERENCES Volunteer(SSN),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- ReportTo Table (Associates teams with reporting employees)
CREATE TABLE reportTo (
    team_name VARCHAR(50),
    SSN CHAR(9),
    report_date DATE,
    report_description VARCHAR(100),
    PRIMARY KEY (team_name, SSN),
    FOREIGN KEY (SSN) REFERENCES Employee(SSN),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- Leads Table (Indicates which volunteer leads a specific team)
CREATE TABLE leads (
    SSN CHAR(9),
    team_name VARCHAR(50),
    PRIMARY KEY (team_name, SSN),
    FOREIGN KEY (SSN) REFERENCES Volunteer(SSN),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- Index on team_name and SSN for optimized queries on team leaders
CREATE INDEX idx_leads_team_name_ssn ON leads(team_name, SSN);

