INSERT INTO PROJECT(NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, ID_EVALUATION_TEMPLATE, ID_REPORT_TEMPLATE, STATUS) VALUES ('Mi Primer Proyecto', 'Este es el primer proyecto de Evaluacion 360', 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 1,1,'PENDIENTE');
INSERT INTO PROJECT_ADMIN (ID_USER,PROJECT_ID, CREATOR, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2,1, true, 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2,1, 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE (ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2,1, 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO FEEDBACK_PROVIDER (ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (2,1, 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO REVIEWER (ID_USER,PROJECT_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (3,1, 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'), 'Pablo', to_date('2018-11-05', 'yyyy-MM-dd'));
INSERT INTO EVALUEE_FEEDBACK_PROVIDER (CREATED_ON, EVALUEE_ID, FEEDBACK_PROVIDER_ID, RELATIONSHIP, STATUS) VALUES (to_date('2018-11-05', 'yyyy-MM-dd'),1,1,'Jefe', 'PENDIENTE');
INSERT INTO EVALUEE_REVIEWER (EVALUEE_ID, REVIEWER_ID) VALUES (1,1);
INSERT INTO EVALUEE_REVIEWER (EVALUEE_ID, REVIEWER_ID) VALUES (1,2);