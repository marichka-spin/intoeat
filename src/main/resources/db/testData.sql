INSERT INTO Users (username, password) VALUES ('Marichka', '123123');
INSERT INTO Users (username, password) VALUES ('Watcher', '123456');
INSERT INTO Users (username, password) VALUES ('Olga', '654321');

INSERT INTO Groups (name, description) VALUES ('Group 1', 'Group description 1');
INSERT INTO Groups (name, description) VALUES ('Group 2', 'Group description 2');
INSERT INTO Groups (name, description) VALUES ('Group 3', 'Group description 3');

INSERT INTO Details (id, about, latitude, longitude, zoom, menu) VALUES (1, 'About1', 10.11, 11.11, 0, 'Menu1');
INSERT INTO Details (id, about, latitude, longitude, zoom, menu) VALUES (2, 'About2', 10.12, 11.12, 0, 'Menu2');
INSERT INTO Details (id, about, latitude, longitude, zoom, menu) VALUES (3, 'About3', 10.13, 11.13, 0, 'Menu3');

INSERT INTO Details_Halls (Details_id, hall, hall_key) VALUES(1, 'Hall description 1', 'Hall type 1');
INSERT INTO Details_Halls (Details_id, hall, hall_key) VALUES(2, 'Hall description 2', 'Hall type 2');
INSERT INTO Details_Halls (Details_id, hall, hall_key) VALUES(3, 'Hall description 3', 'Hall type 3');

INSERT INTO Details_Entertainments (Details_id, entertainment, entertainment_key) VALUES(1, 'Entertainment Description 1', 'Entertainment 1');
INSERT INTO Details_Entertainments (Details_id, entertainment, entertainment_key) VALUES(2, 'Entertainment Description 2', 'Entertainment 2');
INSERT INTO Details_Entertainments (Details_id, entertainment, entertainment_key) VALUES(3, 'Entertainment Description 3', 'Entertainment 3');

INSERT INTO Details_FoodType (Details_id, food, food_type_key) VALUES(1, 'Food description 1', 'Food type 1');
INSERT INTO Details_FoodType (Details_id, food, food_type_key) VALUES(2, 'Food description 2', 'Food type 2');
INSERT INTO Details_FoodType (Details_id, food, food_type_key) VALUES(3, 'Food description 3', 'Food type 3');

INSERT INTO Details_ForChildren (Details_id, for_children) VALUES(1, 'ForChildren description 1');
INSERT INTO Details_ForChildren (Details_id, for_children) VALUES(2, 'ForChildren description 2');
INSERT INTO Details_ForChildren (Details_id, for_children) VALUES(3, 'ForChildren description 3');

INSERT INTO Details_Parking (Details_id, parking, parking_key) VALUES(1, 'Parking description 1', 'Parking type 1');
INSERT INTO Details_Parking (Details_id, parking, parking_key) VALUES(2, 'Parking description 2', 'Parking type 2');
INSERT INTO Details_Parking (Details_id, parking, parking_key) VALUES(3, 'Parking description 3', 'Parking type 3');

INSERT INTO Details_Kitchens (Details_id, kitchen) VALUES(1, 'Kitchen 1');
INSERT INTO Details_Kitchens (Details_id, kitchen) VALUES(2, 'Kitchen 2');
INSERT INTO Details_Kitchens (Details_id, kitchen) VALUES(3, 'Kitchen 3');

INSERT INTO Details_Languages (Details_id, language) VALUES(1, 'Language 1');
INSERT INTO Details_Languages (Details_id, language) VALUES(2, 'Language 2');
INSERT INTO Details_Languages (Details_id, language) VALUES(3, 'Language 3');

INSERT INTO Details_PhotoPaths (Details_id, photo_path) VALUES(1, 'PhotoPath 1');
INSERT INTO Details_PhotoPaths (Details_id, photo_path) VALUES(2, 'PhotoPath 2');
INSERT INTO Details_PhotoPaths (Details_id, photo_path) VALUES(3, 'PhotoPath 3');

INSERT INTO Tags (name, description, type) VALUES ('Tag 1', 'Tag description 1', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 2', 'Tag description 2', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 3', 'Tag description 3', 0);

INSERT INTO Group_Tag (group_id, tag_id) VALUES (1, 1);
INSERT INTO Group_Tag (group_id, tag_id) VALUES (2, 2);
INSERT INTO Group_Tag (group_id, tag_id) VALUES (3, 3);

INSERT INTO Places (name, description, image) VALUES ('Place name 1', 'Place description 1', 'default.jpg');
INSERT INTO Places (name, description, image) VALUES ('Place name 2', 'Place description 2', 'default.jpg');
INSERT INTO Places (name, description, image) VALUES ('Place name 3', 'Place description 3', 'default.jpg');

INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 1', '1111', 'web.site.1.com', 'emails@1.test', '10.00 - 23.00', 1);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 2', '2222', 'web.site.2.com', 'emails@2.test', '10.00 - 23.00', 2);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 3', '3333', 'web.site.3.com', 'emails@3.test', '10.00 - 23.00', 3);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 4', '4444', 'web.site.4.com', 'emails@4.test', '10.00 - 23.00', 3);

INSERT INTO Place_Tag (place_id, tag_id) VALUES (1, 1);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (2, 2);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (3, 3);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (3, 2);


