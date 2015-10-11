package com.oilMap.client.rest;

import com.oilMap.client.common.Constants;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Map;

/**
 * Created by SungGeun on 2015-10-01.
 */
@Rest(rootUrl = Constants.Host.CONTEXT_PATH, converters = {MappingJackson2HttpMessageConverter.class})
public interface AARestProtocol {

    @Post(Constants.Host.AUTH_SELECT_URL)
    Map<String, Object> authSelectUrl(Map<String, Object> request);

    @Post(Constants.Host.AUTH_IS_EXIST)
    Map<String, Object> authIsExistUrl(Map<String, Object> request);

    @Post(Constants.Host.AUTH_INSERT)
    Map<String, Object> authInsertUrl(Map<String, Object> request);


}
