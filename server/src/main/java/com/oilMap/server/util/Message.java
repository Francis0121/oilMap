package com.oilMap.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

import java.util.Locale;

/**
 * Created by Francis on 2015-03-24.
 */
@Controller
public class Message {

    @Qualifier(value = "messageSource")
    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getValue(String key)
    {
        Locale locale = LocaleContextHolder.getLocale();
        messageSource.setDefaultEncoding("utf-8");
        return messageSource.getMessage(key, new Object[0], locale);
    }

}
