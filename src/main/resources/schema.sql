-- 1. Student Records
CREATE TABLE Student_Records (
    student_id INT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    grade VARCHAR(10),
    enrollment_date DATE
);

-- 2. Vehicle Information
CREATE TABLE Vehicle_Information (
    vehicle_id INT PRIMARY KEY,
    model VARCHAR(50),
    manufacturer VARCHAR(50),
    registration_year INT,
    price DECIMAL(10,2)
);

-- 3. Movie Listings
CREATE TABLE Movie_Listings (
    movie_id INT PRIMARY KEY,
    title VARCHAR(255),
    genre VARCHAR(50),
    release_year INT,
    duration_minutes INT
);

-- 4. Weather Reports
CREATE TABLE Weather_Reports (
    report_id INT PRIMARY KEY,
    city VARCHAR(100),
    temperature DECIMAL(5,2),
    humidity INT,
    report_date DATE
);

-- 5. Book Catalog
CREATE TABLE Book_Catalog (
    book_id INT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(100),
    publication_year INT,
    price DECIMAL(10,2)
);

-- 6. Airline Flight Schedules
CREATE TABLE Flight_Schedules (
    flight_id INT PRIMARY KEY,
    airline VARCHAR(50),
    departure_city VARCHAR(100),
    arrival_city VARCHAR(100),
    departure_time TIME
);

-- 7. Music Playlist
CREATE TABLE Music_Playlist (
    song_id INT PRIMARY KEY,
    title VARCHAR(255),
    artist VARCHAR(100),
    album VARCHAR(100),
    release_year INT
);

-- 8. Restaurant Menu
CREATE TABLE Restaurant_Menu (
    item_id INT PRIMARY KEY,
    dish_name VARCHAR(100),
    category VARCHAR(50),
    price DECIMAL(10,2),
    available BOOLEAN
);

-- 9. Space Mission Data
CREATE TABLE Space_Missions (
    mission_id INT PRIMARY KEY,
    mission_name VARCHAR(255),
    launch_date DATE,
    destination VARCHAR(100),
    duration_days INT
);

-- 10. Tech Gadgets Inventory
CREATE TABLE Tech_Gadgets (
    gadget_id INT PRIMARY KEY,
    name VARCHAR(100),
    brand VARCHAR(50),
    release_year INT,
    price DECIMAL(10,2)
);