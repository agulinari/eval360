package com.gire.eval360.evaluations.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gire.eval360.evaluations.domain.Evaluation;
import com.gire.eval360.evaluations.domain.Item;
import com.gire.eval360.evaluations.domain.Section;
import com.gire.eval360.evaluations.repository.EvaluationRepository;

@Configuration
public class MongoInitConfiguration {

	
	@Bean
	public CommandLineRunner loadData(EvaluationRepository repository) {
		return (args) -> {
			
			Section section1 = Section.builder().id(Long.valueOf(1))
											.item(Item.builder().id(Long.valueOf(1)).value("2").value1("3").type("RATING").build())
											.item(Item.builder().id(Long.valueOf(2)).value("4").value1("2").type("RATING").build())
											.item(Item.builder().id(Long.valueOf(3)).value("1").value1("5").type("RATING").build())
											.item(Item.builder().id(Long.valueOf(4)).value("5").value1("3").type("RATING").build())
											.item(Item.builder().id(Long.valueOf(5)).value("aasfasf").value1("").type("TEXTBOX").build())
											.build();
			Section section2 = Section.builder().id(Long.valueOf(2))
					.item(Item.builder().id(Long.valueOf(6)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(7)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(8)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(9)).value("5").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(10)).value("aasfasf").value1("").type("TEXTBOX").build())
					.build();
			Section section3 = Section.builder().id(Long.valueOf(3))
					.item(Item.builder().id(Long.valueOf(11)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(12)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(13)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(14)).value("5").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(15)).value("aasfasf").value1("").type("TEXTBOX").build())
					.build();
			Section section4 = Section.builder().id(Long.valueOf(4))
					.item(Item.builder().id(Long.valueOf(16)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(17)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(18)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(19)).value("5").value1("3").type("TEXTBOX").build())
					.build();
			Section section5 = Section.builder().id(Long.valueOf(5))
					.item(Item.builder().id(Long.valueOf(20)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(21)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(22)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(23)).value("5").value1("3").type("TEXTBOX").build())
					.build();
			Section section6 = Section.builder().id(Long.valueOf(6))
					.item(Item.builder().id(Long.valueOf(24)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(25)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(26)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(27)).value("5").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(28)).value("aasfasf").value1("").type("TEXTBOX").build())
					.build();
			Section section7 = Section.builder().id(Long.valueOf(7))
					.item(Item.builder().id(Long.valueOf(29)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(30)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(31)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(32)).value("5").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(33)).value("aasfasf").value1("").type("TEXTBOX").build())
					.build();
			Section section8 = Section.builder().id(Long.valueOf(8))
					.item(Item.builder().id(Long.valueOf(34)).value("2").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(35)).value("4").value1("2").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(36)).value("1").value1("5").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(37)).value("5").value1("3").type("RATING").build())
					.item(Item.builder().id(Long.valueOf(38)).value("aasfasf").value1("").type("TEXTBOX").build())
					.build();
			
			Evaluation evaluation = Evaluation.builder()
										.idEvaluee(Long.valueOf(3))
										.idProject(Long.valueOf(1))
										.idFeedbackProvider(Long.valueOf(4))
										.username("admin")
										.relationship("AUTO")
										.section(section1)
										.section(section2)
										.section(section3)
										.section(section4)
										.section(section5)
										.section(section6)
										.section(section7)
										.section(section8)			
										.build();
							
			repository.save(evaluation).subscribe();
		};
	}
}
