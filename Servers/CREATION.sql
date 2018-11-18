CREATE DATABASE IF NOT EXISTS `tiwbnb`;
USE `tiwbnb`;

DROP TABLE IF EXISTS MESSAGES_ADMIN CASCADE;
DROP TABLE IF EXISTS MESSAGES CASCADE;
DROP TABLE IF EXISTS BOOKING CASCADE;
DROP TABLE IF EXISTS HOME CASCADE;
DROP TABLE IF EXISTS ADMIN CASCADE;
DROP TABLE IF EXISTS USER CASCADE;


CREATE TABLE USER (
  USER_ID INT NOT NULL AUTO_INCREMENT,
  USER_EMAIL VARCHAR(25) NOT NULL,
  USER_NAME VARCHAR(50) NOT NULL,
  USER_SURNAME VARCHAR(20) NOT NULL,
  USER_PASSWORD VARCHAR(20) NOT NULL,
  USER_BIRTHDATE DATE NOT NULL,
  CONSTRAINT PK_USER PRIMARY KEY (USER_ID),
  CONSTRAINT UNIQUE_USER UNIQUE (USER_EMAIL)
);

CREATE TABLE ADMIN (
  ADMIN_ID INT NOT NULL AUTO_INCREMENT,
  ADMIN_EMAIL VARCHAR(25) NOT NULL,
  ADMIN_PASSWORD VARCHAR(20) NOT NULL,
  CONSTRAINT PK_ADMIN PRIMARY KEY (ADMIN_ID),
  CONSTRAINT UNIQUE_ADMIN UNIQUE (ADMIN_EMAIL)
);

CREATE TABLE HOME (
  HOME_ID INT NOT NULL AUTO_INCREMENT,
  HOME_NAME VARCHAR(30) NOT NULL,
  HOME_EMAIL VARCHAR(25) NOT NULL,
  HOME_CITY VARCHAR(25) NOT NULL,
  HOME_DESCRIPTION_FULL VARCHAR(200) NOT NULL,
  HOME_DESCRIPTION_SHORT VARCHAR(50) NOT NULL,
  HOME_TYPE VARCHAR(3) NOT NULL,
  HOME_GUESTS INT(3) NOT NULL,
  HOME_PHOTOS VARCHAR(45),
  HOME_PRICE_NIGHT DECIMAL(6,2) NOT NULL,
  HOME_AV_DATE_INIT DATE NOT NULL,
  HOME_AV_DATE_FIN DATE NOT NULL,
  CONSTRAINT PK_HOME PRIMARY KEY (HOME_ID),
  CONSTRAINT UNIQUE_HOME UNIQUE (HOME_NAME, HOME_EMAIL),
  CONSTRAINT FK_HOME FOREIGN KEY (HOME_EMAIL) REFERENCES USER (USER_EMAIL) ON DELETE CASCADE
);

CREATE TABLE BOOKING (
  BOOKING_ID INT NOT NULL AUTO_INCREMENT,
  BOOKING_USER_ID INT NOT NULL,
  BOOKING_HOME_ID INT NOT NULL,
  BOOKING_DATE_IN DATE NOT NULL,
  BOOKING_DATE_OUT DATE NOT NULL,
  BOOKING_CARD_NUM INT(16) NOT NULL,
  BOOKING_EXP_CODE VARCHAR(6) NOT NULL,
  BOOKING_CV2 INT(3) NOT NULL,
  BOOKING_CONFIRMED BOOLEAN NOT NULL,
  CONSTRAINT PK_BOOKING PRIMARY KEY (BOOKING_ID),
  CONSTRAINT FK_BOOKING1 FOREIGN KEY (BOOKING_USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
  CONSTRAINT FK_BOOKING2 FOREIGN KEY (BOOKING_HOME_ID) REFERENCES HOME (HOME_ID) ON DELETE CASCADE
);

CREATE TABLE MESSAGES (
  MESSAGE_ID INT NOT NULL AUTO_INCREMENT,
  MESSAGE_SENDER_ID INT NOT NULL,
  MESSAGE_RECEIVER_ID INT NOT NULL,
  MESSAGE_CONTENT TEXT NOT NULL,
  MESSAGE_DATE DATE NOT NULL,
  MESSAGE_READ BOOLEAN NOT NULL,
  CONSTRAINT PK_MESSAGES PRIMARY KEY (MESSAGE_ID),
  CONSTRAINT FK_MESSAGES1 FOREIGN KEY (MESSAGE_SENDER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
  CONSTRAINT FK_MESSAGES2 FOREIGN KEY (MESSAGE_RECEIVER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

CREATE TABLE MESSAGES_ADMIN (
  MESSAGE_ID INT NOT NULL AUTO_INCREMENT,
  MESSAGE_ADMIN_ID INT NOT NULL,
  MESSAGE_USER_ID INT NOT NULL,
  MESSAGE_CONTENT TEXT NOT NULL,
  MESSAGE_DATE DATE NOT NULL,
  MESSAGE_READ BOOLEAN NOT NULL,
  MESSAGE_FROM_ADMIN BOOLEAN NOT NULL,
  CONSTRAINT PK_MESSAGES_ADMIN PRIMARY KEY (MESSAGE_ID),
  CONSTRAINT FK_MESSAGES_ADMIN1 FOREIGN KEY (MESSAGE_ADMIN_ID) REFERENCES ADMIN (ADMIN_ID) ON DELETE CASCADE,
  CONSTRAINT FK_MESSAGES_ADMIN2 FOREIGN KEY (MESSAGE_USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

INSERT INTO `USER` VALUES (1, 'mail@ibm.com', 'Fernando', 'Garcia', 'secret', '1997-07-18');
INSERT INTO `USER` VALUES (2, 'mail@redsys.es', 'Fernando', 'Piqueras', 'password', '1997-06-15');
INSERT INTO `USER` VALUES (3, 'toni@ibm.com', 'Toni', 'Surname', 'tony', '1997-06-11');
INSERT INTO `USER` VALUES (4, 'guille@ibm.com', 'Guille', 'Surname', 'guille', '1997-03-15');
INSERT INTO `USER` VALUES (5, 'example@ibm.com', 'dummy', 'Insert', 'secret', '2017-06-15');
INSERT INTO `USER` VALUES (6, 'example@amazon.com', 'user', 'Surname', 'user', '2011-09-19');

INSERT INTO `ADMIN` VALUE (1, 'sysadmin', 'admin');
INSERT INTO `ADMIN` VALUE (2, 'ibm', 'admin');
INSERT INTO `ADMIN` VALUE (3, 'tzarraon@inf.uc3m.es', 'admin');

INSERT INTO `HOME` VALUE (1, 'Loft', 'mail@ibm.com', 'Madrid', 'Full Description', 'Short Description', 
'apt', 5, 'path/image/loft', 420, '20180101', '20181231');
INSERT INTO `HOME` VALUE (2, 'Condo', 'mail@ibm.com', 'NY', 'Full Description', 'Short Description', 
'pri', 1, 'path/image/condo', 70, '20180101', '20181231');
INSERT INTO `HOME` VALUE (3, 'Suite', 'mail@redsys.es', 'Madrid', 'Full Description', 'Short Description', 
'apt', 3, 'path/image/suite', 1337.70, '20180101', '20181231');
