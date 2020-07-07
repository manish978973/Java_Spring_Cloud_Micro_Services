package com.learnprogramming.photoapp.api.users.shared;


import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;


@Component
public class FeignErrorDecoder implements ErrorDecoder {
	
	Environment environment;
	
	public FeignErrorDecoder(Environment environment) {
		this.environment=environment;
	}

	@Override
	public Exception decode(String arg0, Response arg1) {
		// TODO Auto-generated method stub

       switch(arg1.status()) {
       case 400:
    	   break;
    	   
       case 404:
    	   if(arg0.contains("getAlbums"))
    			   {
    		   return new ResponseStatusException(HttpStatus.valueOf(arg1.status()),environment.getProperty("error404"));
    			   }
    		   break; 	   
    	   
       default:
    	   return new Exception(arg1.reason());
       }
       
       return null;
	
	

}
	
}
