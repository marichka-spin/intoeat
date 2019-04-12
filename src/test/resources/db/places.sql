INSERT INTO Places (name, description, image) VALUES ('Place name 1', 'Place description 1', 'default.jpg');
INSERT INTO Places (name, description, image) VALUES ('Place name 2', 'Place description 2', 'default.jpg');
INSERT INTO Places (name, description, image) VALUES ('Place name 3', 'Place description 3', 'default.jpg');

INSERT INTO Tags (name, description, type) VALUES ('Tag 1', 'Tag description 1', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 2', 'Tag description 2', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 3', 'Tag description 3', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 4', 'Tag description 4', 0);

INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 1', '1111', 'web.site.1.com', 'emails@1.test', '10.00 - 23.00', 1);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 2', '2222', 'web.site.2.com', 'emails@2.test', '10.00 - 23.00', 2);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 3', '3333', 'web.site.3.com', 'emails@3.test', '10.00 - 23.00', 3);
INSERT INTO Contacts (address, phone, webSite, email, workTime, place_id) VALUES ('Adress 4', '4444', 'web.site.4.com', 'emails@4.test', '10.00 - 23.00', 3);

INSERT INTO Place_Tag (place_id, tag_id) VALUES (1, 1);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (2, 2);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (3, 3);
INSERT INTO Place_Tag (place_id, tag_id) VALUES (3, 2);