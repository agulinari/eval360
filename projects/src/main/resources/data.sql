INSERT INTO PROJECT(ID, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, ID_EVALUATION_TEMPLATE, ID_REPORT_TEMPLATE, STATUS, START_DATE) VALUES (1, 'Mi Primer Proyecto', 'Este es el primer proyecto de Evaluacion 360', 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 1,1,'PENDIENTE', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO PROJECT_ADMIN (ID, ID_USER,PROJECT_ID, CREATOR, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (1, 1,1, true, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (1, 1,1, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (1, 1,1, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (1, 1,1, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2, 2,1, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (1,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),1,1,'AUTO', 'PENDIENTE');
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (1,1,1);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (2,1,2);

INSERT INTO PROJECT(ID, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, ID_EVALUATION_TEMPLATE, ID_REPORT_TEMPLATE, STATUS, START_DATE, END_DATE) VALUES (2, 'Evaluacion 2015', 'Evaluacion de desempeño del año 2015', 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 1,1,'RESUELTO', PARSEDATETIME('2015-10-10', 'yyyy-MM-dd'), PARSEDATETIME('2015-11-09', 'yyyy-MM-dd'));
INSERT INTO PROJECT_ADMIN (ID, ID_USER,PROJECT_ID, CREATOR, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2, 1,2, true, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (3, 1,2, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2, 1,2, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (3, 2,2, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2, 4,2, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (3, 3,2, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (2,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),2,2,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (3,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),3,2,'PAR', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (4,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),3,3,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (3,2,3);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (4,3,3);

INSERT INTO PROJECT(ID, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, ID_EVALUATION_TEMPLATE, ID_REPORT_TEMPLATE, STATUS, START_DATE, END_DATE) VALUES (3, 'Evaluacion 2016', 'Evaluacion de desempeño del año 2016', 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 1,1,'RESUELTO', PARSEDATETIME('2016-01-02', 'yyyy-MM-dd'), PARSEDATETIME('2016-02-15', 'yyyy-MM-dd'));
INSERT INTO PROJECT_ADMIN (ID, ID_USER,PROJECT_ID, CREATOR, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (3, 1,3, true, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (4, 1,3, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (4, 1,3, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (5, 2,3, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (6, 3,3, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (4, 1,3, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (5,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),4,4,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (6,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),5,4,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (7,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),6,4,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (5,4,4);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (6,5,4);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (7,6,4);

INSERT INTO PROJECT(ID, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, ID_EVALUATION_TEMPLATE, ID_REPORT_TEMPLATE, STATUS, START_DATE, END_DATE) VALUES (4, 'Evaluacion 2017', 'Evaluacion de desempeño del año 2017', 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 1,1,'RESUELTO', PARSEDATETIME('2017-07-25', 'yyyy-MM-dd'), PARSEDATETIME('2017-10-23', 'yyyy-MM-dd'));
INSERT INTO PROJECT_ADMIN (ID, ID_USER,PROJECT_ID, CREATOR, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (4, 1,4, true, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (5, 1,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (7, 3,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (8, 4,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (9, 5,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (5, 1,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (6, 2,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (7, 3,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID, ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (8, 4,4, 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'), 'Pablo', PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (8,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),7,5,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (9,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),7,6,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (10,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),8,5,'PAR', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (11,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),8,7,'PAR', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (12,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),8,8,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (ID, CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (13,PARSEDATETIME('2018-11-05', 'yyyy-MM-dd'),9,6,'JEFE', 'RESUELTO');
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (8,7,5);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (9,8,5);
INSERT INTO EVALUEE_REVIEWER (ID, EVALUEE_ID, REVIEWER_ID) VALUES (10,9,5);


