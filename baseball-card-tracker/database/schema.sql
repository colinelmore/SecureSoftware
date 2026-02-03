CREATE TABLE Card (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    height INT,
    weight INT,
    position VARCHAR(50),
    team VARCHAR(50),
    battingAverage DECIMAL(3, 3)
);

CREATE TABLE CardStyle (
    id SERIAL PRIMARY KEY,
    manufacturer VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    edition VARCHAR(50)
);

CREATE TABLE FindCard (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    teamName VARCHAR(50),
    FOREIGN KEY (teamName) REFERENCES Card(team)
);