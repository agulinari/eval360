package com.gire.eval360.projects.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.dto.CompletedEvaluee;
import com.gire.eval360.projects.domain.dto.PendingEvaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.CreateReviewer;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.service.ProjectService;
import com.gire.eval360.projects.service.remote.TemplateServiceRemote;


@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProjectService projectService;
	
	@MockBean
	private TemplateServiceRemote templateServiceRemote;
	
    @Test
    public void testCreateProject() throws Exception {
    
    	CreateReviewer reviewer = CreateReviewer.builder().id(Long.valueOf(1)).idUser(Long.valueOf(2)).build();
    	CreateFeedbackProvider feedbackProvider = CreateFeedbackProvider.builder().id(Long.valueOf(1)).idUser(Long.valueOf(2)).build();
    	CreateEvaluee evaluee = CreateEvaluee.builder().id(Long.valueOf(1)).idUser(Long.valueOf(1)).feedbackProvider(feedbackProvider).reviewer(reviewer).build();
    	CreateProjectAdmin admin = CreateProjectAdmin.builder().creator(true).id(Long.valueOf(1)).idUser(Long.valueOf(3)).build();
    	CreateProjectRequest request = CreateProjectRequest.builder().description("Test").evaluee(evaluee).admin(admin).build();
    	    	
       	ObjectMapper objectMapper = new ObjectMapper();
       	String json = objectMapper.writeValueAsString(request);
       	
        this.mockMvc.perform(post("/projects/create")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(json))
                 .andDo(print())
                 .andExpect(status().isCreated());
        
		BDDMockito.verify(projectService, BDDMockito.times(1)).createProject(request);

    }
    
  
    @Test
    public void testReportFeedback() throws Exception {
    	
    	ReportFeedbackRequest request = ReportFeedbackRequest.builder().idEvaluee(Long.valueOf(1)).idFeedbackProvider(Long.valueOf(1)).build();
    	
      	ObjectMapper objectMapper = new ObjectMapper();
       	String json = objectMapper.writeValueAsString(request);
       	
        this.mockMvc.perform(put("/projects/reportFeedback")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(json))
                 .andDo(print())
                 .andExpect(status().isAccepted());
        
		BDDMockito.verify(projectService, BDDMockito.times(1)).reportFeedback(request);
    }
	
	
	@Test
	public void testAddEvaluee() throws Exception {
		
	   	CreateReviewer reviewer = CreateReviewer.builder().id(Long.valueOf(1)).idUser(Long.valueOf(2)).build();
    	CreateFeedbackProvider feedbackProvider = CreateFeedbackProvider.builder().id(Long.valueOf(1)).idUser(Long.valueOf(2)).build();
		CreateEvaluee evaluee = CreateEvaluee.builder().id(Long.valueOf(1)).idUser(Long.valueOf(1)).feedbackProvider(feedbackProvider).reviewer(reviewer).build();
		
		ObjectMapper objectMapper = new ObjectMapper();
       	String json = objectMapper.writeValueAsString(evaluee);
       	
        this.mockMvc.perform(post("/projects/1/addEvaluee")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(json))
                 .andDo(print())
                 .andExpect(status().isCreated());
        
		BDDMockito.verify(projectService, BDDMockito.times(1)).addEvaluee(Long.valueOf(1),evaluee);
        
	}
	
	@Test
	public void testAddAdmin() throws Exception {
		
	   	CreateProjectAdmin admin = CreateProjectAdmin.builder().id(Long.valueOf(1)).idUser(Long.valueOf(2)).creator(false).build();

		ObjectMapper objectMapper = new ObjectMapper();
       	String json = objectMapper.writeValueAsString(admin);
       	
        this.mockMvc.perform(post("/projects/1/addAdmin")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(json))
                 .andDo(print())
                 .andExpect(status().isCreated());
        
		BDDMockito.verify(projectService, BDDMockito.times(1)).addAdmin(Long.valueOf(1),admin);
        
	}
	
	@Test
	public void testGetEvalueesForFeedback() throws Exception {
		
       	Evaluee evaluee = new Evaluee();
       	evaluee.setId(Long.valueOf(1));
       	evaluee.setIdUser(Long.valueOf(2));
       	List<Evaluee> evaluees = new ArrayList<>();
       	evaluees.add(evaluee);
       	
       	BDDMockito.when(projectService.getEvalueesForFeedback(Long.valueOf(1), Long.valueOf(1))).thenReturn(evaluees);
       	
        this.mockMvc.perform(get("/projects/1/feedbackProvider/1/evaluees"))
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful())
                 .andExpect(jsonPath("$[0].idUser", is(2)));
                
	}
	
	@GetMapping("/{id}/status")
	public ResponseEntity<ProjectStatus> getProjectStatus(@PathVariable Long id) {
		Optional<ProjectStatus> projectStatus = this.projectService.getProjectStatus(id);
		return projectStatus.map(p -> ResponseEntity.ok().body(p)).orElse(ResponseEntity.notFound().build());
	}
	
	@Test
	public void testGetProjectStatus() throws Exception {
		
       	ProjectStatus projectStatus = new ProjectStatus();
       	projectStatus.setId(Long.valueOf(1));
       	projectStatus.setName("Test");
       	projectStatus.setDescription("Proyecto Test");
       	
       	BDDMockito.when(projectService.getProjectStatus(Long.valueOf(1))).thenReturn(Optional.of(projectStatus));
       	
        this.mockMvc.perform(get("/projects/1/status"))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.id", is(1)))
                 .andExpect(jsonPath("$.name", is("Test")))
                 .andExpect(jsonPath("$.description", is("Proyecto Test")));
                
	}
	
	@Test
	public void testGetProjectStatusNotFound() throws Exception {

       	BDDMockito.when(projectService.getProjectStatus(Long.valueOf(1))).thenReturn(Optional.empty());
       	
        this.mockMvc.perform(get("/projects/1/status"))
                 .andDo(print())
                 .andExpect(status().isNotFound());
                
	}
		
	@Test
	public void testGetPendingEvalueesForUser() throws Exception {
		
		PendingEvaluee evaluee = new PendingEvaluee();
       	evaluee.setId(Long.valueOf(1));
       	evaluee.setIdUser(Long.valueOf(2));
       	List<PendingEvaluee> evaluees = new ArrayList<>();
       	evaluees.add(evaluee);
       	
       	BDDMockito.when(projectService.getPendingEvalueesForUser(Long.valueOf(1), Long.valueOf(1))).thenReturn(evaluees);
       	
        this.mockMvc.perform(get("/projects/1/pending/1"))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$[0].idUser", is(2)));
                
	}
	
	@Test
	public void testGetCompletedEvalueesForUser() throws Exception {
		
		CompletedEvaluee evaluee = new CompletedEvaluee();
       	evaluee.setId(Long.valueOf(1));
       	evaluee.setIdUser(Long.valueOf(2));
       	List<CompletedEvaluee> evaluees = new ArrayList<>();
       	evaluees.add(evaluee);
       	
       	BDDMockito.when(projectService.getCompletedEvalueesForUser(Long.valueOf(1), Long.valueOf(1))).thenReturn(evaluees);
       	
        this.mockMvc.perform(get("/projects/1/completed/1"))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$[0].idUser", is(2)));
                
	}
    
    
    
}
