package com.oilMap.server.controller;

import com.oilMap.server.batch.BatchService;
import com.oilMap.server.batch.Ranking;
import com.oilMap.server.batch.RankingFilter;
import com.oilMap.server.util.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HostController {
	
	private static Logger logger = LoggerFactory.getLogger(HostController.class);

	@Autowired
	private Http http;

	@Autowired
	private BatchService batchService;
	
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
	
	@ResponseBody
	@RequestMapping(value = "/ranking", method = RequestMethod.POST)
	public Map<String, Object> ranking(){
		Map<String, Object> response = new HashMap<String, Object>();
		batchService.updateRankingDatabase();
		response.put("result", true);
		
		List<Ranking> rankingList = batchService.selectRanking(new RankingFilter());
		response.put("rankingList", rankingList);
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/select/ranking", method = RequestMethod.POST)
	public List<Ranking> selectRanking(@RequestBody RankingFilter rankingFilter){
		return batchService.selectRanking(rankingFilter);
	}
}