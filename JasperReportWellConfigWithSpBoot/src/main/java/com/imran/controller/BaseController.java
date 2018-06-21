package com.imran.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imran.util.CommonFunctions;
import com.imran.report.CusJasperReportDef;
@Controller
public class BaseController implements CommonFunctions {

    /**
     * convert response to JSON using gson converter
     * @param result
     * @return
     */
	public String renderJsonOutput(Object result) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("dd-MM-yyyy HH:mm:ss:aa").create();
	  	return gson.toJson(result);
    }

    /**
     * write byte array to response to download/open report
     * @param jasperReport CusJasperReportDef
     * @param forceDownload whether force  browser to download, if false browser can open like image/pdf
     * @return
     * @throws IOException 
     */
    public  void respondReportOutput(CusJasperReportDef jasperReport, boolean forceDownload,HttpServletResponse response) throws IOException {
        if (jasperReport == null || jasperReport.getContent() == null) {
        	 throw new FileNotFoundException("jasper Report Not found");
        } else {
        	
            String outputFileName = (jasperReport.getOutputFilename()) + "." + jasperReport.getReportFormat().getExtension();
            String contentDisposition = forceDownload == true ? "attachment;filename=\""+outputFileName+"\"": "filename=\""+outputFileName+"\"";
            response.setContentType(jasperReport.getReportFormat().getMimeType());
            response.setHeader("Content-Disposition", contentDisposition);
            response.addHeader("Content-Length",Integer.toString(jasperReport.getContent().length));
            OutputStream os = response.getOutputStream();

            try {
               os.write(jasperReport.getContent() , 0, jasperReport.getContent().length);
            } catch (Exception excp) {
               //handle error
            } finally {
                os.close();
            }
           
        }

    }
	

    
 

}
