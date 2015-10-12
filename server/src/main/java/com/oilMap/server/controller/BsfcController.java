package com.oilMap.server.controller;

import com.oilMap.server.bsfc.Bsfc;
import com.oilMap.server.bsfc.BsfcSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by SungGeun on 2015-10-12.
 */
@Controller
@RequestMapping("/bsfc")
public class BsfcController {

    private static Logger logger = LoggerFactory.getLogger(BsfcController.class);

    @Autowired
    private BsfcSerivce bsfcSerivce;

    @ResponseBody
    @RequestMapping(value="/select", method = RequestMethod.POST)
    public Bsfc select(@RequestBody Bsfc bsfc){
        logger.debug(bsfc.toString());
        if(bsfc.getRpm()!= null && bsfc.getEngineLoad() != null){
            Integer rpm = bsfc.getRpm();
            Integer engineLoad = bsfc.getEngineLoad();
            if(rpm >= 1100 && rpm <= 3500 && engineLoad >=25 && engineLoad <= 100){
                return bsfcSerivce.select(bsfc);
            }
        }

        return new Bsfc();
    }

}
