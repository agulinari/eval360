package com.gire.eval360.projects.service;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gire.eval360.projects.domain.EvaluationStatus;
import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.EvalueeFeedbackProvider;
import com.gire.eval360.projects.domain.EvalueeReviewer;
import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.ProjectAdmin;
import com.gire.eval360.projects.domain.Relationship;
import com.gire.eval360.projects.domain.Reviewer;
import com.gire.eval360.projects.domain.Status;
import com.gire.eval360.projects.domain.dto.CompletedEvaluee;
import com.gire.eval360.projects.domain.dto.PendingEvaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.CreateReviewer;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.repository.EvalueeFeedbackProviderRepository;
import com.gire.eval360.projects.repository.ProjectRepository;
import com.gire.eval360.projects.service.remote.EvaluationServiceRemote;
import com.gire.eval360.projects.service.remote.TemplateServiceRemote;
import com.gire.eval360.projects.service.remote.UserServiceRemote;
import com.gire.eval360.projects.service.remote.dto.users.UserResponse;


@RunWith(SpringRunner.class)
public class ProjectServiceTest {

	@MockBean
	private ProjectRepository projectRepository;
	
	@MockBean
	private EvalueeFeedbackProviderRepository efpRepository;
	
	@MockBean
	private NotificationFeedBackSender notificationFeedBackSender;
	
	@MockBean
	private UserServiceRemote userServiceRemote; 

	@MockBean
	private TemplateServiceRemote templateServiceRemote;
	
	@MockBean
	private EvaluationServiceRemote evaluationServiceRemote;

	private ProjectService projectService;
	

    @Before
    public void setUp() {
    	this.projectService = new ProjectServiceImpl(projectRepository, efpRepository, 
    			notificationFeedBackSender, userServiceRemote, templateServiceRemote, evaluationServiceRemote);
    }
	
	@Test
	public void testCreateProject() {
		
	   	CreateReviewer reviewer = CreateReviewer.builder().id(1L).idUser(2L).build();
    	CreateFeedbackProvider feedbackProvider = CreateFeedbackProvider.builder().id(1L).idUser(2L).relationship(Relationship.JEFE).build();
    	CreateEvaluee evaluee = CreateEvaluee.builder().id(1L).idUser(1L).feedbackProvider(feedbackProvider).reviewer(reviewer).build();
    	CreateProjectAdmin admin = CreateProjectAdmin.builder().creator(true).id(1L).idUser(3L).build();
    	CreateProjectRequest request = CreateProjectRequest.builder().name("Test").description("Test").evaluee(evaluee).admin(admin).build();

    	BDDMockito.when(projectRepository.save(BDDMockito.any())).then(returnsFirstArg());
    	
    	Project project = projectService.createProject(request);
    	
		BDDMockito.verify(projectRepository, BDDMockito.times(1)).save(project);

		BDDMockito.verify(notificationFeedBackSender, BDDMockito.times(1)).sendNotificationFeedBackProvider(BDDMockito.any());
		
		Assert.assertEquals(project.getName(), "Test");
		Assert.assertEquals(project.getStatus(), Status.PENDIENTE);
		Assert.assertEquals(project.getEvaluees().size(), 1);
		Assert.assertEquals(project.getFeedbackProviders().size(), 1);
		Assert.assertEquals(project.getProjectAdmins().size(), 1);
		Assert.assertEquals(project.getReviewers().size(), 1);
		Evaluee ev = project.getEvaluees().iterator().next();
		FeedbackProvider fp = project.getFeedbackProviders().iterator().next();
		Reviewer rw = project.getReviewers().iterator().next();
		Assert.assertEquals(ev.getIdUser(), Long.valueOf(1));
		Assert.assertEquals(ev.getFeedbackProviders().get(0).getFeedbackProvider(), fp);
		Assert.assertEquals(ev.getReviewers().get(0).getReviewer(), rw);
		Assert.assertEquals(fp.getIdUser(), Long.valueOf(2));
		Assert.assertEquals(rw.getIdUser(), Long.valueOf(2));
		ProjectAdmin pa = project.getProjectAdmins().iterator().next();
		Assert.assertEquals(pa.getIdUser(), Long.valueOf(3));

	}
		
