package com.neeraj.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neeraj.response.ApiResponse;

@RestController
public class HomeController {
	
	@GetMapping
	public ApiResponse HomeControllerHandler() {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Hello Spring Boot");
		return apiResponse;
	}

}
