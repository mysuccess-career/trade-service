package com.db.demo.demo.messages;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * @author Savitha
 */

@Configuration
public class TradeServiceMessageSourceImpl implements TradeServiceMessageSource {

  @Autowired
  private MessageSource messageSource;

  @Override
  public String getMessage(String key, String defaultValue) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(key, null, defaultValue, locale);
  }

}
