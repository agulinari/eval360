package com.gire.eval360.evaluations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EvaluationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluationsApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//
//		repository.deleteAll();
//
//		
//		Item item1 = Item.builder().id(Long.valueOf(1)).type("QUESTION").content("").value(Integer.valueOf(4)).build();
//		Item item2 = Item.builder().id(Long.valueOf(2)).type("QUESTION").content("").value(Integer.valueOf(3)).build();
//		Item item3 = Item.builder().id(Long.valueOf(3)).type("QUESTION").content("").value(Integer.valueOf(5)).build();
//		Item item4 = Item.builder().id(Long.valueOf(4)).type("QUESTION").content("").value(Integer.valueOf(1)).build();
//		Item item5 = Item.builder().id(Long.valueOf(5)).type("QUESTION").content("").value(Integer.valueOf(5)).build();
//		
//		Section s1 = Section.builder().id(Long.valueOf(1)).item(item1).build();
//		Section s2 = Section.builder().id(Long.valueOf(2)).item(item2).item(item3).build();
//		Section s3 = Section.builder().id(Long.valueOf(2)).item(item4).item(item5).build();
//
//		// save a couple of customers
//		Evaluation e1 = Evaluation.builder().id(BigInteger.valueOf(1))
//				.idProject(Long.valueOf(1))
//				.idEvaluee(Long.valueOf(1))
//				.idFeedbackProvider(Long.valueOf(1))
//				.section(s1)
//				.section(s2)
//				.build();
//
//		Evaluation e2 = Evaluation.builder().id(BigInteger.valueOf(2))
//				.idProject(Long.valueOf(1))
//				.idEvaluee(Long.valueOf(1))
//				.idFeedbackProvider(Long.valueOf(2))
//				.section(s3)
//				.build();
//
//		
//		repository.save(e1);
//		repository.save(e2);
//	}
}
