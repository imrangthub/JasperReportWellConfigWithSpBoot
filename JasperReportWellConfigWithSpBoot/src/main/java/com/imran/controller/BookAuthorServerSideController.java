package com.imran.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.imran.model.BookAuthor;
import com.imran.report.ReportGenerationService;
import com.imran.service.BookAuthorServerSideService;



@Controller
@RequestMapping("library/author/serverSide")
public class BookAuthorServerSideController extends BaseController{
	
	@Autowired
	ReportGenerationService reportGenerationService;
	
	@Autowired
	private BookAuthorServerSideService bookAuthorServerSideService;
	
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(@RequestParam("gender") String gender) {
		Map<String, Object> results = new HashMap<String, Object>();
		List<BookAuthor> authorData = new ArrayList<BookAuthor>();
		
		authorData = bookAuthorServerSideService.list();
		
		if(!gender.equals("ALL")) {
			authorData = bookAuthorServerSideService.listByGender(gender);
		}

		int TotalRecords = authorData.size();
	    int TotalDisplayRecords = 2;
	    
		results.put("TotalRecords", TotalRecords);
		results.put("TotalDisplayRecords", TotalDisplayRecords);
		results.put("data", authorData);
		results.put("isError", Boolean.FALSE);
		results.put("message", "test data");
		return results;
	

	}
	

	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView listAuthor() {
		return new ModelAndView("library/bookAuthor/serverside");
	}
	
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addBook(@Valid BookAuthor bookAuthor, BindingResult bindingResult){
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (bookAuthor.getName().isEmpty() || bookAuthor.getCountry().isEmpty()) {
			  result.put("isError", Boolean.TRUE);
			  result.put("message","Require field is empty.");
			  return result;
		}
		
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors().toString());
			  result.put("isError", Boolean.TRUE);
			  result.put("message",bindingResult.getAllErrors().toString());
			  return result;
		}
		bookAuthorServerSideService.saveAuthor(bookAuthor);
		  result.put("isError", Boolean.FALSE);
		  result.put("message","Successfully saved  Book author.");
		
		return result;
	}
	
	@RequestMapping(value="/edit/{id}",  method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> edit(@PathVariable("id") Long id){
		Map<String, Object> result = new HashMap<String, Object>();
		BookAuthor bookAuthor = bookAuthorServerSideService.authorById(id);
		System.out.println(bookAuthor.getName());
		  result.put("isError", Boolean.FALSE);
		result.put("message","You Click On "+bookAuthor.getName());
		result.put("obj",bookAuthor);
		return result;
		
	}
	
	@RequestMapping(value="/delete/{id}",  method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(@PathVariable("id") Long id){
		Map<String, Object> result = new HashMap<String, Object>();

		BookAuthor bookAuthor = bookAuthorServerSideService.authorById(id);
		if(bookAuthor == null) {
			  result.put("isError", Boolean.TRUE);
			  result.put("message","Delete failed, Author Dose not exit any more.");
			return result;
		}
		
		bookAuthorServerSideService.delete(id);
		  result.put("isError", Boolean.FALSE);
		result.put("message","Successfully Delete Author");
		return result;
		
	}
	
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public  void prescription(@RequestParam(value = "reportFormate", required = true) String reportFormate, @RequestParam(value = "empId", required = true) long empId, HttpServletRequest request, HttpServletResponse response) throws IOException {	
		respondReportOutput(reportGenerationService.prescription(request,response),false,response);
		
	}
		




}
