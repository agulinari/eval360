package com.gire.eval360.reports.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gire.eval360.reports.domain.AreaToImprove;
import com.gire.eval360.reports.domain.Comment;
import com.gire.eval360.reports.domain.Item;
import com.gire.eval360.reports.domain.ItemScore;
import com.gire.eval360.reports.domain.ReportData;
import com.gire.eval360.reports.domain.Score;
import com.gire.eval360.reports.domain.Section;
import com.gire.eval360.reports.service.remote.EvaluationServiceRemote;
import com.gire.eval360.reports.service.remote.TemplateServiceRemote;
import com.gire.eval360.reports.service.remote.dto.evaluations.Evaluation;
import com.gire.eval360.reports.service.remote.dto.templates.EvaluationTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemTemplate;
import com.gire.eval360.reports.service.remote.dto.templates.ItemType;
import com.gire.eval360.reports.service.remote.dto.templates.SectionType;
import com.google.common.collect.Lists;

import reactor.core.publisher.Mono;

@Service
public class ReportServiceImpl implements ReportService {

	private final EvaluationServiceRemote evaluationServiceRemote;
	private final TemplateServiceRemote templateServiceRemote;
	
	@Autowired
	public ReportServiceImpl(EvaluationServiceRemote evaluationServiceRemote,
							 TemplateServiceRemote templateServiceRemote) {
		
		this.evaluationServiceRemote = evaluationServiceRemote;
		this.templateServiceRemote = templateServiceRemote;
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
		Integer managers = countRelationship(evaluations, "Jefe");
		Integer peers = countRelationship(evaluations, "Par");
		Integer directReports = countRelationship(evaluations, "Subordinado");
		List<Item> items = calculateEvaluatedItems(template, evaluations);
		List<String> strengths = getStrengths(items);
		List<String> weaknesses = getWeaknesses(items);
		List<AreaToImprove> areasToImprove = getAreasToImprove(items);
		List<Comment> comments = getComments(template, evaluations);
		List<Section> detailedResults = getDetailedResults(template, evaluations);
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
						ItemScore average = calculateAverage(itemId, evaluations, "");
						ItemScore averageManagers = calculateAverage(itemId, evaluations, "Jefe");
						ItemScore averagePeers = calculateAverage(itemId, evaluations, "Par");
						ItemScore averageDirectReports = calculateAverage(itemId, evaluations, "Subordinado");
						
						Item item = Item.builder()
										.name(itemTemplate.getTitle())
										.description(itemTemplate.getDescription())
										.currentPerformanceByColleagues(average.getCurrentPerformance())
										.currentPerformanceByDirectReports(averageDirectReports.getCurrentPerformance())
										.currentPerformanceByManagers(averageManagers.getCurrentPerformance())
										.currentPerformanceByPeers(averagePeers.getCurrentPerformance())
										.currentPerformanceByMe(BigDecimal.ZERO)
										.desiredPerformanceByColleagues(average.getDesiredPerformance())
										.desiredPerformanceByDirectReports(averageDirectReports.getDesiredPerformance())
										.desiredPerformanceByManagers(averageManagers.getDesiredPerformance())
										.desiredPerformanceByPeers(averagePeers.getDesiredPerformance())
										.desiredPerformanceByMe(BigDecimal.ZERO)
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

	private ItemScore calculateAverage(Long itemId, List<Evaluation> evaluations, String relationship) {
		
		Integer totalCurrentScore = 0;
		Integer totalDesiredScore = 0;
		Integer evalCount = evaluations.size();
		for (Evaluation evaluation : evaluations) {
			
			if (relationship.isEmpty() || evaluation.getRelationship().equals(relationship)) {
				
				for (com.gire.eval360.reports.service.remote.dto.evaluations.Section section : evaluation.getSections()) {
					for (com.gire.eval360.reports.service.remote.dto.evaluations.Item item :  section.getItems()) {
						if ((item.getId().longValue() == itemId.longValue()) && (item.getType().equals(ItemType.RATING.toString()))){
							Integer score = Integer.valueOf(item.getValue());
							Integer score1 = Integer.valueOf(item.getValue1());
							totalCurrentScore = totalCurrentScore + score;
							totalDesiredScore = totalDesiredScore + (score1 - score);
						}
					}
				}
				
			}
		}
		BigDecimal currentScore = BigDecimal.ZERO;
		if (evalCount > 0) {
			currentScore = BigDecimal.valueOf(totalCurrentScore / evalCount);
		}
		BigDecimal desiredScore = BigDecimal.ZERO;
		if (evalCount > 0) {
			desiredScore = BigDecimal.valueOf(totalDesiredScore / evalCount);
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
	
	private List<AreaToImprove> getAreasToImprove(List<Item> calculatedItems) {
		calculatedItems.sort((Item i1, Item i2) -> i1.getDesiredPerformanceByColleagues().compareTo(i2.getDesiredPerformanceByColleagues()));
		List<Item> reverseList = Lists.reverse(calculatedItems);

		List<AreaToImprove> areas = reverseList.stream()
				.map(i -> buildAreaToImprove(i)).collect(Collectors.toList());
		return areas;
	}

	private AreaToImprove buildAreaToImprove(Item i) {
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
		
		Score scoreDR = getScore(percentDpDR);
		Score scoreManagers = getScore(percentDpManagers);
		Score scoreMe = getScore(percentDpMe);
		Score scorePeers = getScore(percentDpPeers);
		
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

	private Score getScore(BigDecimal dp) {

		if (dp.compareTo(BigDecimal.valueOf(33))<0) {
			return Score.SMALL;
		}else if (dp.compareTo(BigDecimal.valueOf(66))>0) {
			return Score.LARGE;
		}else {
			return Score.MEDIUM;
		}
	}

	private List<Section> getDetailedResults(EvaluationTemplate template, List<Evaluation> evaluations) {
		
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
				for (ItemTemplate itemTemplate : section.getItems()) {
					Long itemId = itemTemplate.getId();

					if (itemTemplate.getItemType().equals(ItemType.TEXTBOX)) {
						Comment comment = Comment.builder().question(itemTemplate.getDescription()).ownResponse("N/A").build();
						List<String> otherResponses = new ArrayList<>();
						for (Evaluation evaluation : evaluations){
							String response = getComment(section.getId(), itemId, evaluation);
							otherResponses.add(response);
							
						}
						comment.setOtherResponses(otherResponses);
						sec.getComments().add(comment);
					} else if (itemTemplate.getItemType().equals(ItemType.RATING)){
						ItemScore average = calculateAverage(itemId, evaluations, "");
						ItemScore averageManagers = calculateAverage(itemId, evaluations, "Jefe");
						ItemScore averagePeers = calculateAverage(itemId, evaluations, "Par");
						ItemScore averageDirectReports = calculateAverage(itemId, evaluations, "Subordinado");
						
						Item item = Item.builder()
										.name(itemTemplate.getTitle())
										.description(itemTemplate.getDescription())
										.currentPerformanceByColleagues(average.getCurrentPerformance())
										.currentPerformanceByDirectReports(averageDirectReports.getCurrentPerformance())
										.currentPerformanceByManagers(averageManagers.getCurrentPerformance())
										.currentPerformanceByPeers(averagePeers.getCurrentPerformance())
										.currentPerformanceByMe(BigDecimal.ZERO)
										.desiredPerformanceByColleagues(average.getDesiredPerformance())
										.desiredPerformanceByDirectReports(averageDirectReports.getDesiredPerformance())
										.desiredPerformanceByManagers(averageManagers.getDesiredPerformance())
										.desiredPerformanceByPeers(averagePeers.getDesiredPerformance())
										.desiredPerformanceByMe(BigDecimal.ZERO)
										.build();
						
						sec.getItems().add(item);
					}
				}
				sections.add(sec);
			}
		}
		return sections;
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