	@Test
	public void testReportFeedback() {
		ReportFeedbackRequest request = ReportFeedbackRequest.builder().idEvaluee(1L).idFeedbackProvider(1L).build();
		
		EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
		Project project = new Project();
		project.setId(1L);
		project.setIdEvaluationTemplate(1L);
		Evaluee evaluee = new Evaluee();
		evaluee.setId(1L);
		evaluee.setIdUser(1L);
		evaluee.setProject(project);
		efp.setEvaluee(evaluee);
		efp.setFeedbackProvider(new FeedbackProvider());
		efp.setId(1L);
		efp.setRelationship(Relationship.JEFE);
		efp.setStatus(EvaluationStatus.PENDIENTE);
		
		List<EvalueeFeedbackProvider> efps = new ArrayList<>();
		efps.add(efp);
		evaluee.setFeedbackProviders(efps);
		
		BDDMockito.when(efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(), request.getIdFeedbackProvider())).thenReturn(efp);

		projectService.reportFeedback(request);
		
		BDDMockito.verify(notificationFeedBackSender, BDDMockito.times(1)).sendNotificationReviewer(BDDMockito.any());

	}
	
	@Test
	public void testNotReportFeedback() {
		ReportFeedbackRequest request = ReportFeedbackRequest.builder().idEvaluee(1L).idFeedbackProvider(1L).build();
				
		Project project = new Project();
		project.setId(1L);
		project.setIdEvaluationTemplate(1L);
		Evaluee evaluee = new Evaluee();
		evaluee.setId(1L);
		evaluee.setIdUser(1L);
		evaluee.setProject(project);
		
		EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
		efp.setEvaluee(evaluee);
		efp.setFeedbackProvider(new FeedbackProvider());
		efp.setId(1L);
		efp.setRelationship(Relationship.JEFE);
		efp.setStatus(EvaluationStatus.PENDIENTE);
		
		EvalueeFeedbackProvider efp1 = new EvalueeFeedbackProvider();
		efp1.setEvaluee(evaluee);
		efp1.setFeedbackProvider(new FeedbackProvider());
		efp1.setId(2L);
		efp1.setRelationship(Relationship.PAR);
		efp1.setStatus(EvaluationStatus.PENDIENTE);
		
		List<EvalueeFeedbackProvider> efps = new ArrayList<>();
		efps.add(efp);
		efps.add(efp1);
		evaluee.setFeedbackProviders(efps);

		BDDMockito.when(efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(), request.getIdFeedbackProvider())).thenReturn(efp);

		projectService.reportFeedback(request);
		
		BDDMockito.verify(notificationFeedBackSender, BDDMockito.times(0)).sendNotificationReviewer(BDDMockito.any());

	}


	@Test
	public void testAddEvaluee() {
	   	CreateReviewer reviewer = CreateReviewer.builder().id(2L).idUser(2L).build();
    	CreateFeedbackProvider feedbackProvider = CreateFeedbackProvider.builder().id(2L).idUser(2L).relationship(Relationship.JEFE).build();
		CreateEvaluee evaluee = CreateEvaluee.builder().id(2L).idUser(1L).feedbackProvider(feedbackProvider).reviewer(reviewer).build();

		Project project = mockProject();
		
		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		projectService.addEvaluee(1L, evaluee);
		
		BDDMockito.verify(notificationFeedBackSender).sendNotificationFeedBackProvider(BDDMockito.any());

		
	}
	
	private Project mockProject() {
		
		ProjectAdmin admin = new ProjectAdmin();
		admin.setId(1L);
		admin.setIdUser(3L);
		admin.setCreator(true);
		List<ProjectAdmin> admins = new ArrayList<>();
		admins.add(admin);
		 
		Reviewer reviewer = new Reviewer();
		reviewer.setId(1L);
		reviewer.setIdUser(2L);
		List<Reviewer> reviewers = new ArrayList<>();
		reviewers.add(reviewer);
		
		FeedbackProvider fp = new FeedbackProvider();
		fp.setId(1L);
		fp.setIdUser(2L);
		List<FeedbackProvider> feedbackProviders = new ArrayList<>();
		feedbackProviders.add(fp);
		
		Evaluee evaluee = new Evaluee();
		evaluee.setId(1L);
		evaluee.setIdUser(1L);
		List<Evaluee> evaluees = new ArrayList<>();
		evaluees.add(evaluee);
		
		EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
		efp.setEvaluee(evaluee);
		efp.setFeedbackProvider(fp);
		efp.setRelationship(Relationship.JEFE);
		efp.setStatus(EvaluationStatus.PENDIENTE);
		List<EvalueeFeedbackProvider> efps = new ArrayList<>();
		efps.add(efp);
		evaluee.setFeedbackProviders(efps);
		fp.setEvaluees(efps);

		EvalueeReviewer erw = new EvalueeReviewer();
		erw.setEvaluee(evaluee);
		erw.setReviewer(reviewer);
		List<EvalueeReviewer> erws = new ArrayList<>();
		erws.add(erw);
		evaluee.setReviewers(erws);
		reviewer.setEvaluees(erws);
		
		Project p = new Project();
		p.setDescription("Project Test");
		p.setId(1L);
		p.setEvaluees(evaluees);
		p.setFeedbackProviders(feedbackProviders);
		p.setIdEvaluationTemplate(1L);
		p.setName("Project");
		p.setStatus(Status.PENDIENTE);
		p.setProjectAdmins(admins);
		p.setReviewers(reviewers);
		fp.setProject(p);
		evaluee.setProject(p);
		reviewer.setProject(p);
		admin.setProject(p);
		
		return p;
	}

	
	@Test
	public void testAddAdmin() {
	   	CreateProjectAdmin admin = CreateProjectAdmin.builder().id(2L).idUser(4L).creator(false).build();

		Project project = mockProject();
		
		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		projectService.addAdmin(1L, admin);
		
	}
	
	@Test
	public void testGetEvalueesForFeedback() {
		
		Project project = mockProject();
		
		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		List<Evaluee> evaluees = projectService.getEvalueesForFeedback(1L, 2L);
		
		Assert.assertEquals(1, evaluees.size());
		Assert.assertEquals(Long.valueOf(1), evaluees.get(0).getIdUser());
	}
	

	@Test
	public void testGetProjectStatus() {
		
		UserResponse user1 = new UserResponse();
		user1.setUsername("user");
		user1.setMail("user@gmail.com");
		user1.setEnabled(true);

		UserResponse user2 = new UserResponse();
		user2.setUsername("user2");
		user2.setMail("user2@gmail.com");
		user2.setEnabled(true);

		UserResponse user3 = new UserResponse();
		user3.setUsername("user");
		user3.setMail("user@gmail.com");
		user3.setEnabled(true);

		BDDMockito.when(userServiceRemote.getUserById(1L)).thenReturn(user1);
		BDDMockito.when(userServiceRemote.getUserById(2L)).thenReturn(user2);
		BDDMockito.when(userServiceRemote.getUserById(3L)).thenReturn(user3);
		
		Project project = mockProject();
		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		Optional<ProjectStatus> result = projectService.getProjectStatus(1L);
		
		Assert.assertTrue(result.isPresent());
	}
	

	@Test
	public void testGetPendingEvalueesForUser() {
		
		Project project = mockProject();
		
		UserResponse user = new UserResponse();
		user.setUsername("user");
		user.setMail("user@gmail.com");
		user.setEnabled(true);
		BDDMockito.when(userServiceRemote.getUserById(1L)).thenReturn(user);

		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		List<PendingEvaluee> result = projectService.getPendingEvalueesForUser(1L, 2L);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getIdUser());
	}
	

	@Test
	public void testGetCompletedEvalueesForUser() {
		
		Project project = mockProject();
		
		UserResponse user = new UserResponse();
		user.setUsername("user");
		user.setMail("user@gmail.com");
		user.setEnabled(true);
		BDDMockito.when(userServiceRemote.getUserById(1L)).thenReturn(user);
		
		BDDMockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
		
		List<CompletedEvaluee> result = projectService.getCompletedEvalueesForUser(1L, 2L);
		
		Assert.assertEquals(0, result.size());
	}
	
	
}
