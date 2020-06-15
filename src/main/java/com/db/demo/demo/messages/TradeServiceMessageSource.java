package com.db.demo.demo.messages;

public interface TradeServiceMessageSource {
    /**
     * Gets the messages from local message file
     * @param key
     * @param defaultValue
     * @return
     */
    String getMessage(String key, String defaultValue);

}
