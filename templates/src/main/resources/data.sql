INSERT INTO EVALUATION_TEMPLATE (ID, TITLE, ID_USER, CREATED_DATE) VALUES (1, 'template1', 1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));

INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (1, 1, 'Planning', 'This section is about planning', 'QUESTIONS', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (1, 1, 'Setting goals', 'Sets clear and realistic goals, working with others to ensure understanding and agreement', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (2, 1, 'Breaking down tasks', 'Breaks down tasks into manageable units', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (3, 1, 'Prioritising', 'Identifies and focuses effort on the top priorities', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (4, 1, 'Managing time', 'Effective at managing their time, taking on an appropiate workload and providing sensible estimates', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (5, 1, 'Comment', 'Please provide an explanation of the scores provided for Planning', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (2, 1, 'Delivering', 'This section is about delivering', 'QUESTIONS', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (6, 2, 'Being productive', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (7, 2, 'Communicating progress', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (8, 2, 'Identifies and solves problems', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (9, 2, 'Technical competence', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (10, 2, 'Comment', 'Please provide an explanation of the scores provided for Delivering', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (3, 1, 'Analysis and decision making', 'This section is about decision making', 'QUESTIONS', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (11, 3, 'Analythical thinking', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (12, 3, 'Decision making', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (13, 3, 'Adapting to change', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (14, 3, 'Innovating', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (15, 3, 'Comment', 'Please provide an explanation of the scores provided for Analysis and decision making', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (4, 1, 'Communication', 'This section is about communication', 'QUESTIONS', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (16, 4, 'Listening', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (17, 4, 'Communicating clearly', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (18, 4, 'Positive and professional', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (19, 4, 'Comment', 'Please provide an explanation of the scores provided for Communication', 'TEXTBOX', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (5, 1, 'Self development', 'This section is about self development', 'QUESTIONS', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (20, 5, 'Identifying development needs', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (21, 5, 'Demonstating self-development', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (22, 5, 'Seeking feedback', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (23, 5, 'Comment', 'Please provide an explanation of the scores provided for Self development', 'TEXTBOX', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (6, 1, 'Working with others', 'This section is about working with others', 'QUESTIONS', 6,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (24, 6, 'Sharing expertise', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (25, 6, 'Giving feedback', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (26, 6, 'Handling disagreement', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (27, 6, 'Building networks', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (28, 6, 'Comment', 'Please provide an explanation of the scores provided for Leadership', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (7, 1, 'Managing others', 'This section is about managing others', 'QUESTIONS', 7,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (29, 7, 'Managing people', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (30, 7, 'Delegating', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (31, 7, 'Improving performance', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (32, 7, 'Managing risk', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (33, 7, 'Comment', 'Please provide an explanation of the scores provided for Leadership', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (8, 1, 'Leadership', 'This section is about leadership', 'QUESTIONS', 8,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (34, 8, 'Developing strategy', 'Es bueno motivando al equipo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (35, 8, 'Motivating and inspiring', 'Es bueno motivando al equipo', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (36, 8, 'Influencing', 'Es bueno motivando al equipo', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (37, 8, 'Taking risks', 'Es bueno motivando al equipo', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (38, 8, 'Comment', 'Please provide an explanation of the scores provided for Leadership', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
