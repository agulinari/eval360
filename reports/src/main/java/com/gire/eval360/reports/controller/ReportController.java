package com.gire.eval360.reports.controller;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.gire.eval360.reports.domain.ReportData;
import com.gire.eval360.reports.repository.ReportRepository;
import com.gire.eval360.reports.service.ReportService;
import com.gire.eval360.reports.service.dto.statistics.StatisticsSp;
import com.itextpdf.text.DocumentException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController  {

    private static final String OUTPUT_FILE = "test.pdf";
    private static final String UTF_8 = "UTF-8";
    
    private final TemplateEngine templateEngine;
    private final ReportRepository repository;
    private final ReportService service;

    @Autowired
    public ReportController(TemplateEngine templateEngine, ReportRepository repository, ReportService service) {
    	this.templateEngine = templateEngine;
    	this.repository = repository;
    	this.service = service;
    }
  
    @GetMapping("/model/{idEvaluee}")
    public Mono<ReportData> getModel(@PathVariable Long idEvaluee) {
    	return repository.findByIdEvaluee(idEvaluee);
    }

    @GetMapping("/statistics-project")
    public Mono<StatisticsSp> getInfoProjectForStatistics(@RequestParam Long idProject,@RequestParam Long idEvaluationTemplate){
    	return service.getInfoProjectForStatistics(idProject, idEvaluationTemplate);    	
    }
    
    @GetMapping("/generate")
    public void generateReport(HttpServletResponse response) throws DocumentException, IOException {
    	   // The data in our Thymeleaf templates is not hard-coded. Instead,
        // we use placeholders in our templates. We fill these placeholders
        // with actual data by passing in an object. 
        //
        // Note that we could also read this data from a JSON file, a database
        // a web service or whatever.
        ReportData data = service.generateReport(Long.valueOf(1), Long.valueOf(3), Long.valueOf(1)).block();
       
        Context context = new Context();
        context.setVariable("data", data);

        // Flying Saucer needs XHTML - not just normal HTML. To make our life
        // easy, we use JTidy to convert the rendered Thymeleaf template to
        // XHTML. Note that this might no work for very complicated HTML. But
        // it's good enough for a simple letter.
        String renderedHtmlContent = templateEngine.process("report", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("Code39.ttf", IDENTITY_H, EMBEDDED);

        // FlyingSaucer has a working directory. If you run this test, the working directory
        // will be the root folder of your project. However, all files (HTML, CSS, etc.) are
        // located under "/src/main/resources/templates". So we want to use this folder as the working
        // directory.
        String baseUrl = FileSystems
                                .getDefault()
                                .getPath("src", "main", "resources", "templates")
                                .toUri()
                                .toURL()
                                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();
        
        try {
	        // And finally, we create the PDF:
	       // OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
	        OutputStream outputStream = response.getOutputStream();
	        renderer.createPDF(outputStream);
	        response.setContentType("application/pdf");
	        response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. ", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }        
    }
    
    @GetMapping("/{idEvaluee}")
    public void downloadReport(@PathVariable Long idEvaluee, HttpServletResponse response) throws Exception {

        // The data in our Thymeleaf templates is not hard-coded. Instead,
        // we use placeholders in our templates. We fill these placeholders
        // with actual data by passing in an object. 
        //
        // Note that we could also read this data from a JSON file, a database
        // a web service or whatever.
        ReportData data = repository.findByIdEvaluee(idEvaluee).block();

        if (data == null) {
        	//TODO: devolver 404
        	return;
        }
        
        Context context = new Context();
        context.setVariable("data", data);

        // Flying Saucer needs XHTML - not just normal HTML. To make our life
        // easy, we use JTidy to convert the rendered Thymeleaf template to
        // XHTML. Note that this might no work for very complicated HTML. But
        // it's good enough for a simple letter.
        String renderedHtmlContent = templateEngine.process("report", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("Code39.ttf", IDENTITY_H, EMBEDDED);

        // FlyingSaucer has a working directory. If you run this test, the working directory
        // will be the root folder of your project. However, all files (HTML, CSS, etc.) are
        // located under "/src/main/resources/templates". So we want to use this folder as the working
        // directory.
        String baseUrl = FileSystems
                                .getDefault()
                                .getPath("src", "main", "resources", "templates")
                                .toUri()
                                .toURL()
                                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();
        
        try {
	        // And finally, we create the PDF:
	       // OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
	        OutputStream outputStream = response.getOutputStream();
	        renderer.createPDF(outputStream);
	        response.setContentType("application/pdf");
	        response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. ", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }        
         
    }

    @GetMapping("/test")
    public void testReport(HttpServletResponse response) throws Exception {

        Context context = new Context();
        String renderedHtmlContent = templateEngine.process("test/report", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("Code39.ttf", IDENTITY_H, EMBEDDED);

        String baseUrl = FileSystems
                                .getDefault()
                                .getPath("src", "main", "resources", "templates", "test")
                                .toUri()
                                .toURL()
                                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();
        
        try {
        // And finally, we create the PDF:
       // OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
        OutputStream outputStream = response.getOutputStream();
        renderer.createPDF(outputStream);
        response.setContentType("application/pdf");
        response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. ", ex);
            throw new RuntimeException("IOError writing file to output stream");
          }
       // outputStream.close();
        
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
	

}
