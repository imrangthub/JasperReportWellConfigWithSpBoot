package com.imran.report;

import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imran.model.BookAuthor;
import com.imran.service.BookAuthorServerSideService;
import com.imran.report.JasperService;
import com.imran.util.CommonFunction;
import com.imran.report.CusJasperReportDef;

@Service
@Transactional
public class ReportGenerationService {
	
	 @Autowired
	 JasperService jasperService;
	 
	@Autowired
	private BookAuthorServerSideService bookAuthorServerSideService;

	
	public CusJasperReportDef prescription(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		 parameterMap.put("mysoft","mysoft");
		 String reportFormate = (String)request.getParameter("reportFormate"); 		 
		 List<BookAuthor> authorList = new ArrayList<BookAuthor>();
		 authorList =  bookAuthorServerSideService.list();
		 
		CusJasperReportDef report = new CusJasperReportDef();
		report.setOutputFilename("prescription");
		report.setReportName("prescription");
		report.setReportDir(CommonFunction.getReportPath(request,"/WEB-INF/views/report/prescription/"));
		report.setReportFormat(CommonFunction.printFormat(reportFormate));
		report.setParameters(parameterMap);
		report.setReportData(authorList);
		ByteArrayOutputStream baos = null;
		try {
			baos = jasperService.generateReport(report);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		report.setContent(baos.toByteArray()); 
		return report;
		
	}


}
