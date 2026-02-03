CREATE TABLE Card (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    height INT,
    weight INT,
    position VARCHAR(50),
    team VARCHAR(50),
    batting_average DECIMAL(3, 3)
);

CREATE TABLE CardStyle (
    id SERIAL PRIMARY KEY,
    manufacturer VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    edition VARCHAR(50),
    card_id INT,
    FOREIGN KEY (card_id) REFERENCES Card(id) ON DELETE CASCADE
);

CREATE TABLE FindCard (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    team_name VARCHAR(50),
    card_id INT,
    FOREIGN KEY (card_id) REFERENCES Card(id) ON DELETE CASCADE
);