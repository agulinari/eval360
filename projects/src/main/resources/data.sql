INSERT INTO USER (ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, EMAIL, FIRST_NAME, LAST_NAME) VALUES (1, 'Agustin', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'sivori.daniel@gmail.com','Pablo', 'Sívori');
INSERT INTO PROJECT(ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, STATUS, USER_ID) VALUES (1, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 0,1);