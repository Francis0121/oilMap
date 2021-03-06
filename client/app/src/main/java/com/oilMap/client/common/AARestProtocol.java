package com.oilMap.client.common;

import com.oilMap.client.ranking.RankingFilter;
import com.oilMap.client.ranking.RankingResponse;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Map;

/**
 * Created by SungGeun on 2015-10-01.
 */
@Rest(rootUrl = Constants.Host.CONTEXT_PATH, converters = {MappingJackson2HttpMessageConverter.class})
public interface AARestProtocol {

    // ~ Auth

    @Post(Constants.Host.AUTH_SELECT_URL)
    Map<String, Object> authSelectUrl(Map<String, Object> request);

    @Post(Constants.Host.AUTH_IS_EXIST)
    Map<String, Object> authIsExistUrl(Map<String, Object> request);

    @Post(Constants.Host.AUTH_INSERT)
    Map<String, Object> authInsertUrl(Map<String, Object> request);

    // ~ Ranking

    @Post(Constants.Host.RANKING_SELECT)
    RankingResponse rankingSelectUrl(RankingFilter rankingFilter);

    // ~ Fuel

    @Post(Constants.Host.FUEL_BILL_INSERT)
    Map<String, Object> fuelBillInsert(Map<String, Object> request);

    @Post(Constants.Host.FUEL_BILL_SELECT)
    Map<String, Object> fuelBillSelectUrl(Map<String, Object> request);

    // ~ Bsfc

    @Post(Constants.Host.BSFC_SELECT)
    Bsfc bsfcSelectUrl(Bsfc bsfc);

}
