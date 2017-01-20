-- MAKE SURE TO DELETE NEXT LINE!!!!!!!! 
DROP DATABASE if exists BombingWithFriends;

CREATE DATABASE BombingWithFriends;
USE BombingWithFriends;
CREATE TABLE `Users` (
  `ID` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `Username` varchar(100) NOT NULL,
  `Password` varchar(300) NOT NULL,
  `TotalScore` int(11) NOT NULL
);