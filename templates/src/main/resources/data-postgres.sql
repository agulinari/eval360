INSERT INTO EVALUATION_TEMPLATE (ID, TITLE, ID_USER, CREATED_DATE) VALUES (1, 'template1', 1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));

INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (1, 1, 'Planificación', 'Esta seccion es acerca de planificación', 'QUESTIONS', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (1, 1, 'Estableciendo metas', 'Establece objetivos claros y realistas, trabajando con otros para asegurar la comprensión y el acuerdo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (2, 1, 'Desglosando tareas', 'Desglosa las tareas en unidades manejables', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (3, 1, 'Priorizando', 'Identifica y enfoca el esfuerzo en las principales prioridades', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (4, 1, 'Administrando el tiempo', 'Efectivo para administrar su tiempo, asumir una carga de trabajo adecuada y proporcionar estimaciones razonables', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (5, 1, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para la Planificación', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (2, 1, 'Cumplimiento', 'Esta seccion es acerca de cumplimiento', 'QUESTIONS', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (6, 2, 'Siendo productivo', 'Es productivo tanto cuando se trabaja solo como en grupo', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (7, 2, 'Comunicando progreso', 'Comunica tempranamente el progreso y resalta cualquier problema o cambio en el plan', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (8, 2, 'Identifica y resuelve problemas', 'Es eficaz para identificar problemas, descomponerlos y proponer soluciones', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (9, 2, 'Competencia técnica', 'Es capaz de producir un trabajo de calidad y apoyar a otros como se espera de su rol', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (10, 2, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Cumplimiento', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (3, 1, 'Análisis y toma de decisiones', 'Esta seccion es acerca de análisis y toma de decisiones', 'QUESTIONS', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (11, 3, 'Pensamiento analítico', 'Ofrece un análisis claro que conduce a decisiones lógicas, informadas y objetivas', 'RATING', 1,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (12, 3, 'Toma de decisiones', 'Toma buenas decisiones utilizando la información disponible y una cantidad apropiada de investigación adicional', 'RATING', 2,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (13, 3, 'Adaptación al cambio', 'Aprovecha oportunidades para cambiar que sean beneficiosas para el equipo / organización', 'RATING', 3,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (14, 3, 'Innovando', 'Identifica y considera soluciones innovadoras cuando corresponde', 'RATING', 4,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (15, 3, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Análisis y toma de decisiones', 'TEXTBOX', 5,  PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (4, 1, 'Comunicación', 'Esta seccion es acerca de comunicación', 'QUESTIONS', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (16, 4, 'Escuchando', 'Es bueno para escuchar y asegurarse de entender a los demás', 'RATING', 1,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (17, 4, 'Comunicando con claridad', 'Comunica puntos de vista, ideas y preguntas de manera clara y concisa (tanto verbalmente como en comunicaciones escritas)', 'RATING', 2,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (18, 4, 'Positivo y profesional', 'Da una impresión positiva y profesional', 'RATING', 3,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (19, 4, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Comunicación', 'TEXTBOX', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (5, 1, 'Autodesarrollo', 'Esta sección es acerca de autodesarrollo', 'QUESTIONS', 5,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (20, 5, 'Identificando necesidades de desarrollo', 'Identifica áreas para el autodesarrollo', 'RATING', 1,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (21, 5, 'Demostrando autodesarrollo', 'Demuestra autodesarrollo, con mejora visible', 'RATING', 2,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (22, 5, 'Buscando retroalimentación', 'Busca activamente retroalimentación sobre su propio desempeño de otros', 'RATING', 3,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (23, 5, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Autodesarrollo', 'TEXTBOX', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO SECTION (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (6, 1, 'Trabajando con los demás', 'Esta sección trata acerca de trabajar con los demás', 'QUESTIONS', 6,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (24, 6, 'Compartiendo experiencia', 'Comparte su experiencia para ayudar a la organización en general', 'RATING', 1,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (25, 6, 'Dando retroalimentación', 'Da retroalimentación que ayuda a otros a desarrollarse', 'RATING', 2,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (26, 6, 'Manejando el desacuerdo', 'Es capaz de abordar profesionalmente los desacuerdos entre individuos y equipos', 'RATING', 3,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (27, 6, 'Construyendo vínculos', 'Construye buenas relaciones en toda la organización', 'RATING', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (28, 6, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Trabajando con los demás', 'TEXTBOX', 5,  to_date('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO "section" (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (7, 1, 'Dirigiendo a otros', 'Esta sección es acerca de dirigiendo a otros', 'QUESTIONS', 7,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (29, 7, 'Dirigiendo gente', 'Gestiona eficazmente a las personas, asegurando que los miembros del equipo estén contentos y tengan un buen desempeño', 'RATING', 1,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (30, 7, 'Delegando', 'Delega responsabilidades y tareas de manera efectiva', 'RATING', 2,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (31, 7, 'Mejorando el desempeño', 'Identifica y ofrece oportunidades para ayudar a individuos y equipos a mejorar su rendimiento', 'RATING', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (32, 7, 'Gestionando los riesgos', 'Identifica los riesgos y toma medidas para reducir la probabilidad (y / o el impacto) del riesgo que se produce', 'RATING', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (33, 7, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Dirigiendo a otros', 'TEXTBOX', 5,  to_date('01-01-2016', 'dd-MM-yyyy'));


INSERT INTO "section" (ID, TEMPLATE_ID, NAME, DESCRIPTION, SECTION_TYPE, POSITION, CREATED_DATE) VALUES (8, 1, 'Liderazgo', 'Esta sección es acerca de liderazgo', 'QUESTIONS', 8,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (34, 8, 'Desarrollando una estrategia', 'Desarrolla una estrategia efectiva a corto y largo plazo', 'RATING', 1,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (35, 8, 'Motivando a inspirando', 'Motiva e inspira a los demás', 'RATING', 2,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (36, 8, 'Influenciando', 'Hace un impacto positivo al influenciar a otros', 'RATING', 3,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (37, 8, 'Tomando riesgos', 'Dispuesto a asumir riesgos, habiendo considerado la probabilidad, el impacto y las oportunidades de mitigación', 'RATING', 4,  to_date('01-01-2016', 'dd-MM-yyyy'));
INSERT INTO ITEM_TEMPLATE(ID, SECTION_ID, TITLE, DESCRIPTION, ITEM_TYPE, POSITION, CREATED_DATE) VALUES (38, 8, 'Comentarios', 'Por favor, proporcione una explicación de las puntuaciones proporcionadas para Liderazgo', 'TEXTBOX', 5,  to_date('01-01-2016', 'dd-MM-yyyy'));
