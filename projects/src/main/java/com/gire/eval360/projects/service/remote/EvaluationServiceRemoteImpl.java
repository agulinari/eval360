package com.gire.eval360.projects.service.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gire.eval360.projects.service.remote.dto.evaluations.EvaluatedsRequest;

@Service
public class EvaluationServiceRemoteImpl implements EvaluationServiceRemote {

	@Autowired
	@Qualifier("evaluationsClient")
	private RestTemplate restTemplate;
	
	@Override
	public List<Long> getCompletedEvaluees(EvaluatedsRequest request) {
		
		//header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//httpEnitity       
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request,headers);
		ResponseEntity<List<Long>> call = restTemplate.exchange("/completed", HttpMethod.POST, requestEntity,new ParameterizedTypeReference<List<Long>>() {});
		
		return call.getBody();
		
	}

}
