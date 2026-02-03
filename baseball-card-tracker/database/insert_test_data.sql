INSERT INTO Card (first_name, last_name, height, weight, position, team, batting_average) VALUES
('Babe', 'Ruth', 6.2, 215, 'Outfielder', 'Yankees', 0.342),
('Hank', 'Aaron', 6.0, 180, 'Outfielder', 'Braves', 0.305),
('Willie', 'Mays', 5.10, 180, 'Center Fielder', 'Giants', 0.302),
('Ted', 'Williams', 6.3, 205, 'Left Fielder', 'Red Sox', 0.344);

INSERT INTO CardStyle (manufacturer, year, edition) VALUES
('Topps', 1952, 'Rookie'),
('Fleer', 1980, 'All-Star'),
('Donruss', 1985, 'Limited Edition'),
('Upper Deck', 1990, 'Special Edition');

INSERT INTO FindCard (first_name, last_name, team_name) VALUES
('Babe', 'Ruth', 'Yankees'),
('Hank', 'Aaron', 'Braves'),
('Willie', 'Mays', 'Giants'),
('Ted', 'Williams', 'Red Sox');