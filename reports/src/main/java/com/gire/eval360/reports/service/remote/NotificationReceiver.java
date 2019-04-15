package com.gire.eval360.reports.service.remote;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.reports.domain.ReportData;
import com.gire.eval360.reports.repository.ReportRepository;
import com.gire.eval360.reports.service.ReportService;
import com.gire.eval360.reports.service.remote.dto.notifications.NotificationReviewerDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NotificationReceiver {
	
	private final ReportService reportService;
	private final ReportRepository repository;
	private final NotificationSender notificationSender;
	
	@Autowired
	public NotificationReceiver(final ReportService reportService, final ReportRepository repository, final NotificationSender notificationSender) {
		this.reportService = reportService;
		this.repository = repository;
		this.notificationSender = notificationSender;
	}
	

    @KafkaListener(topics = "${app.topic.notificationRV}", containerFactory = "kafkaListenerNotificationRVContainerFactory")
    public void listenTrx(@Payload NotificationReviewerDto data, @Headers MessageHeaders headers) {
    	log.info("Se ha recibido la notificacion de una evaluacion finalizada con los datos: "+ data);
    	
    	if (data.getIdFeedbackProvider().longValue() != 0L) {
    		log.info("No se genera el reporte, aun quedan evaluaciones por terminar para el idEvaluee {}", data.getIdEvaluee());
    		return;
    	}
    	
    	ReportData response = this.repository.findByIdEvaluee(data.getIdEvaluee()).block();
    	if (response == null) {
    		log.info("Generando reporte");
    		Mono<ReportData> reportData = this.reportService.generateReport(data.getIdProject(), data.getIdEvaluee(), data.getIdTemplate());
    		    
    	    Mono<ReportData> savedReport = reportData.flatMap(report-> {
    	    	return repository.save(report);
    		})
    	    .doOnSuccess(report -> notificationSender.sendNotification(data))
    	    .doOnError(e->log.error(e.getMessage()));
    	    
    	    savedReport.subscribe();
    	}	
    		 		   	
    }
    
}