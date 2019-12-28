-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 16, 2019 at 10:00 PM
-- Server version: 10.3.15-MariaDB
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pvg_coet`
--

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `hod_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`id`, `name`, `hod_id`) VALUES
(1, 'Computer', 1),
(3, 'Information Technology', 5);

-- --------------------------------------------------------

--
-- Table structure for table `faculty`
--

CREATE TABLE `faculty` (
  `id` int(11) NOT NULL,
  `first_name` varchar(25) NOT NULL,
  `last_name` varchar(25) NOT NULL,
  `department_id` int(11) NOT NULL,
  `phone` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `faculty`
--

INSERT INTO `faculty` (`id`, `first_name`, `last_name`, `department_id`, `phone`) VALUES
(1, 'Manisha', 'Marathe', 1, '9845167849'),
(2, 'Anil', 'Bhadgale', 1, '7785461895'),
(3, 'Vijayalakshmi', 'Kanade', 1, '8983547891'),
(4, 'Adityakumar', 'Dongare', 1, '9890451786'),
(5, 'Shubhangi', 'Dixit', 3, '9422185769'),
(6, 'Minal', 'Apsangi', 3, '7704159854');

-- --------------------------------------------------------

--
-- Table structure for table `marks`
--

CREATE TABLE `marks` (
  `id` int(11) NOT NULL,
  `student_roll_num` int(11) NOT NULL DEFAULT 1,
  `subject_id` int(11) NOT NULL DEFAULT 1,
  `marks` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `marks`
--

INSERT INTO `marks` (`id`, `student_roll_num`, `subject_id`, `marks`) VALUES
(1, 170101, 1, 80),
(2, 170101, 6, 70),
(3, 170101, 4, 90),
(4, 170101, 2, 80),
(5, 170102, 1, 80),
(6, 170102, 6, 80),
(7, 170102, 5, 90),
(8, 170102, 2, 80),
(9, 180101, 3, 70),
(10, 180102, 3, 73),
(11, 180103, 3, 80);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `roll_num` int(11) NOT NULL,
  `first_name` varchar(25) NOT NULL,
  `last_name` varchar(25) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `admission_date` date NOT NULL,
  `cet_marks` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`roll_num`, `first_name`, `last_name`, `department_id`, `phone`, `admission_date`, `cet_marks`) VALUES
(170101, 'Vinod', 'Kamat', 1, '7474986413', '2017-08-01', 107),
(170102, 'Adwait', 'Bhope', 1, '8862451783', '2017-08-01', 113),
(170103, 'Aashay', 'Zanpure', 1, '9029742685', '2017-08-21', 140),
(170104, 'Atharva', 'Dhekne', 1, '7093458923', '2017-08-21', 132),
(170301, 'Rashmi', 'Mokashi', 3, '9552514865', '2017-08-01', 125),
(180101, 'Shivam', 'Rajput', 1, '7415487529s', '2017-08-21', 141),
(180102, 'Sunanda', 'Somwase', 1, '9423165742', '2017-08-21', 130),
(180103, 'Sagar', 'Patil', 1, '9545791532', '2017-08-21', 150),
(180104, 'Shreya', 'Gore', 1, '7093458923', '2017-08-21', 131);


--
-- Triggers `students`
--
DELIMITER $$
	CREATE TRIGGER `delete_student` BEFORE DELETE ON `students` FOR EACH ROW DELETE FROM marks WHERE student_roll_num = OLD.roll_num
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `id` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `start_date` int(11) NOT NULL,
  `end_date` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `faculty_id` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`id`, `department_id`, `start_date`, `end_date`, `name`, `faculty_id`) VALUES
(1, 1, 360, 540, 'Object Oriented Programming', 1),
(2, 1, 360, 540, 'Discrete Mathematics', 2),
(3, 1, 540, 720, 'Computer Graphics', 2),
(4, 1, 720, 900, 'Database Management Systems', 3),
(5, 1, 360, 540, 'Digital Electronics And Login Design', 4),
(6, 1, 360, 540, 'Computer Organization and Architecture', 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `department_hod_id` (`hod_id`);

--
-- Indexes for table `faculty`
--
ALTER TABLE `faculty`
  ADD PRIMARY KEY (`id`),
  ADD KEY `faculty_department_id` (`department_id`);

--
-- Indexes for table `marks`
--
ALTER TABLE `marks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `marks_student_roll_num` (`student_roll_num`),
  ADD KEY `marks_subject_id` (`subject_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`roll_num`),
  ADD KEY `student_department_id` (`department_id`);

--
-- Indexes for table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`id`),
  ADD KEY `subject_department_id` (`department_id`),
  ADD KEY `subject_faculty_id` (`faculty_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `departments`
--
ALTER TABLE `departments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `faculty`
--
ALTER TABLE `faculty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `marks`
--
ALTER TABLE `marks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `roll_num` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=170331;

--
-- AUTO_INCREMENT for table `subjects`
--
ALTER TABLE `subjects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `departments`
--
ALTER TABLE `departments`
  ADD CONSTRAINT `department_hod_id` FOREIGN KEY (`hod_id`) REFERENCES `faculty` (`id`) ON UPDATE NO ACTION;

--
-- Constraints for table `faculty`
--
ALTER TABLE `faculty`
  ADD CONSTRAINT `faculty_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON UPDATE NO ACTION;

--
-- Constraints for table `marks`
--
ALTER TABLE `marks`
  ADD CONSTRAINT `marks_student_roll_num` FOREIGN KEY (`student_roll_num`) REFERENCES `students` (`roll_num`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `marks_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`) ON UPDATE NO ACTION;

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `student_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON UPDATE NO ACTION;

--
-- Constraints for table `subjects`
--
ALTER TABLE `subjects`
  ADD CONSTRAINT `subject_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `subject_faculty_id` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`id`) ON UPDATE NO ACTION;
COMMIT;


-- --------------------------------------------------------

DELIMITER $$
CREATE PROCEDURE calculate_pointer(IN roll_num INT, IN date INT, OUT pointer int)
BEGIN
	SELECT avg(marks.marks) INTO pointer

	FROM marks JOIN subjects ON marks.subject_id = subjects.id

	WHERE marks.student_roll_num = roll_num
	AND marks.subject_id IN (SELECT subjects.id FROM subjects, students	
	WHERE subjects.department_id = students.department_id
	AND subjects.start_date < date AND subjects.end_date > date
	AND students.roll_num = roll_num);

END $$	
DELIMITER ;

-- --------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE insert_marks(
    IN  s_id INT,
    IN  s_roll_num  INT,
    IN  m INT)
BEGIN
    DECLARE num INT DEFAULT 0;

    SELECT count(*)
    INTO @num
    FROM marks
    WHERE subject_id = s_id
    AND student_roll_num = s_roll_num;

    IF @num = 0 THEN
        INSERT INTO marks(student_roll_num, subject_id, marks)
        VALUES (s_roll_num, s_id, m);
    END IF;
    IF @num > 0 THEN
        UPDATE marks
        SET marks = m
        WHERE student_roll_num = s_roll_num
        AND subject_id = s_id;
    END IF;
END $$

DELIMITER ;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

