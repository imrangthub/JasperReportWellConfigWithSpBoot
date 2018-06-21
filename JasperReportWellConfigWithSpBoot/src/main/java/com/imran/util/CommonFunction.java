package com.imran.util;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imran.report.CusJasperReportDef;
import com.imran.report.JasperExportFormat;


public class CommonFunction {

	
	public static String GetJson(Object obj) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = builder.create();
		
		return gson.toJson(obj);
	}
	
	public static Gson NewGson() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
	}
	
	public static String getJsoWithDateFormat(Object obj) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("dd-MM-yyyy HH:mm:ss:aa").create();
	  	return gson.toJson(obj);
	}
	
	
	public static void finalClose(Connection con,  ResultSet rs,Statement st){

		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
}
	
	
	public static String join(List<String> namesList) {
	    return String.join(",", namesList
	            .stream()
	            .map(name -> ("'" + name + "'"))
	            .collect(Collectors.toList()));
	}
	
	public static JasperExportFormat printFormat(String printFormat){
        if(printFormat==null){
            printFormat = "PDF";
        }
        if(printFormat.equals("PDF")){
            return JasperExportFormat.PDF_FORMAT;
        } else if(printFormat.equals("DOCX")){
            return JasperExportFormat.DOCX_FORMAT;
        } else if(printFormat.equals("XLSX")){
            return JasperExportFormat.XLSX_FORMAT;
        }
        return JasperExportFormat.PDF_FORMAT;
    }

    public static  String getReportPath(HttpServletRequest request,String path) {
    	  return request.getServletContext().getRealPath(path);
    	// return request.getSession().getServletContext().getRealPath(path);
    	 //path = this.getClass().getClassLoader().getResource("").getPath();
    }


    /**
     * write byte array to response to download/open report
     * @param jasperReport CusJasperReportDef
     * @param forceDownload whether force  browser to download, if false browser can open like image/pdf
     * @return
     * @throws IOException 
     */
    public static void respondReportOutput(CusJasperReportDef jasperReport, boolean forceDownload,HttpServletResponse response) throws IOException {
        if (jasperReport == null || jasperReport.getContent() == null) {
         
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
