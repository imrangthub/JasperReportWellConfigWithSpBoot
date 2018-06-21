package com.imran.util;

import java.util.Map;

import org.json.JSONObject;


public interface CommonFunctions {

	default Response getSuccessResponse(String message) {
		     Response response = new Response();
	         response.setSuccess(true);
	         response.setMessage(message); 
	         return response;
	    }
	 
	default    Response getSuccessResponse(String message, Response response) {
	        response.setSuccess(true);
	        response.setMessage(message); 
	        return response;
	    }

	default   Response getErrorResponse(String message) {
		  Response response = new Response();
		  response.setSuccess(false);
	      response.setMessage(message); 
	      return response;
	  }
	  
	default    Response getErrorResponse(String message, Response response) {
	        response.setSuccess(false);
	        response.setMessage(message); 
	        return response;
	    }
	
	default  Object getJsonValue(JSONObject json, String key) {
    	if(json.has(key)) {
    		if(json.get(key) != "" && json.get(key) != null) {
    		return json.get(key);
    	}
    	else {
    		return null;
    		}
    	}
		return null;
    }
	
	default String strReplace(String str, Map<String,Object> map) {
		String replaceStr = str;
		for (Map.Entry<String,Object> entry : map.entrySet()) {
			replaceStr = replaceStr.replaceAll(entry.getKey().toString(), entry.getValue().toString());

	    }
		return replaceStr;
	}
	

}
