package com.oilMap.server.controller;

import com.oilMap.server.util.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HostController {
	
	private static Logger logger = LoggerFactory.getLogger(HostController.class);

	@Autowired
	private Http http;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(Model model) {
		return "host";
	}
	
	@ResponseBody
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public Boolean crawler() throws Exception {
		Boolean isSuccess = http.sendGet();
		return isSuccess;
	}
}