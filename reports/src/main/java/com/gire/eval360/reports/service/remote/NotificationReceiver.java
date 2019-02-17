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
	
	@Autowired
	public NotificationReceiver(final ReportService reportService, final ReportRepository repository) {
		this.reportService = reportService;
		this.repository = repository;
	}
	

    @KafkaListener(topics = "${app.topic.notificationRV}", containerFactory = "kafkaListenerNotificationRVContainerFactory")
    public void listenTrx(@Payload NotificationReviewerDto data, @Headers MessageHeaders headers) {
    	log.info("Se ha recibido la notificacion para generar el reporte con los datos: "+ data);
    	
    	ReportData response = this.repository.findByIdEvaluee(data.getIdEvaluee()).block();
    	if (response == null) {
    		log.info("Generando reporte");
    		Mono<ReportData> reportData = this.reportService.generateReport(data.getIdProject(), data.getIdEvaluee(), data.getIdTemplate());
    		    
    	    Mono<ReportData> savedReport = reportData.flatMap(report-> {
    	    	return repository.save(report);
    		}).doOnError(e->log.error(e.getMessage()));
    	    
    	    savedReport.subscribe();
    	}	
    		 		   	
    }
    
}