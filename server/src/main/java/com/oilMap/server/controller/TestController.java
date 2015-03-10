package com.oilMap.server.controller;

import com.oilMap.server.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class TestController {
	
	private static Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "test";
	}
	
	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Test getJson(){
		return new Test(100, "Get");
	}

	@ResponseBody
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public Test postJson(@RequestBody Test test){
		logger.debug("Post info " + test);
		return new Test(101, "Post");
	}
	
}