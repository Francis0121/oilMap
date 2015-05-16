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

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> crawler() throws Exception {
		Double avgGasoline = http.sendGet();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("avgGasoline", avgGasoline);
		return response;
	}
}