INSERT INTO USER (ID, EMPLOYEE_ID, USERNAME, PASSWORD, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (1, 1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi',  1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO USER (ID, EMPLOYEE_ID, USERNAME, PASSWORD, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (2, 1, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC',  1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO USER (ID, EMPLOYEE_ID, USERNAME, PASSWORD, ENABLED, LASTPASSWORDRESETDATE, CREATED_DATE) VALUES (3, 1, 'disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 0, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO AUTHORITY (ID, NAME, CREATED_DATE) VALUES (1, 'ROLE_USER', PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO AUTHORITY (ID, NAME, CREATED_DATE) VALUES (2, 'ROLE_ADMIN', PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
 
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
