DROP TABLE IF EXISTS appointments;
 
CREATE TABLE appointments (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  patient_name VARCHAR(250) NOT NULL,
  doctor_name VARCHAR(250) NOT NULL,
  issue VARCHAR(250) DEFAULT NULL,
  appointment_date VARCHAR(250) DEFAULT NULL,
  reference_number VARCHAR(250) DEFAULT NULL
);
 
INSERT INTO appointments (patient_name, doctor_name, issue, appointment_date, reference_number) VALUES
  ('Aliko', 'Dangote', 'Headache', '2nd Mar 2020', 'ABC123'),
  ('Rakho', 'Hari', 'Headache', '2nd May 2020', 'ABC124');