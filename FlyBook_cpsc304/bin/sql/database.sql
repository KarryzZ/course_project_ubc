-- Paste the SQL used in DatabaseConnection.java here

-- SQL DDL and Loading Data
-- CREATE TABLE: set up the database
CREATE TABLE time_zone (
    city varchar2(20) not null PRIMARY KEY,
    time_zone varchar2(5) not null);

CREATE TABLE user_info (
    user_id varchar2(10) not null PRIMARY KEY,
    password varchar2(10) not null,
    name varchar2(20) not null,
    city varchar2(20),
    email varchar2(100));


CREATE TABLE Individual_Chat
    (time DATE,
    sender CHAR(20), content CHAR(100),
    u_id1 CHAR(10),
    u_id2 CHAR(10),
    PRIMARY KEY (u_id1, u_id2, time),
    FOREIGN KEY (u_id1) REFERENCES User_info ON DELETE CASCADE,
    FOREIGN KEY (u_id2) REFERENCES User_info ON DELETE CASCADE);


CREATE TABLE Share_Post
    (postid CHAR(10),
    time DATE,
    content CHAR(1000),
    u_id CHAR(10) NOT NULL,
    PRIMARY KEY (postid),
    FOREIGN KEY (u_id) REFERENCES User_info);

CREATE TABLE Media
    (mediaid CHAR(10),
    mtype CHAR(10),
    PRIMARY KEY (mediaid));

CREATE TABLE


-- INSERT: load data
INSERT INTO time_zone VALUES ("Vancouver","GMT-8");
INSERT INTO time_zone VALUES ("Toronto", "GMT-5");

INSERT INTO user_info VALUES ("0000","0000","admin","Vancouver","admin@gmail.com");


-- Queries
-- DELETE Operation

-- UPDATE Operation

-- Selection
SELECT *
FROM user_info, time_zone
WHERE user_info.city = time_zone.city;

SELECT *
FROM user_info, time_zone
WHERE user_id = " + userid + "
    AND user_info.city = time_zone.city;

-- Projection
SELECT city FROM time_zone;
select user_id from user_info;
select user_id, password from user_info;

-- JOIN

-- Aggregation with GROUP BY

-- Aggregation with HAVING

-- Nested Aggregation with GROUP BY

-- Division