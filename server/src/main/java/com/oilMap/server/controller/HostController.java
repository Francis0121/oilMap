package com.oilMap.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class HostController {
	
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.45 Safari/537.36";
	
	private static Logger logger = LoggerFactory.getLogger(HostController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(Model model) {
		return "host";
	}
	
	@ResponseBody
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public Boolean crawler() throws Exception {
		logger.debug("isSuccess??");
		sendGet();
		return true;
	}

	private void sendGet() throws Exception {

		String url = "http://p.search.naver.com/soil/search.naver?_callback=window.__jindo2_callback._oilsearch1234_0&where=nx&rcode=0&company=0&kind=1&self=0&search_result=1&api_type=2&start=1&q_sid=";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		logger.debug(response.toString());
	}


}