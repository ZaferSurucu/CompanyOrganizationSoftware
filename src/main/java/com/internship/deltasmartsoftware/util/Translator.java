package com.internship.deltasmartsoftware.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {
    private static MessageSource messageSource;

    @Autowired
    Translator (MessageSource messageSource){
        Translator.messageSource = messageSource;

    }

    public static String toLocale(String msgCode){
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(msgCode, null, locale);
        } catch (Exception e) {
            return msgCode;
        }
    }
}
