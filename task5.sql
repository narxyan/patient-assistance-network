-- sql Quries
--1. Enter a new team into the database (1/month).
--2. Enter a new client into the database and associate him or her with one or more teams (1/week).
--3. Enter a new volunteer into the database and associate him or her with one or more teams
--(2/month).
--4. Enter the number of hours a volunteer worked this month for a particular team (30/month).
--5. Enter a new employee into the database and associate him or her with one or more teams
--(1/year).
--6. Enter an expense charged by an employee (1/day).
--7. Enter a new donor and associate him or her with several donations (1/day).
--8. Retrieve the name and phone number of the doctor of a particular client (1/week).
--9. Retrieve the total amount of expenses charged by each employee for a particular period of
--time. The list should be sorted by the total amount of expenses (1/month).
--10. Retrieve the list of volunteers that are members of teams that support a particular client
--(4/year).
--11. Retrieve the names of all teams that were founded after a particular date (1/month).
--12. Retrieve the names, social security numbers, contact information, and emergency contact
--information of all people in the database (1/week).
--13. Retrieve the name and total amount donated by donors that are also employees. The list
--should be sorted by the total amount of the donations, and indicate if each donor wishes to
--remain anonymous (1/week)
--14. Increase the salary by 10% of all employees to whom more than one team must report. (1/year)
--15. Delete all clients who do not have health insurance and whose value of importance for
--transportation is less than 5 (4/year). 



-- Initially the tables are empty so we need to populate them so that it dosent give us forigen Key constraints and other errors 
--Create New Records Sequentially: Since each table starts empty, we need to 
--populate them in a way that satisfies any foreign key constraints. For example, when adding a Client or Volunteer, their corresponding record must first exist in the Person table.
--Check for Foreign Key Relationships: To avoid foreign key violations in an initially empty database, entries are added in an order that respects dependencies:

-- inserting into Person and Insurance policy and teams to populate the table 
-- Insert basic records into Person using Anme Chars
-- this is the dummy Data which i am using to populate the data base to execute task 5.

INSERT INTO Person (ssn, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list)
VALUES
('101010101', 'Goku Son', 'M', 'Martial Artist', '123 Kame House Rd', 'goku.son@gmail.com', '5551010101', 1),
('202020202', 'Naruto Uzumaki', 'M', 'Hokage', '7 Hokage St', 'naruto.uzumaki@gmail.com', '5552020202', 1),
('303030303', 'Sakura Haruno', 'F', 'Medical Ninja', '8 Sakura Ln', 'sakura.haruno@gmail.com', '5553030303', 0),
('404040404', 'Edward Elric', 'M', 'Alchemist', '24 Resembool St', 'edward.elric@gmail.com', '5554040404', 1),
('505050505', 'Mikasa Ackerman', 'F', 'Soldier', '12 Wall Maria Rd', 'mikasa.ackerman@gmail.com', '5555050505', 1),
('606060606', 'Luffy Monkey D.', 'M', 'Pirate', '1 Thousand Sunny', 'luffy.monkeyd@gmail.com', '5556060606', 1),
('707070707', 'Levi Ackerman', 'M', 'Captain', '9 Wall Rose Ave', 'levi.ackerman@gmail.com', '5557070707', 0),
('808080808', 'Nami', 'F', 'Navigator', '15 Grand Line St', 'nami.navigates@gmail.com', '5558080808', 1),
('909090909', 'Ichigo Kurosaki', 'M', 'Soul Reaper', '8 Karakura St', 'ichigo.kurosaki@gmail.com', '5559090909', 1),
('111111111', 'Light Yagami', 'M', 'Student', '1 Justice Ln', 'light.yagami@gmail.com', '5551111111', 0),
('222222222', 'Saitama', 'M', 'Hero', '23 Z-City Rd', 'saitama.hero@gmail.com', '5552222222', 1),
('333333333', 'Eren Yeager', 'M', 'Soldier', '5 Shiganshina St', 'eren.yeager@gmail.com', '5553333333', 1),
('444444444', 'Hinata Hyuga', 'F', 'Ninja', '18 Hyuga Clan Rd', 'hinata.hyuga@gmail.com', '5554444444', 1),
('555555555', 'Bulma Briefs', 'F', 'Scientist', '16 Capsule Corp', 'bulma.briefs@gmail.com', '5555555555', 0),
('666666666', 'Vegeta', 'M', 'Prince of Saiyans', '789 Saiyan Blvd', 'vegeta.prince@gmail.com', '5556666666', 0);
INSERT INTO Emp_Expenses (SSN, expense_amount, expense_date, expense_description)
VALUES 
    ('999111777', 150.00, '2024-10-15', 'Training Expenses'),
    ('999111777', 200.00, '2024-10-20', 'Office Supplies');
