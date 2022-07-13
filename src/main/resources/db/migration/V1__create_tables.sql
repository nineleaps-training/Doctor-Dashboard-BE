	CREATE TABLE `login_details` (
	  `id` bigint NOT NULL AUTO_INCREMENT,
	  `domain` varchar(30) NOT NULL,
	  `email_id` varchar(80) NOT NULL,
	  `name` varchar(50) NOT NULL,
	  `profile_pic` varchar(255) NOT NULL,
	  `role` varchar(8) NOT NULL,
	  PRIMARY KEY (`id`),
	  UNIQUE KEY `UK_n0utq2b38h07gqiu2v6mperw2` (`email_id`),
	  UNIQUE KEY `UK_2bje4c6xb4uyl1wdp4hi0n26j` (`profile_pic`)
	) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `doctor_details` (
	  `id` bigint NOT NULL AUTO_INCREMENT,
	  `age` smallint NOT NULL,
	  `degree` varchar(250) NOT NULL,
	  `experience` smallint NOT NULL,
	  `gender` varchar(10) NOT NULL,
	  `login_id` bigint NOT NULL,
	  `phone_no` varchar(10) NOT NULL,
	  `speciality` varchar(20) NOT NULL,
	  PRIMARY KEY (`id`),
	  KEY `FK3nwns289elorm83symvr547s0` (`login_id`),
	  CONSTRAINT `FK3nwns289elorm83symvr547s0` FOREIGN KEY (`login_id`) REFERENCES `login_details` (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `todolist` (
	  `id` bigint NOT NULL AUTO_INCREMENT,
	  `description` varchar(50) DEFAULT NULL,
	  `status` bit(1) DEFAULT NULL,
	  `doctor_id` bigint NOT NULL,
	  PRIMARY KEY (`id`),
	  KEY `FK51nuktw2hwtjxpcey8uq64blc` (`doctor_id`),
	  CONSTRAINT `FK51nuktw2hwtjxpcey8uq64blc` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_details` (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `patients` (
	  `id` bigint NOT NULL AUTO_INCREMENT,
	  `address` varchar(255) DEFAULT NULL,
	  `age` int NOT NULL,
	  `alternate_mobile_no` varchar(10) DEFAULT NULL,
	  `blood_group` varchar(3) DEFAULT NULL,
	  `gender` varchar(10) DEFAULT NULL,
	  `mobile_no` varchar(10) DEFAULT NULL,
	  `timestamp` datetime NOT NULL,
	  `login_id` bigint DEFAULT NULL,
	  PRIMARY KEY (`id`),
	  UNIQUE KEY `UK_4iu3f3u5aai748to39n457f77` (`login_id`),
	  CONSTRAINT `FKkh6kv3jvwnmr8py9fb4546g8k` FOREIGN KEY (`login_id`) REFERENCES `login_details` (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `appointments` (
	  `appoint_id` bigint NOT NULL AUTO_INCREMENT,
	  `appointment_time` time NOT NULL,
	  `category` varchar(20) NOT NULL,
	  `date_of_appointment` date NOT NULL,
	  `doctor_name` varchar(50) NOT NULL,
	  `follow_up_appointment_id` bigint DEFAULT NULL,
	  `is_booked_again` bit(1) DEFAULT NULL,
	  `is_read` bit DEFAULT 0,
	  `patient_email` varchar(50) NOT NULL,
	  `patient_name` varchar(50) NOT NULL,
	  `status` varchar(15) NOT NULL,
	  `symptoms` varchar(100) DEFAULT NULL,
	  `timestamp` datetime NOT NULL,
	  `doctor_id` bigint NOT NULL,
	  `patient_id` bigint NOT NULL,
	  PRIMARY KEY (`appoint_id`),
	  KEY `inedx_fn` (`doctor_id`,`patient_id`,`date_of_appointment`),
	  KEY `FK8exap5wmg8kmb1g1rx3by21yt` (`patient_id`),
	  CONSTRAINT `FK8exap5wmg8kmb1g1rx3by21yt` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
	  CONSTRAINT `FKqbclut35x0bpfbd2iy91khj8u` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_details` (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `attributes` (
	  `aid` bigint NOT NULL AUTO_INCREMENT,
	  `blood_pressure` varchar(10) DEFAULT NULL,
	  `body_temp` double DEFAULT NULL,
	  `glucose_level` bigint DEFAULT NULL,
	  `prescription` varchar(100) DEFAULT NULL,
	  `appointment_id` bigint DEFAULT NULL,
	  PRIMARY KEY (`aid`),
	  KEY `FKhed7ejdnur5s8t3n54jv2bj5f` (`appointment_id`),
	  CONSTRAINT `FKhed7ejdnur5s8t3n54jv2bj5f` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appoint_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `prescriptions` (
	  `pres_id` bigint NOT NULL AUTO_INCREMENT,
	  `days` bigint DEFAULT NULL,
	  `drug_name` varchar(50) DEFAULT NULL,
	  `quantity` bigint DEFAULT NULL,
	  `time` varchar(10) DEFAULT NULL,
	  `type` varchar(10) DEFAULT NULL,
	  `appointment_id` bigint DEFAULT NULL,
	  PRIMARY KEY (`pres_id`),
	  KEY `FKe2fpvlkkcgcd40k4ufyyju2al` (`appointment_id`),
	  CONSTRAINT `FKe2fpvlkkcgcd40k4ufyyju2al` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appoint_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	CREATE TABLE `files` (
	  `report_id` bigint NOT NULL AUTO_INCREMENT,
	  `appointment_id` bigint DEFAULT NULL,
	  `data_report` longblob,
	  `name` varchar(255) DEFAULT NULL,
	  `type` varchar(255) DEFAULT NULL,
	  PRIMARY KEY (`report_id`),
	  UNIQUE KEY `UK_bksqq0ss48puwt9j9oofrnpa3` (`appointment_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;