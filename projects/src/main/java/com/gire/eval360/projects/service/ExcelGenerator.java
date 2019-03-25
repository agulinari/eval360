package com.gire.eval360.projects.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gire.eval360.projects.domain.dto.AdminStatus;
import com.gire.eval360.projects.domain.dto.EvalueeDetail;
import com.gire.eval360.projects.domain.dto.FeedbackProviderStatus;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.dto.ReviewerStatus;

public class ExcelGenerator {
	  
	  public static ByteArrayInputStream projectToExcel(ProjectStatus project) throws IOException {
	    
		String[] columnsProject = {"Nombre", "Descripción", "Template"};
	    String[] columnsUsers = {"Usuario", "Rol", "Relación", "Evaluado"};
	    
	    try(
	        Workbook workbook = new XSSFWorkbook();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ){
	      CreationHelper createHelper = workbook.getCreationHelper();
	   
	      Sheet sheet = workbook.createSheet("Project");
	   
	      Font headerFont = workbook.createFont();
	      headerFont.setBold(true);
	      headerFont.setColor(IndexedColors.BLUE.getIndex());
	   
	      CellStyle headerCellStyle = workbook.createCellStyle();
	      headerCellStyle.setFont(headerFont);
	   
	      // Row for Header
	      Row headerRow = sheet.createRow(0);
	   
	      // Header
	      for (int col = 0; col < columnsProject.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(columnsProject[col]);
	        cell.setCellStyle(headerCellStyle);
	      }
	      
	     Row projectRow = sheet.createRow(1);
	     projectRow.createCell(0).setCellValue(project.getName());
	     projectRow.createCell(1).setCellValue(project.getDescription());
	     projectRow.createCell(2).setCellValue("TODO"); //TODO: agregar template
	     
	      // Row for Users Header
	      Row headerUsersRow = sheet.createRow(2);
	   
	      // Users Header
	      for (int col = 0; col < columnsUsers.length; col++) {
	        Cell cell = headerUsersRow.createCell(col);
	        cell.setCellValue(columnsUsers[col]);
	        cell.setCellStyle(headerCellStyle);
	      }

	     int rowIdx = 3;
	     List<FeedbackProviderStatus> fps = project.getFeedbackProvidersStatus();
	     for (FeedbackProviderStatus fp : fps) {
	    	 List<EvalueeDetail> evaluees = fp.getEvaluees();
	    	 for (EvalueeDetail evaluee : evaluees) {
	    		 Row row = sheet.createRow(rowIdx++);
	    		 row.createCell(0).setCellValue(fp.getUsername());
	    		 row.createCell(1).setCellValue("Feedback Provider");
	    		 row.createCell(2).setCellValue(evaluee.getRelationship().name());
	    		 row.createCell(3).setCellValue(evaluee.getUsername());
	    	 }
	     }
	     
	     List<ReviewerStatus> reviewers = project.getReviewersStatus();
	     for (ReviewerStatus reviewer : reviewers) {
	    	 List<EvalueeDetail> evaluees = reviewer.getEvaluees();
	    	 for (EvalueeDetail evaluee : evaluees) {
	    		 Row row = sheet.createRow(rowIdx++);
	    		 row.createCell(0).setCellValue(reviewer.getUsername());
	    		 row.createCell(1).setCellValue("Reviewer");
	    		 row.createCell(3).setCellValue(evaluee.getUsername());
	    	 }
	     }
	     
	     List<AdminStatus> admins = project.getAdminsStatus();
	     for (AdminStatus admin : admins) {
	    		 Row row = sheet.createRow(rowIdx++);
	    		 row.createCell(0).setCellValue(admin.getUsername());
	    		 row.createCell(1).setCellValue("Project Admin");
	     }
	     
	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    }
	  }
	}