-- Insert person who is both an employee and a donor
INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list)
VALUES ('111222333', 'Jonathan Joestar', 'M', 'Philanthropist', '45 Joestar Mansion', 'jonathan.joestar@gmail.com', '5551234567', 1);

-- Insert into Employee table
INSERT INTO Employee (SSN, salary, marital_status, hire_date)
VALUES ('111222333', 95000.00, 'Married', '2023-01-15');

-- Insert into Donor table
INSERT INTO Donor (SSN, anonymous)
VALUES ('111222333', 0);

-- Insert a donation via Check
INSERT INTO [Check] (SSN, donation_date, donation_type, donation_amount, campaign_name, check_number)
VALUES ('111222333', '2024-10-01', 'Check', 300.00, 'Charity Event', 'CHK001');

-- Insert a donation via Credit Card
INSERT INTO Credit_Card (SSN, donation_date, donation_type, donation_amount, campaign_name, credit_card_no, credit_card_type, credit_card_expiry_date)
VALUES ('111222333', '2024-10-05', 'Credit', 500.00, 'Annual Gala', '1234567890123456', 'Visa', '2026-12-31');

-- populating done

--Queries

--1 . Enter a New Team into the Database (1/month)
INSERT INTO Teams (team_name, team_type, date_formed)
VALUES ('Team Zeta', 'Research', '2024-11-01');

--2. Enter a New Client and Associate with One or More Teams (1/week)
-- Insert into Person table first
INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone_number, on_mailing_list)
VALUES ('888999111', 'Chris Evans', 'M', 'Artist', '12 Elm St', 'chrisevans@example.com', '555-8888', 1);

-- Insert into Client table
INSERT INTO Client (SSN, doctor_name, doctor_phone_no, date_assigned)
VALUES ('888999111', 'Dr. Blake', '555-7777', '2024-10-15');

-- Associate the client with a team
INSERT INTO assignedTo (SSN, team_name, isActive)
VALUES ('888999111', 'Team Zeta', 'Active');

--3. Enter a New Volunteer and Associate with One or More Teams (2/month)
-- Insert into Person table first
INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone_number, on_mailing_list)
VALUES ('777888999', 'Olivia Lee', 'F', 'Volunteer', '99 Maple St', 'olivialee@example.com', '555-9999', 1);

-- Insert into Volunteer table
INSERT INTO Volunteer (SSN, date_joined, date_of_training, location_of_training)
VALUES ('777888999', '2024-09-01', '2024-09-05', 'New York');

-- Associate the volunteer with a team
INSERT INTO worksIn (SSN, team_name, hours_worked, worked_month, isActive)
VALUES ('777888999', 'Team Zeta', 0, 'November', 'Active');


--4. Enter the Number of Hours a Volunteer Worked This Month for a Particular Team (30/month)
UPDATE worksIn
SET hours_worked = hours_worked + 10  -- Adding 10 hours as an example
WHERE SSN = '777888999' AND team_name = 'Team Zeta' AND worked_month = 'November';

--5. Enter a New Employee and Associate with One or More Teams (1/year)

-- Insert into Person table first
INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone_number, on_mailing_list)
VALUES ('666777888', 'Lucas Brown', 'M', 'Engineer', '123 Pine St', 'lucasbrown@example.com', '555-6666', 1);

-- Insert into Employee table
INSERT INTO Employee (SSN, salary, marital_status, hire_date)
VALUES ('666777888', 80000.00, 'Married', '2024-08-01');

-- Associate the employee with a team
INSERT INTO reportTo (team_name, SSN, report_date, report_description)
VALUES ('Team Zeta', '666777888', '2024-11-05', 'Monthly Report');

--6. Enter an Expense Charged by an Employee (1/day)
INSERT INTO Emp_Expenses (SSN, expense_amount, expense_date, expense_description)
VALUES ('666777888', 120.00, '2024-11-05', 'Travel Expenses');

--7. Enter a New Donor and Associate with Several Donations (1/day)
-- Insert into Person table first
INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone_number, on_mailing_list)
VALUES ('555444333', 'Emma White', 'F', 'Donor', '101 Oak St', 'emmawhite@example.com', '555-4444', 1);

-- Insert into Donor table
INSERT INTO Donor (SSN, anonymous)
VALUES ('555444333', 0);

