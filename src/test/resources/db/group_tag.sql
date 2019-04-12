INSERT INTO Groups (name, description) VALUES ('Group 1', 'Group description 1');
INSERT INTO Groups (name, description) VALUES ('Group 2', 'Group description 2');
INSERT INTO Groups (name, description) VALUES ('Group 3', 'Group description 3');

INSERT INTO Tags (name, description, type) VALUES ('Tag 1', 'Tag description 1', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 2', 'Tag description 2', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 3', 'Tag description 3', 0);
INSERT INTO Tags (name, description, type) VALUES ('Tag 4', 'Tag description 4', 0);

INSERT INTO Group_Tag (group_id, tag_id) VALUES (1, 1);
INSERT INTO Group_Tag (group_id, tag_id) VALUES (2, 2);
INSERT INTO Group_Tag (group_id, tag_id) VALUES (3, 3);