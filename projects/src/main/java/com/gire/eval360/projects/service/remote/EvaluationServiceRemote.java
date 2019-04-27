package com.gire.eval360.projects.service.remote;

import java.util.List;

import com.gire.eval360.projects.service.remote.dto.evaluations.EvaluatedsRequest;

public interface EvaluationServiceRemote {

	List<Long> getCompletedEvaluees(EvaluatedsRequest request);

}
