INSERT INTO USER (ID, USERNAME, PASSWORD, MAIL, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin@gmail.com',  1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO USER (ID, USERNAME, PASSWORD, MAIL, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user@gmail.com',  1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO USER (ID, USERNAME, PASSWORD, MAIL, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (3, 'disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'disabled@gmail.com', 0, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO AUTHORITY (ID, NAME, CREATED_DATE) VALUES (1, 'ROLE_USER', PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO AUTHORITY (ID, NAME, CREATED_DATE) VALUES (2, 'ROLE_ADMIN', PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
 
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
