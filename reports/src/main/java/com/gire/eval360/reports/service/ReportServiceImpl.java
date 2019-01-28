package com.gire.eval360.reports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gire.eval360.reports.domain.AreaToImprove;
import com.gire.eval360.reports.domain.Comment;
import com.gire.eval360.reports.domain.Item;
import com.gire.eval360.reports.domain.ReportData;
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
			
			return evaluations.map(es -> createReport(template, es));
		});
		
		return report;
	}

	private ReportData createReport(EvaluationTemplate template, List<Evaluation> evaluations) {
		
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
									  .username("User")
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
						BigDecimal average = calculateAverage(itemId, evaluations, "");
						BigDecimal averageManagers = calculateAverage(itemId, evaluations, "Jefe");
						BigDecimal averagePeers = calculateAverage(itemId, evaluations, "Par");
						BigDecimal averageDirectReports = calculateAverage(itemId, evaluations, "Subordinado");
						
						Item item = Item.builder()
										.name(itemTemplate.getTitle())
										.description(itemTemplate.getDescription())
										.currentPerformanceByColleagues(average)
										.currentPerformanceByDirectReports(averageDirectReports)
										.currentPerformanceByManagers(averageManagers)
										.currentPerformanceByPeers(averagePeers)
										.currentPerformanceByMe(BigDecimal.ZERO)
										.desiredPerformanceByColleagues(BigDecimal.ZERO)
										.desiredPerformanceByDirectReports(BigDecimal.ZERO)
										.desiredPerformanceByManagers(BigDecimal.ZERO)
										.desiredPerformanceByPeers(BigDecimal.ZERO)
										.desiredPerformanceByMe(BigDecimal.ZERO)
										.build();
						
						items.add(item);
			
					}
				}
			}
		}
		items.sort((Item i1, Item i2) -> i1.getCurrentPerformanceByColleagues().compareTo(i2.getCurrentPerformanceByColleagues()));
		return items;
		
	}

	private BigDecimal calculateAverage(Long itemId, List<Evaluation> evaluations, String relationship) {
		
		Integer totalScore = 0;
		Integer evalCount = evaluations.size();
		for (Evaluation evaluation : evaluations) {
			
			if (relationship.isEmpty() || evaluation.getRelationship().equals(relationship)) {
				
				for (com.gire.eval360.reports.service.remote.dto.evaluations.Section section : evaluation.getSections()) {
					for (com.gire.eval360.reports.service.remote.dto.evaluations.Item item :  section.getItems()) {
						if ((item.getId() == itemId) && (item.getType().equals(ItemType.RATING))){
							Integer score = Integer.valueOf(item.getValue());
							totalScore = totalScore + score;
						}
					}
				}
				
			}
		}
		BigDecimal averageScore = BigDecimal.ZERO;
		if (evalCount > 0) {
			averageScore = BigDecimal.valueOf(totalScore / evalCount);
		}
		return averageScore;
	}

	private List<String> getWeaknesses(List<Item> calculatedItems) {
		
		List<String> weaknesses = calculatedItems.stream().map(i -> i.getName()).limit(5).collect(Collectors.toList());
		return weaknesses;
	}

	private List<String> getStrengths(List<Item> calculatedItems) {
		
		List<Item> reverseList = Lists.reverse(calculatedItems);
		List<String> strengths = reverseList.stream().map(i -> i.getName()).limit(5).collect(Collectors.toList()); 
		return strengths;
	}
	
	private List<AreaToImprove> getAreasToImprove(List<Item> calculatedItems) {

		List<AreaToImprove> areas = calculatedItems.stream().map(i -> AreaToImprove.builder().areaAssessed(i.getName()).build()).collect(Collectors.toList());
		return areas;
	}

	private List<Section> getDetailedResults(EvaluationTemplate template, List<Evaluation> evaluations) {
		// TODO Auto-generated method stub
		return null;
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
