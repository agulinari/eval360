package com.gire.eval360.reports.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gire.eval360.reports.service.dto.statistics.StatisticsSp;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpEvaluee;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpItem;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpPoint;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpSection;
import com.gire.eval360.reports.service.remote.EvaluationServiceRemote;
import com.gire.eval360.reports.service.remote.TemplateServiceRemote;
import com.gire.eval360.reports.service.remote.dto.evaluations.Evaluation;
import com.gire.eval360.reports.service.remote.dto.evaluations.Item;
import com.gire.eval360.reports.service.remote.dto.evaluations.Section;
import com.gire.eval360.reports.service.remote.dto.templates.EvaluationTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
public class ReportServiceTest {

	@MockBean
	private EvaluationServiceRemote evaluationServiceRemote;
	@MockBean
	private TemplateServiceRemote templateServiceRemote;
	
	private ReportService reportService;
	
	@Before
	public void setUp() {
		this.reportService = new ReportServiceImpl(evaluationServiceRemote,templateServiceRemote);
	}
	
	@Test
	public void testGetInfoProjectForStatistics() {
		Long idEvalTemplate = Long.valueOf(1);
		Long idProject = Long.valueOf(1);
		
		/* ITEMS SEC1 y SEC2 - EV 1 JUAN */
		List<Item> itemSecUnoEvUnoJuan = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("12","15"),Arrays.asList("13","18"));
		List<Item> itemSecDosEvUnoJuan = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("16","18"),Arrays.asList("12","14"));
		/* ITEMS SEC1 y SEC2 - EV 2 JUAN */
		List<Item> itemSecUnoEvDosJuan = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("12","14"),Arrays.asList("11","16"));
		List<Item> itemSecDosEvDosJuan = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("11","12"),Arrays.asList("9","10"));
		
		/* ITEMS SEC1 y SEC2 - EV 1 PEDRO */
		List<Item> itemSecUnoEvUnoPedro = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("16","17"),Arrays.asList("12","13"));
		List<Item> itemSecDosEvUnoPedro = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("12","20"),Arrays.asList("15","16"));
		/* ITEMS SEC1 y SEC2 - EV 2 PEDRO */
		List<Item> itemSecUnoEvDosPedro = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("13","14"),Arrays.asList("10","13"));
		List<Item> itemSecDosEvDosPedro = generateItemsSection(Arrays.asList(Long.valueOf(1),Long.valueOf(2)),"RATING",Arrays.asList("9","11"),Arrays.asList("12","15"));
		
		Evaluation evUno = buildDummyMonoEvaluation("1",Long.valueOf(1),Long.valueOf(3),Long.valueOf(1),"JEFE", "Juan",itemSecUnoEvUnoJuan,itemSecDosEvUnoJuan);
		Evaluation evDos = buildDummyMonoEvaluation("2",Long.valueOf(1),Long.valueOf(4),Long.valueOf(1),"JEFE", "Juan",itemSecUnoEvDosJuan,itemSecDosEvDosJuan);
		Evaluation evTres = buildDummyMonoEvaluation("3",Long.valueOf(2),Long.valueOf(5),Long.valueOf(1),"JEFE", "Pedro",itemSecUnoEvUnoPedro,itemSecDosEvUnoPedro);
		Evaluation evCuatro = buildDummyMonoEvaluation("4",Long.valueOf(2),Long.valueOf(6),Long.valueOf(1),"JEFE", "Pedro",itemSecUnoEvDosPedro,itemSecDosEvDosPedro);
		List<Evaluation> evals = Arrays.asList(evUno, evDos, evTres, evCuatro);
		Flux<Evaluation> fevals = Flux.fromIterable(evals); 
		Mono<EvaluationTemplate> met = buildDummyMonoEvaluationTemplate();
		Mockito.when(this.templateServiceRemote.getTemplateById(idEvalTemplate)).thenReturn(met);
		Mockito.when(this.evaluationServiceRemote.getEvaluationsByProject(idProject)).thenReturn(fevals);
		Mono<StatisticsSp> mstatisticsSp = this.reportService.getInfoProjectForStatistics(idProject,idEvalTemplate);
		StatisticsSp stSp = mstatisticsSp.block();
		List<StatisticsSpPoint> pointsSectionJuan = getPointSectionsByNameEvaluee(stSp,"Juan");
		List<StatisticsSpPoint> pointsSectionPedro = getPointSectionsByNameEvaluee(stSp,"Pedro");
		assertEquals(2,stSp.getStatisticsSpEvaluees().size());
		assertEquals(4,stSp.getStatisticsSpSections().size());
		assertEquals(4,pointsSectionJuan.size());
		assertEquals(4,pointsSectionPedro.size());
		
		assertEquals(BigDecimal.valueOf(1200,2),pointsSectionJuan.get(0).getPoint());
		assertEquals(BigDecimal.valueOf(1200,2),pointsSectionJuan.get(0).getPointManagers());
		assertEquals(BigDecimal.valueOf(1450,2),pointsSectionJuan.get(1).getPoint());
		assertEquals(BigDecimal.valueOf(1450,2),pointsSectionJuan.get(1).getPointManagers());
		assertEquals(BigDecimal.valueOf(1350,2),pointsSectionJuan.get(2).getPoint());
		assertEquals(BigDecimal.valueOf(1350,2),pointsSectionJuan.get(2).getPointManagers());
		assertEquals(BigDecimal.valueOf(1500,2),pointsSectionJuan.get(3).getPoint());
		assertEquals(BigDecimal.valueOf(1500,2),pointsSectionJuan.get(3).getPointManagers());
		
		assertEquals(BigDecimal.valueOf(1450,2),pointsSectionPedro.get(0).getPoint());
		assertEquals(BigDecimal.valueOf(1450,2),pointsSectionPedro.get(0).getPointManagers());
		assertEquals(BigDecimal.valueOf(1550,2),pointsSectionPedro.get(1).getPoint());
		assertEquals(BigDecimal.valueOf(1550,2),pointsSectionPedro.get(1).getPointManagers());
		assertEquals(BigDecimal.valueOf(1050,2),pointsSectionPedro.get(2).getPoint());
		assertEquals(BigDecimal.valueOf(1050,2),pointsSectionPedro.get(2).getPointManagers());
		assertEquals(BigDecimal.valueOf(1550,2),pointsSectionPedro.get(3).getPoint());
		assertEquals(BigDecimal.valueOf(1550,2),pointsSectionPedro.get(3).getPointManagers());
	}

	private List<Item> generateItemsSection(List<Long> idItems, String typeItem, List<String> values, List<String> values1){
		AtomicInteger index = new AtomicInteger();
		List<Item> items = idItems.stream().map(id->{
			int position = index.getAndIncrement();
			Item item = Item.builder().id(id).type(typeItem).value(values.get(position)).value1(values1.get(position)).build();
			return item;
		}).collect(Collectors.toList());
		return items;
	}
	
	private List<StatisticsSpPoint> getPointSectionsByNameEvaluee(StatisticsSp stSp, String nameEvaluee) {
		return stSp.getStatisticsSpSections().stream().map(s->{
			List<StatisticsSpPoint> pointsJuan = s.getStatisticSpItems().stream().map(p->p.getPoints()).flatMap(List::stream)
												  .filter(p->p.getStatisticSpEvaluee().getName().equalsIgnoreCase(nameEvaluee)).collect(Collectors.toList());
			return pointsJuan;
		}).flatMap(List::stream).collect(Collectors.toList());
	}

	private Evaluation buildDummyMonoEvaluation(String idEvaluation, Long idEvaluee, Long idFeedbackProvider, Long idProject, String relationship, String username, 
												List<Item> itemsSecUno, List<Item> itemsSecDos) {
		Section suno = Section.builder().id(Long.valueOf(1)).items(itemsSecUno).build();
		Section sdos = Section.builder().id(Long.valueOf(2)).items(itemsSecDos).build();
		List<Section> sections = Arrays.asList(suno,sdos);
		Evaluation eval = Evaluation.builder().id(idEvaluation).date(LocalDateTime.now()).idEvaluee(idEvaluee).idFeedbackProvider(idFeedbackProvider).
											   idProject(idProject).relationship(relationship).sections(sections).username(username).build();
		
		return eval;
	}

	private Mono<EvaluationTemplate> buildDummyMonoEvaluationTemplate() {
		ItemTemplate iuno = ItemTemplate.builder().id(Long.valueOf(1)).itemType(ItemType.RATING).position(1).build();
		ItemTemplate idos = ItemTemplate.builder().id(Long.valueOf(2)).itemType(ItemType.RATING).position(2).build();
		List<ItemTemplate> itSecUno = Arrays.asList(iuno, idos);
		List<ItemTemplate> itSecDos = Arrays.asList(iuno, idos);
		com.gire.eval360.reports.service.remote.dto.templates.Section suno = com.gire.eval360.reports.service.remote.dto.templates.Section.builder().id(Long.valueOf(1)).items(itSecUno).build();
		com.gire.eval360.reports.service.remote.dto.templates.Section sdos = com.gire.eval360.reports.service.remote.dto.templates.Section.builder().id(Long.valueOf(2)).items(itSecDos).build();
		List<com.gire.eval360.reports.service.remote.dto.templates.Section> sections = Arrays.asList(suno,sdos);
		EvaluationTemplate evTemp = EvaluationTemplate.builder().id(Long.valueOf(1)).idUser("1").sections(sections).build();
		return Mono.just(evTemp);
	}

	

}
