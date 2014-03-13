CREATE DATABASE courseplanner;

USE courseplanner;

CREATE TABLE IF NOT EXISTS degree (
	degree_id INT PRIMARY KEY auto_increment,
	specialization VARCHAR(40) DEFAULT "",
	major VARCHAR(40) NOT NULL,
	department VARCHAR(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
	course_id INT PRIMARY KEY auto_increment,
	department VARCHAR(8) NOT NULL,
	course_number VARCHAR(4) NOT NULL,
	course_title VARCHAR(50) NOT NULL,
	credits INT NOT NULL,
	course_description TEXT
);

CREATE TABLE IF NOT EXISTS completion_req (
	degree_id INT,
	selection_set_id INT PRIMARY KEY auto_increment,
	selection_set_req INT NOT NULL,
	foreign key (degree_id) references degree(degree_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS course_offerings (
	quarter VARCHAR(20),
	year YEAR(4),
	course_id INT,
	primary key (course_id, quarter, year),
	foreign key (course_id) references courses(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS selection_sets (
	selection_set_id INT,
	course_id INT,
	selection_set_title TEXT,
	primary key (selection_set_id, course_id),
	foreign key (course_id) references courses(course_id) ON DELETE RESTRICT,
	foreign key (selection_set_id) references completion_req(selection_set_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS prereqs (
	course_id INT,
	prereq_id INT NOT NULL,
	primary key (course_id, prereq_id),
	foreign key (course_id) references courses(course_id) ON DELETE CASCADE,
	foreign key (prereq_id) references courses(course_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS schedule( 
	quarter varchar(20),
	year int,
	course_id int,
	FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

/* ***************** Test Data ***************** */
INSERT INTO courses (course_title, course_number, department, credits, course_description) VALUES ('Digital Logic', '151', 'CS', '4', 'Boolean algebra. Design/analysis of combinational and sequential systems using SSI/MSI/LSI modules. Number systems. Error detection and correction codes. Arithmetic algorithms. Hardware/firmware implementation of algorithms.');
INSERT INTO courses (course_title, course_number, department, credits, course_description) VALUES ('Intro to Optimization', '169', 'CS', '4', 'A broad introduction to optimization. Unconstrained and constrained optimization. Equality and inequality constraints. Linear and integer programming. Stochastic dynamic programming.');
INSERT INTO courses (course_title, course_number, department, credits, course_description) VALUES ('Intro to Data Management', '122A', 'CS', '4', 'Introduction to the design of databases and the use of database management systems (DBMS) for applications. Topics include entity-relationship modeling for design, relational data model, relational algebra, relational design theory, and Structured Query Language (SQL) programming.');
INSERT INTO courses (course_title, course_number, department, credits, course_description) VALUES ('Intro to Data Management Project', '122B', 'CS', '4', '');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('Intro to Data AI', '171', 'CS', '4');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('Programming Languages', '141', 'CS', '4');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('OS', '143A', 'CS', '4');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('OS Project', '143B', 'CS', '4');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('Java I', '21', 'ICS', '6');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('Java II', '22', 'ICS', '6');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('Java Data Structures', '23', 'ICS', '6');
INSERT INTO courses (course_title, course_number, department, credits) VALUES ('RISC Architecture', '152', 'CS', '4');

INSERT INTO degree (major, department) VALUES ('Computer Science', 'CS');
INSERT INTO degree (specialization, major, department) VALUES ('Software Development', 'Informatics', 'IN4');
INSERT INTO degree (specialization, major, department) VALUES ('Networking', 'Computer Science', 'CS');
INSERT INTO degree (major, department) VALUES ('BIM', 'ICS');

INSERT INTO course_offerings (quarter, year, course_id) VALUES ('Fall', '2013', '2');
INSERT INTO course_offerings (quarter, year, course_id) VALUES ('Spring', '2014', '3');
INSERT INTO course_offerings (quarter, year, course_id) VALUES ('Winter', '2013', '1');
INSERT INTO course_offerings (quarter, year, course_id) VALUES ('Fall', '2016', '3');

INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '1' FROM degree WHERE major="Computer Science" AND specialization="Networking";
INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '2' FROM degree WHERE major="Informatics" AND specialization="Software Development";
INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '1' FROM degree WHERE major="BIM" AND specialization='';
INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '1' FROM degree WHERE major="BIM" AND specialization='';
INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '1' FROM degree WHERE major="BIM" AND specialization='';
INSERT INTO completion_req (degree_id, selection_set_req) SELECT degree_id, '1' FROM degree WHERE major="BIM" AND specialization='';

INSERT INTO selection_sets (selection_set_id, course_id) SELECT "1", course_id FROM courses WHERE course_number = "151" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "1", course_id FROM courses WHERE course_number = "152" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "2", course_id FROM courses WHERE course_number = "21" AND department = "ICS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "2", course_id FROM courses WHERE course_number = "22" AND department = "ICS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "2", course_id FROM courses WHERE course_number = "23" AND department = "ICS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "3", course_id FROM courses WHERE course_number = "122A" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "3", course_id FROM courses WHERE course_number = "143A" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "4", course_id FROM courses WHERE course_number = "169" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "4", course_id FROM courses WHERE course_number = "171" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id, selection_set_title) SELECT "5", course_id, "Project courses" FROM courses WHERE course_number = "122B" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id, selection_set_title) SELECT "5", course_id, "Project courses" FROM courses WHERE course_number = "143B" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "6", course_id FROM courses WHERE course_number = "21" AND department = "CS";
INSERT INTO selection_sets (selection_set_id, course_id) SELECT "6", course_id FROM courses WHERE course_number = "22" AND department = "CS";

INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '22' AND t1.department = 'ICS' AND t2.course_number = '21' AND t1.department = 'ICS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '23' AND t1.department = 'ICS' AND t2.course_number = '22' AND t1.department = 'ICS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '151' AND t1.department = 'CS' AND t2.course_number = '23' AND t1.department = 'ICS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '169' AND t1.department = 'CS' AND t2.course_number = '23' AND t1.department = 'ICS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '152' AND t1.department = 'CS' AND t2.course_number = '151' AND t1.department = 'CS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '122A' AND t1.department = 'CS' AND t2.course_number = '23' AND t1.department = 'ICS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '122B' AND t1.department = 'CS' AND t2.course_number = '122A' AND t1.department = 'CS';
INSERT INTO prereqs (course_id, prereq_id) SELECT t1.course_id, t2.course_id FROM courses AS t1 JOIN courses AS t2 WHERE t1.course_number = '122B' AND t1.department = 'CS' AND t2.course_number = '141' AND t1.department = 'CS';