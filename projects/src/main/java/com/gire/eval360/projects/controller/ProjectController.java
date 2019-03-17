package com.gire.eval360.projects.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.Relationship;
import com.gire.eval360.projects.domain.dto.CompletedEvaluee;
import com.gire.eval360.projects.domain.dto.PendingEvaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.excel.ProjectAdminExcel;
import com.gire.eval360.projects.domain.excel.ProjectEvalueeExcel;
import com.gire.eval360.projects.domain.excel.ProjectExcel;
import com.gire.eval360.projects.domain.excel.ProjectFpExcel;
import com.gire.eval360.projects.domain.excel.ProjectReviewerExcel;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;
	
	@Autowired
	public ProjectController(final ProjectService projectService) {
		this.projectService= projectService;
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest request) {
		// TODO: validate request
		this.projectService.createProject(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/reportFeedback")
	public ResponseEntity<?> reportFeedback(@RequestBody ReportFeedbackRequest request) {
		this.projectService.reportFeedback(request);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{id}/addEvaluee")
	public ResponseEntity<?> addEvaluee(@PathVariable Long id, @RequestBody CreateEvaluee request) {
		// TODO: validate request
		this.projectService.addEvaluee(id, request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/{id}/addAdmin")
	public ResponseEntity<?> addAdmin(@PathVariable Long id, @RequestBody CreateProjectAdmin request) {
		// TODO: validate request
		this.projectService.addAdmin(id, request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}/feedbackProvider/{idFp}/evaluees")
	public List<Evaluee> getEvalueesForFeedback(@PathVariable Long id, @PathVariable Long idFp ) {
		return this.projectService.getEvalueesForFeedback(id, idFp);
	}
	
	@GetMapping("/{id}/status")
	public ResponseEntity<ProjectStatus> getProjectStatus(@PathVariable Long id) {
		Optional<ProjectStatus> projectStatus = this.projectService.getProjectStatus(id);
		return projectStatus.map(p -> ResponseEntity.ok().body(p)).orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/{id}/pending/{idFp}")
	public List<PendingEvaluee> getPendingEvalueesForUser(@PathVariable Long id, @PathVariable Long idFp ) {
		return this.projectService.getPendingEvalueesForUser(id, idFp);
	}
	
	@GetMapping("/{id}/completed/{idReviewer}")
	public List<CompletedEvaluee> getCompletedEvalueesForUser(@PathVariable Long id, @PathVariable Long idReviewer) {
		return this.projectService.getCompletedEvalueesForUser(id, idReviewer);
	}

	@PostMapping("/import")
	public ResponseEntity<?> importProject(@RequestParam("file") MultipartFile excelFile) throws IOException {
		
		ProjectExcel projectExcel = new ProjectExcel();
		
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		XSSFRow rowHeader = worksheet.getRow(1);
		
		projectExcel.setName(rowHeader.getCell(0).getStringCellValue());
		projectExcel.setDescription(rowHeader.getCell(0).getStringCellValue());
		projectExcel.setTemplate(rowHeader.getCell(0).getStringCellValue());
		List<ProjectEvalueeExcel> evaluees = new ArrayList<>();
		List<ProjectAdminExcel> admins = new ArrayList<>();
		
		for (int i=3; i<worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String rol = row.getCell(1).getStringCellValue();
			
			switch(rol) {
			case "Feedback Provider": 
				addEvalueeWithFP(evaluees, i, row);
				break;
			case "Reviewer":
				addEvalueeWithReviewer(evaluees, i, row);
				break;
			case "Project Admin":
				addProjectAdmin(admins, row);
				break;
			default:
				log.warn("No se pudo determinar el rol del usuario en posicion {}", i);
			}
			
		}
		projectExcel.setEvaluees(evaluees);
		projectExcel.setAdmins(admins);
		this.projectService.importProject(projectExcel);
		workbook.close();
		return new ResponseEntity<>(HttpStatus.CREATED);

	}


	private void addProjectAdmin(List<ProjectAdminExcel> admins, XSSFRow row) {
		ProjectAdminExcel admin = new ProjectAdminExcel();
		admin.setUsername(row.getCell(0).getStringCellValue());
		admins.add(admin);
	}


	private void addEvalueeWithReviewer(List<ProjectEvalueeExcel> evaluees, int i, XSSFRow row) {
		String evalueeName1 = row.getCell(3).getStringCellValue().trim().toUpperCase();
		String reviewerName = row.getCell(0).getStringCellValue().trim().toUpperCase();
		
		if (evalueeName1 == null || evalueeName1.isEmpty()) {
			throw new IllegalStateException("Formato excel invalido. Username de evaluado invalido. Fila:"+i);
		}
		
		if (reviewerName == null || reviewerName.isEmpty()) {
			throw new IllegalStateException("Formato excel invalido. Username de reviewer. Fila:"+i);
		}
		
		boolean evaluee1Exists = false;
		for (ProjectEvalueeExcel evaluee : evaluees) {
			if (evaluee.getUsername().equals(evalueeName1)) {
				evaluee1Exists = true;
				for (ProjectReviewerExcel reviewer : evaluee.getReviewers()) {
					if (reviewer.getUsername().equals(reviewerName)) {
						throw new IllegalStateException("Formato excel invalido. Reviewer duplicado para evaluee. Fila:"+i);
					}
				}
				ProjectReviewerExcel reviewer = new ProjectReviewerExcel();
				reviewer.setUsername(reviewerName);
				evaluee.getReviewers().add(reviewer);
				break;
			}
		}
		
		if (!evaluee1Exists) {
			ProjectEvalueeExcel evaluee = new ProjectEvalueeExcel();
			evaluee.setUsername(evalueeName1);
			List<ProjectReviewerExcel> reviewers = new ArrayList<>();
			List<ProjectFpExcel> fps = new ArrayList<>();
			ProjectReviewerExcel reviewer = new ProjectReviewerExcel();
			reviewer.setUsername(reviewerName);
			reviewers.add(reviewer);
			evaluee.setReviewers(reviewers);
			evaluee.setFeedbackProviders(fps);
			evaluees.add(evaluee);
		}
	}


	private void addEvalueeWithFP(List<ProjectEvalueeExcel> evaluees, int i, XSSFRow row) {
		
		String evalueeName = row.getCell(3).getStringCellValue().trim().toUpperCase();
		String fpName = row.getCell(0).getStringCellValue().trim().toUpperCase();
		String relationship = row.getCell(2).getStringCellValue().trim().toUpperCase();
		
		if (evalueeName == null || evalueeName.isEmpty()) {
			throw new IllegalStateException("Formato excel invalido. Username de evaluado invalido. Fila:"+i);
		}
		
		if (fpName == null || fpName.isEmpty()) {
			throw new IllegalStateException("Formato excel invalido. Username de feedback provider invalido. Fila:"+i);
		}
		
		if (relationship == null || relationship.isEmpty() || !Relationship.contains(relationship)) {
			throw new IllegalStateException("Formato excel invalido. Tipo de relación inválido. Fila:"+i);
		}
		
		boolean evalueeExists = false;
		for (ProjectEvalueeExcel evaluee : evaluees) {
			if (evaluee.getUsername().equals(evalueeName)) {
				evalueeExists = true;
				for (ProjectFpExcel fp : evaluee.getFeedbackProviders()) {
					if (fp.getUsername().equals(fpName)) {
						throw new IllegalStateException("Formato excel invalido. Feedback provider duplicado para evaluee. Fila:"+i);
					}
				}
				ProjectFpExcel fp = new ProjectFpExcel();
				fp.setRelationship(relationship);
				fp.setUsername(fpName);
				evaluee.getFeedbackProviders().add(fp);
				break;
			}
		}
		
		if (!evalueeExists) {
			ProjectEvalueeExcel evaluee = new ProjectEvalueeExcel();
			evaluee.setUsername(evalueeName);
			List<ProjectFpExcel> feedbackProviders = new ArrayList<>();
			List<ProjectReviewerExcel> reviewers = new ArrayList<>();
			ProjectFpExcel fp = new ProjectFpExcel();
			fp.setRelationship(relationship);
			fp.setUsername(fpName);
			feedbackProviders.add(fp);
			evaluee.setFeedbackProviders(feedbackProviders);
			evaluee.setReviewers(reviewers);
			evaluees.add(evaluee);
		}
	}
	
}