-- Add donations in Check and Credit_Card tables
INSERT INTO [Check] (SSN, donation_date, donation_type, donation_amount, campaign_name, check_number)
VALUES ('555444333', '2024-11-05', 'Check', 300.00, 'Year End Fund', 'CHK003');

INSERT INTO Credit_Card (SSN, donation_date, donation_type, donation_amount, campaign_name, credit_card_no, credit_card_type, credit_card_expiry_date)
VALUES ('555444333', '2024-11-06', 'Credit', 500.00, 'Year End Fund', '8765432187654321', 'Visa', '2027-09-30');

--8. Retrieve the Name and Phone Number of the Doctor of a Particular Client (1/week)

SELECT person_name, doctor_phone_no
FROM Person P
JOIN Client C ON P.SSN = C.SSN
WHERE C.SSN = '888999111';


---9. Retrieve the Total Amount of Expenses Charged by Each Employee for a Particular Period, Sorted by Total Expenses (1/month)
SELECT E.SSN, P.person_name, SUM(expense_amount) AS TotalExpenses
FROM Emp_Expenses E
JOIN Person P ON E.SSN = P.SSN
WHERE expense_date BETWEEN '2024-10-01' AND '2024-10-31'
GROUP BY E.SSN, P.person_name
ORDER BY TotalExpenses DESC;

--10. Retrieve the List of Volunteers Who Are Members of Teams That Support a Particular Client (4/year)
SELECT V.SSN, P.person_name
FROM Volunteer V
JOIN worksIn WI ON V.SSN = WI.SSN
JOIN assignedTo AT ON WI.team_name = AT.team_name
JOIN Person P ON V.SSN = P.SSN
WHERE AT.SSN = '888999111';


--11. Retrieve the Names of All Teams That Were Founded After a Particular Date (1/month)
SELECT team_name
FROM Teams
WHERE date_formed > '2024-01-01';

--12. Retrieve Names, SSNs, Contact Information, and Emergency Contact Information of All People (1/week)
SELECT P.person_name, P.SSN, P.mailing_address, P.email, P.phone_number, EC.emergency_name, EC.phone_number AS emergency_phone, EC.relation
FROM Person P
LEFT JOIN Emergency_Contact EC ON P.SSN = EC.SSN;

--13. Retrieve the Name and Total Donation Amount by Donors Who Are Also Employees, Sorted by Total Donations and Indicate Anonymity (1/week)

SELECT P.person_name, D.SSN, SUM(ISNULL(C.donation_amount, 0) + ISNULL(CR.donation_amount, 0)) AS TotalDonations, D.anonymous
FROM Donor D
JOIN Person P ON D.SSN = P.SSN
LEFT JOIN [Check] C ON D.SSN = C.SSN
LEFT JOIN Credit_Card CR ON D.SSN = CR.SSN
JOIN Employee E ON D.SSN = E.SSN
GROUP BY P.person_name, D.SSN, D.anonymous
ORDER BY TotalDonations DESC;

--14. Increase the Salary by 10% for Employees to Whom More Than One Team Must Report (1/year)
UPDATE Employee
SET salary = salary * 1.10
WHERE SSN IN (
    SELECT SSN
    FROM reportTo
    GROUP BY SSN
    HAVING COUNT(team_name) > 1
);


--Qurey 15  :15. Delete All Clients Who Do Not Have Health Insurance and Whose Importance for Transportation Is Less Than 5 (4/year)
--  to execute it without error  we need to delete:  
-- Delete from assignedTo where the Client SSN meets the condition
DELETE FROM assignedTo
WHERE SSN IN (
    SELECT C.SSN
    FROM Client C
    LEFT JOIN HasPolicy HP ON C.SSN = HP.SSN 
        AND HP.policy_id IN (SELECT policy_id FROM Insurance_Policy WHERE policy_type = 'Health')
    LEFT JOIN Needs N ON C.SSN = N.SSN AND N.need_type = 'Transportation'
    WHERE HP.policy_id IS NULL AND (N.importance < 5 OR N.importance IS NULL)
);

-- Now delete from Client
DELETE FROM Client
WHERE SSN IN (
    SELECT C.SSN
    FROM Client C
    LEFT JOIN HasPolicy HP ON C.SSN = HP.SSN 
        AND HP.policy_id IN (SELECT policy_id FROM Insurance_Policy WHERE policy_type = 'Health')
    LEFT JOIN Needs N ON C.SSN = N.SSN AND N.need_type = 'Transportation'
    WHERE HP.policy_id IS NULL AND (N.importance < 5 OR N.importance IS NULL)
);


