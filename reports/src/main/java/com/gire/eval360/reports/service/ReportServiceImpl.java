package com.gire.eval360.reports.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.gire.eval360.reports.domain.AreaToImprove;
import com.gire.eval360.reports.domain.Comment;
import com.gire.eval360.reports.domain.Item;
import com.gire.eval360.reports.domain.ItemScore;
import com.gire.eval360.reports.domain.ReportData;
import com.gire.eval360.reports.domain.Score;
import com.gire.eval360.reports.domain.Section;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSp;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpEvaluee;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpEvaluee.StatisticsSpEvalueeBuilder;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpItem;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpPoint;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSpSection;
import com.gire.eval360.reports.service.remote.EvaluationServiceRemote;
import com.gire.eval360.reports.service.remote.TemplateServiceRemote;
import com.gire.eval360.reports.service.remote.dto.evaluations.Evaluation;
import com.gire.eval360.reports.service.remote.dto.templates.EvaluationTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemType;
import com.gire.eval360.reports.service.remote.dto.templates.SectionType;
import com.google.common.collect.Lists;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReportServiceImpl implements ReportService {

	private final EvaluationServiceRemote evaluationServiceRemote;
	private final TemplateServiceRemote templateServiceRemote;
	private Map<Long,EvaluationTemplate> mapTemplates = new HashMap<Long,EvaluationTemplate>();
	
	@Autowired
	public ReportServiceImpl(EvaluationServiceRemote evaluationServiceRemote,
							 TemplateServiceRemote templateServiceRemote) {
		
		this.evaluationServiceRemote = evaluationServiceRemote;
		this.templateServiceRemote = templateServiceRemote;
	}
	
	@Override
	public Mono<StatisticsSp> getInfoProjectForStatistics(Long idProject, Long idEvaluationTemplate) {
		
		EvaluationTemplate evalTemp = mapTemplates.get(idEvaluationTemplate);
		Mono<StatisticsSp> statisticsSp = Mono.empty();
		try {
		if(evalTemp==null) {
			statisticsSp = templateServiceRemote.getTemplateById(idEvaluationTemplate).flatMap(template -> {
				mapTemplates.put(template.getId(), template);
				Mono<List<Evaluation>> evaluations = evaluationServiceRemote.getEvaluationsByProject(idProject).collectList();
				return evaluations.map(es -> createStatisticsSp(template, es));
			});
		}else {
			Mono<List<Evaluation>> evaluations = evaluationServiceRemote.getEvaluationsByProject(idProject).collectList();
			statisticsSp = evaluations.map(es -> createStatisticsSp(evalTemp, es));
		}
		}catch(Exception e) {
			System.out.println(e);
		}
		return statisticsSp;
	}
	
	private StatisticsSp createStatisticsSp(EvaluationTemplate template, List<Evaluation> evaluations){
					
		List<StatisticsSpSection> stSpSects=new ArrayList<StatisticsSpSection>();
		List<StatisticsSpEvaluee> stSpEvals=new ArrayList<StatisticsSpEvaluee>();
		
		Map<Long, List<Evaluation>> mapEvaluations = evaluations.stream().collect(Collectors.groupingBy(Evaluation::getIdEvaluee));
		
		mapEvaluations.values().forEach(evals->{
			Evaluation eval = evals.get(0);
			StatisticsSpEvaluee spEvaluee = StatisticsSpEvaluee.builder().id(eval.getIdEvaluee().toString()).name(eval.getUsername()).build();
			List<StatisticsSpSection> statisticsSpSection = getStatisticsSpSection(spEvaluee,template,evals);
			stSpSects.addAll(statisticsSpSection);
			stSpEvals.add(spEvaluee);
		});
		
		StatisticsSp stSp = StatisticsSp.builder().statisticsSpSections(stSpSects).statisticsSpEvaluees(stSpEvals).build();
		return stSp;
	}
	
	private List<StatisticsSpSection> getStatisticsSpSection(StatisticsSpEvaluee spEvaluee, EvaluationTemplate template,
															 List<Evaluation> evaluations) {
		
			Set<com.gire.eval360.reports.service.remote.dto.evaluations.Section> sections = evaluations.stream().map(ev->ev.getSections()).flatMap(List::stream).collect(Collectors.toSet());
			
			List<StatisticsSpSection> stSps = sections.stream().map(s->{
				Optional<com.gire.eval360.reports.service.remote.dto.templates.Section> osectTemp = template.getSections().stream().filter(st->st.getId().equals(s.getId())).findFirst();
				com.gire.eval360.reports.service.remote.dto.templates.Section sectTemp = osectTemp.isPresent()?osectTemp.get():com.gire.eval360.reports.service.remote.dto.templates.Section.builder().build();
				
				List<Evaluation> evaluationsForAverage = evaluations.stream().map(e->{
					List<com.gire.eval360.reports.service.remote.dto.evaluations.Section> sectionsForAverage = e.getSections().stream().filter(sect->sect.getId().equals(s.getId())).collect(Collectors.toList());
					Evaluation evalForAverage = e.toBuilder().clearSections().sections(sectionsForAverage).build();
					return evalForAverage;
				}).collect(Collectors.toList());
				
				List<StatisticsSpItem> stSpIts = sectTemp.getItems().stream().filter(it->it.getItemType().equals(ItemType.RATING))
						.map(it->{
							ItemScore average = calculateAverage(it.getId(), evaluationsForAverage, "", true);
							ItemScore averageManagers = calculateAverage(it.getId(), evaluationsForAverage, "JEFE", true);
							ItemScore averagePeers = calculateAverage(it.getId(), evaluationsForAverage, "PAR", true);
							ItemScore averageDirectReports = calculateAverage(it.getId(), evaluationsForAverage, "SUBORDINADO", true);
							Optional<ItemTemplate> oitT = sectTemp.getItems().stream().filter(itTemp-> itTemp.getId().equals(it.getId())).findFirst();
							ItemTemplate itT = (oitT.isPresent())?oitT.get():ItemTemplate.builder().build();

							StatisticsSpPoint stSpPoint = StatisticsSpPoint.builder().point(average.getCurrentPerformance()).pointManagers(averageManagers.getCurrentPerformance()).
									pointPeers(averagePeers.getCurrentPerformance()).pointDirectReports(averageDirectReports.getCurrentPerformance()).
									statisticSpEvaluee(spEvaluee).build();
							StatisticsSpItem stSpIt = StatisticsSpItem.builder().
									description(itT.getDescription()).id(itT.getId()).
									title(itT.getTitle()).point(stSpPoint).build();
							return stSpIt;
						}).collect(Collectors.toList());
				StatisticsSpSection stSpSect=StatisticsSpSection.builder().name(sectTemp.getName()).statisticSpItems(stSpIts).build();
				return stSpSect;
			}).collect(Collectors.toList());
			return stSps;
	}

	@Override
	public Mono<ReportData> generateReport(Long idProject, Long idEvaluee, Long idTemplate) {

		Mono<ReportData> report = templateServiceRemote.getTemplateById(idTemplate).flatMap(template -> {
			
			Mono<List<Evaluation>> evaluations = evaluationServiceRemote.getEvaluationsByProjectAndEvaluee(idProject, idEvaluee).collectList();
			
			return evaluations.map(es -> createReport(idProject, idEvaluee, template, es));
		});
		
		return report;
	}

	private ReportData createReport(Long idProject, Long idEvaluee, EvaluationTemplate template, List<Evaluation> evaluations) {
		
		String username = (!evaluations.isEmpty()) ? evaluations.get(0).getUsername() : "User";
		LocalDate date = LocalDate.now();
		Integer autoeval = countRelationship(evaluations, "AUTO");
		Integer managers = countRelationship(evaluations, "JEFE");
		Integer peers = countRelationship(evaluations, "PAR");
		Integer directReports = countRelationship(evaluations, "SUBORDINADO");
		List<Item> items = calculateEvaluatedItems(template, evaluations);
		List<String> strengths = getStrengths(items);
		List<String> weaknesses = getWeaknesses(items);
		List<AreaToImprove> areasToImprove = getAreasToImprove(items, managers, peers, directReports, autoeval);
		List<Comment> comments = getComments(template, evaluations);
		List<Section> detailedResults = getDetailedResults(template, evaluations, managers, peers, directReports, autoeval);
		ReportData report = ReportData.builder()
									  .idProject(idProject)
									  .idEvaluee(idEvaluee)
									  .date(date)
									  .username(username)
									  .managers(managers)
									  .peers(peers)
									  .directReports(directReports)
									  .strengths(strengths)
									  .weaknesses(weaknesses)
									  .areasToImprove(areasToImprove)
									  .comments(comments)
									  .detailedResults(detailedResults)
									  .build();
									  
		return report;
	}


	private Integer countRelationship(List<Evaluation> evaluations, String relationship) {
		Integer count = 0;
		for (Evaluation evaluation : evaluations) {
			String currentRelationship = evaluation.getRelationship();
			if (currentRelationship.equals(relationship)) {
				count++;
			}
		}
		return count;
	}
	
	private List<Item> calculateEvaluatedItems(EvaluationTemplate template, List<Evaluation> evaluations){
		
		List<Item> items = new ArrayList<>();
		
		for (com.gire.eval360.reports.service.remote.dto.templates.Section section : template.getSections()) {
			if (section.getSectionType().equals(SectionType.QUESTIONS)){
				for (ItemTemplate itemTemplate : section.getItems()) {
					Long itemId = itemTemplate.getId();
					if (itemTemplate.getItemType().equals(ItemType.RATING)) {
						ItemScore average = calculateAverage(itemId, evaluations, "", true);
						ItemScore averageManagers = calculateAverage(itemId, evaluations, "JEFE", true);
						ItemScore averagePeers = calculateAverage(itemId, evaluations, "PAR", true);
						ItemScore averageDirectReports = calculateAverage(itemId, evaluations, "SUBORDINADO", true);
						ItemScore averageMe = calculateAverage(itemId, evaluations, "AUTO", true);
						
						Item item = Item.builder()
										.name(itemTemplate.getTitle())
										.description(itemTemplate.getDescription())
										.currentPerformanceByColleagues(average.getCurrentPerformance())
										.currentPerformanceByDirectReports(averageDirectReports.getCurrentPerformance())
										.currentPerformanceByManagers(averageManagers.getCurrentPerformance())
										.currentPerformanceByPeers(averagePeers.getCurrentPerformance())
										.currentPerformanceByMe(averageMe.getCurrentPerformance())
										.desiredPerformanceByColleagues(average.getDesiredPerformance())
										.desiredPerformanceByDirectReports(averageDirectReports.getDesiredPerformance())
										.desiredPerformanceByManagers(averageManagers.getDesiredPerformance())
										.desiredPerformanceByPeers(averagePeers.getDesiredPerformance())
										.desiredPerformanceByMe(averageMe.getDesiredPerformance())
										.build();
						
						items.add(item);
			
					}
				}
			}
		}
		
		items.sort((Item i1, Item i2) -> i1.getDesiredPerformanceByColleagues().compareTo(i2.getDesiredPerformanceByColleagues()));
		List<Item> reverseList = Lists.reverse(items);
		return reverseList;
		
	}

	private ItemScore calculateAverage(Long itemId, List<Evaluation> evaluations, String relationship, boolean diff) {
		
		Integer totalCurrentScore = 0;
		Integer totalDesiredScore = 0;
		Integer evalCount = 0;
		for (Evaluation evaluation : evaluations) {
			
			if (relationship.isEmpty() || evaluation.getRelationship().equals(relationship)) {
				
				for (com.gire.eval360.reports.service.remote.dto.evaluations.Section section : evaluation.getSections()) {
					for (com.gire.eval360.reports.service.remote.dto.evaluations.Item item :  section.getItems()) {
						if ((item.getId().longValue() == itemId.longValue()) && (item.getType().equals(ItemType.RATING.toString()))){
							Integer score = Integer.valueOf(item.getValue());
							Integer score1 = Integer.valueOf(item.getValue1());
							totalCurrentScore = totalCurrentScore + score;
							if (diff) {
								totalDesiredScore = totalDesiredScore + (score1 - score);
							} else {
								totalDesiredScore = totalDesiredScore + score1;
							}
							evalCount++;
						}
					}
				}
				
			}
		}
		BigDecimal currentScore = BigDecimal.ZERO;
		if (evalCount.intValue() != 0) {
			double cs = totalCurrentScore.doubleValue() / evalCount.doubleValue();
			currentScore = BigDecimal.valueOf(cs).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		BigDecimal desiredScore = BigDecimal.ZERO;
		if (evalCount.intValue() !=  0) {
			double ds = totalDesiredScore.doubleValue() / evalCount.doubleValue();
			desiredScore = BigDecimal.valueOf(ds).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		ItemScore itemScore = ItemScore.builder().currentPerformance(currentScore).desiredPerformance(desiredScore).build();
		return itemScore;
	}

	private List<String> getWeaknesses(List<Item> calculatedItems) {
		
		calculatedItems.sort((Item i1, Item i2) -> i1.getDesiredPerformanceByColleagues().compareTo(i2.getDesiredPerformanceByColleagues()));
		List<Item> reverseList = Lists.reverse(calculatedItems);
		List<String> weaknesses = reverseList.stream().map(i -> i.getName()).limit(5).collect(Collectors.toList());
		return weaknesses;
	}

	private List<String> getStrengths(List<Item> calculatedItems) {
		
		calculatedItems.sort((Item i1, Item i2) -> i1.getCurrentPerformanceByColleagues().compareTo(i2.getCurrentPerformanceByColleagues()));

		List<Item> reverseList = Lists.reverse(calculatedItems);
		List<String> strengths = reverseList.stream().map(i -> i.getName()).limit(5).collect(Collectors.toList()); 
		return strengths;
	}
	
	private List<AreaToImprove> getAreasToImprove(List<Item> calculatedItems, Integer managers, Integer peers, Integer directReports, Integer autoeval) {
		calculatedItems.sort((Item i1, Item i2) -> i1.getDesiredPerformanceByColleagues().compareTo(i2.getDesiredPerformanceByColleagues()));
		List<Item> reverseList = Lists.reverse(calculatedItems);

		List<AreaToImprove> areas = reverseList.stream()
				.map(i -> buildAreaToImprove(i, managers, peers, directReports, autoeval)).collect(Collectors.toList());
		return areas;
	}

	private AreaToImprove buildAreaToImprove(Item i, Integer managers, Integer peers, Integer directReports, Integer autoeval) {
		BigDecimal dp = i.getDesiredPerformanceByColleagues();
		BigDecimal percentDp = dp.add(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(12.5));
		
		BigDecimal dpDR = i.getDesiredPerformanceByDirectReports();
		BigDecimal percentDpDR = dpDR.add(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(12.5));
		
		BigDecimal dpManagers = i.getDesiredPerformanceByManagers();
		BigDecimal percentDpManagers = dpManagers.add(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(12.5));
		
		BigDecimal dpMe = i.getDesiredPerformanceByMe();
		BigDecimal percentDpMe = dpMe.add(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(12.5));
		
		BigDecimal dpPeers = i.getDesiredPerformanceByPeers();
		BigDecimal percentDpPeers = dpPeers.add(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(12.5));
		
		Score scoreDR = getScore(percentDpDR, directReports);
		Score scoreManagers = getScore(percentDpManagers, managers);
		Score scoreMe = getScore(percentDpMe, autoeval);
		Score scorePeers = getScore(percentDpPeers, peers);
		
		AreaToImprove area = AreaToImprove.builder()
		.areaAssessed(i.getName())
		.desiredImprovement(percentDp)
		.ownView(scoreMe)
		.managersView(scoreManagers)
		.directReportsView(scoreDR)
		.peersView(scorePeers)
		.build();
		
		return area;
	}

	private Score getScore(BigDecimal dp, Integer count) {
		
		if (count.intValue() == 0) {
			return Score.NONE;
		}

		if (dp.compareTo(BigDecimal.valueOf(33))<0) {
			return Score.SMALL;
		}else if (dp.compareTo(BigDecimal.valueOf(66))>0) {
			return Score.LARGE;
		}else {
			return Score.MEDIUM;
		}
	}

	private List<Section> getDetailedResults(EvaluationTemplate template, List<Evaluation> evaluations, Integer managers, Integer peers, Integer directReports, Integer autoEval) {
		
		List<Section> sections = new ArrayList<>();
		for (com.gire.eval360.reports.service.remote.dto.templates.Section section : template.getSections()) {
			if (section.getSectionType().equals(SectionType.QUESTIONS)){
				Section sec = Section.builder()
										 .name(section.getName())
										 .description(section.getDescription())
										 .currentPerformanceByColleagues(BigDecimal.ZERO)
										 .currentPerformanceByMe(BigDecimal.ZERO)
										 .desiredPerformanceByColleagues(BigDecimal.ZERO)
										 .desiredPerformanceByMe(BigDecimal.ZERO)
										 .items(new ArrayList<>())
										 .comments(new ArrayList<>())
										 .build();
				
				BigDecimal sectionCurrentPerformanceByMe = BigDecimal.ZERO;
				BigDecimal sectionCurrentPerformanceByColleagues = BigDecimal.ZERO;
				BigDecimal sectionDesiredPerformanceByMe = BigDecimal.ZERO;
				BigDecimal sectionDesiredPerformanceByColleagues = BigDecimal.ZERO;
				
				for (ItemTemplate itemTemplate : section.getItems()) {
					Long itemId = itemTemplate.getId();

					if (itemTemplate.getItemType().equals(ItemType.TEXTBOX)) {
						
						Comment comment = Comment.builder().question(itemTemplate.getDescription()).ownResponse("N/A").build();
						List<String> otherResponses = new ArrayList<>();
						for (Evaluation evaluation : evaluations){
							
							String response = getComment(section.getId(), itemId, evaluation);

							if (evaluation.getRelationship().equals("AUTO")) {
								comment.setOwnResponse(response);
							} else {
								otherResponses.add(response);
							}
							
						}
						comment.setOtherResponses(otherResponses);
						sec.getComments().add(comment);
					} else if (itemTemplate.getItemType().equals(ItemType.RATING)){
						
						ItemScore averageMe = null;
						if (autoEval.intValue() == 0) {
							averageMe = ItemScore.builder().currentPerformance(null).desiredPerformance(null).build();
						} else {
							averageMe = calculateAverage(itemId, evaluations, "AUTO", false);
							sectionCurrentPerformanceByMe = sectionCurrentPerformanceByMe.add(averageMe.getCurrentPerformance());
							sectionDesiredPerformanceByMe = sectionDesiredPerformanceByMe.add(averageMe.getDesiredPerformance());
						}
						ItemScore averageManagers = null;
						if (managers.intValue() == 0) {
							averageManagers = ItemScore.builder().currentPerformance(null).desiredPerformance(null).build();						
						} else {
							averageManagers = calculateAverage(itemId, evaluations, "JEFE", false);
						}
						ItemScore averagePeers = null;
						if (peers.intValue() == 0) {
							averagePeers = ItemScore.builder().currentPerformance(null).desiredPerformance(null).build();						
						} else {
							averagePeers = calculateAverage(itemId, evaluations, "PAR", false);
						}
						ItemScore averageDirectReports = null;
						if (directReports.intValue() == 0) {
							averageDirectReports = ItemScore.builder().currentPerformance(null).desiredPerformance(null).build();						
						} else {
							averageDirectReports = calculateAverage(itemId, evaluations, "SUBORDINADO", false);
						}
						ItemScore averageColleagues = calculateAverageColleagues(averageManagers, averagePeers, averageDirectReports, managers, peers, directReports);
						sectionCurrentPerformanceByColleagues = sectionCurrentPerformanceByColleagues.add(averageColleagues.getCurrentPerformance());
						sectionDesiredPerformanceByColleagues = sectionDesiredPerformanceByColleagues.add(averageColleagues.getDesiredPerformance());
						
						Item item = Item.builder()
										.name(itemTemplate.getTitle())
										.description(itemTemplate.getDescription())
										.currentPerformanceByColleagues(averageColleagues.getCurrentPerformance())
										.currentPerformanceByDirectReports(averageDirectReports.getCurrentPerformance())
										.currentPerformanceByManagers(averageManagers.getCurrentPerformance())
										.currentPerformanceByPeers(averagePeers.getCurrentPerformance())
										.currentPerformanceByMe(averageMe.getCurrentPerformance())
										.desiredPerformanceByColleagues(averageColleagues.getDesiredPerformance())
										.desiredPerformanceByDirectReports(averageDirectReports.getDesiredPerformance())
										.desiredPerformanceByManagers(averageManagers.getDesiredPerformance())
										.desiredPerformanceByPeers(averagePeers.getDesiredPerformance())
										.desiredPerformanceByMe(averageMe.getDesiredPerformance())
										.build();
						
						
						sec.getItems().add(item);
					}
				}
				
				if (!sec.getItems().isEmpty()) {
					BigDecimal totalItems = BigDecimal.valueOf(sec.getItems().size());
					sec.setCurrentPerformanceByColleagues(sectionCurrentPerformanceByColleagues.divide(totalItems, 2, RoundingMode.HALF_UP));
					sec.setCurrentPerformanceByMe(sectionCurrentPerformanceByMe.divide(totalItems, 2, RoundingMode.HALF_UP));
					sec.setDesiredPerformanceByColleagues(sectionDesiredPerformanceByColleagues.divide(totalItems, 2, RoundingMode.HALF_UP));
					sec.setDesiredPerformanceByMe(sectionDesiredPerformanceByMe.divide(totalItems, 2, RoundingMode.HALF_UP));
				}
				sections.add(sec);
			}
		}
		return sections;
	}

	private ItemScore calculateAverageColleagues(ItemScore averageManagers, ItemScore averagePeers,
			ItemScore averageDirectReports, Integer managers, Integer peers, Integer directReports) {
		
		double currentManagers = (averageManagers.getCurrentPerformance() != null) ? averageManagers.getCurrentPerformance().doubleValue() : 0;
		double desiredManagers = (averageManagers.getDesiredPerformance() != null) ? averageManagers.getDesiredPerformance().doubleValue() : 0;
		double currentPeers = (averagePeers.getCurrentPerformance() != null) ? averagePeers.getCurrentPerformance().doubleValue() : 0;
		double desiredPeers = (averagePeers.getDesiredPerformance() != null) ? averagePeers.getDesiredPerformance().doubleValue() : 0;
		double currentDR = (averageDirectReports.getCurrentPerformance() != null) ? averageDirectReports.getCurrentPerformance().doubleValue() : 0;
		double desiredDR = (averageDirectReports.getDesiredPerformance() != null) ? averageDirectReports.getDesiredPerformance().doubleValue() : 0;
		
		double totalColleagues = managers.doubleValue() + peers.doubleValue() + directReports.doubleValue();
		double sumColleagues = currentManagers*managers.doubleValue() + currentPeers*peers.doubleValue() + currentDR*directReports.doubleValue();
		
		double sumDesiredColleagues = desiredManagers*managers.doubleValue() + desiredPeers*peers.doubleValue() + desiredDR*directReports.doubleValue();
		
		BigDecimal currentColleagues = BigDecimal.ZERO;
		if (totalColleagues != 0) {
			double cc = sumColleagues / totalColleagues;
			currentColleagues = BigDecimal.valueOf(cc).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		BigDecimal desiredColleagues = BigDecimal.ZERO;
		if (totalColleagues != 0) {
			double cc = sumDesiredColleagues / totalColleagues;
			desiredColleagues = BigDecimal.valueOf(cc).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		ItemScore averageColleagues = ItemScore.builder().currentPerformance(currentColleagues)
														 .desiredPerformance(desiredColleagues)
														 .build();
		return averageColleagues;
	}

	private List<Comment> getComments(EvaluationTemplate template, List<Evaluation> evaluations) {
		List<Comment> comments = new ArrayList<>();
		for (com.gire.eval360.reports.service.remote.dto.templates.Section section : template.getSections()) {
			if (section.getSectionType().equals(SectionType.QUESTIONS)){
				Long sectionId = section.getId();
				for (ItemTemplate itemTemplate : section.getItems()) {
					Long itemId = itemTemplate.getId();
					if (itemTemplate.getItemType().equals(ItemType.TEXTBOX)) {
						Comment comment = Comment.builder().question(itemTemplate.getDescription()).ownResponse("N/A").build();
						
						List<String> otherResponses = new ArrayList<>();
						for (Evaluation evaluation : evaluations){
							String response = getComment(sectionId, itemId, evaluation);
							otherResponses.add(response);
							
						}
						comment.setOtherResponses(otherResponses);
						comments.add(comment);
					}
				}
			}
		}
		return comments;
	}

	private String getComment(Long sectionId, Long itemId, Evaluation evaluation) {
		for (com.gire.eval360.reports.service.remote.dto.evaluations.Section section : evaluation.getSections()) {
			if (section.getId().longValue() == sectionId.longValue()) {
				for (com.gire.eval360.reports.service.remote.dto.evaluations.Item item: section.getItems()) {
					if (item.getId().longValue() == itemId.longValue()) {
						 return item.getValue();
					}
				}
			}
		}
		return "N/A";
	}

}
