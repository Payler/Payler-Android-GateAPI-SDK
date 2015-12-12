package com.payler.paylergateapi.lib.model.request;

/**
 * Запрос получения информации о шаблоне рекуррентных платежей. Рекомендуется 
 * использовать для получения полной информации о зарегистрированном шаблоне 
 * рекуррентных платежей. Если в запросе не указать идентификатор шаблона рекуррентных 
 * платежей, то в ответе придет информация обо всех зарегистрированных на Продавца 
 * шаблонах. 
 */
public class GetTemplateRequest extends GateRequest {

    private String key;

    private String recurrentTemplateId;

    public GetTemplateRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public GetTemplateRequest setRecurrentTemplateId(String recurrentTemplateId) {
        this.recurrentTemplateId = recurrentTemplateId;
        return this;
    }
}
