package com.codegym.demo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@lombok.Data
public class Data {
    private Request request;
    private CurrentCondition current_condition;
    private Weather weather;

    @XmlRootElement(namespace = "com.codegym.demo.model.Data")
    @XmlAccessorType(XmlAccessType.FIELD)
    @lombok.Data
    public static class Request {
        private String type;
        private String query;
    }

    @XmlRootElement(namespace = "com.codegym.demo.model.Data")
    @XmlAccessorType(XmlAccessType.FIELD)
    @lombok.Data
    public static class CurrentCondition {
        private String observation_time;
        private String temp_C;
        private String weatherCode;
        private String weatherIconUrl;
        private String weatherDesc;
        private String windspeedMiles;
        private String windspeedKmph;
        private String winddirDegree;
        private String winddir16Point;
        private String precipMM;
        private String humidity;
        private String visibility;
        private String pressure;
        private String cloudcover;
    }

    @XmlRootElement(namespace = "com.codegym.demo.model.Data")
    @XmlAccessorType(XmlAccessType.FIELD)
    @lombok.Data
    public static class Weather {
        private String date;
        private String tempMaxC;
        private String tempMaxF;
        private String tempMinC;
        private String tempMinF;
        private String windspeedMiles;
        private String windspeedKmph;
        private String winddirection;
        private String weatherCode;
        private String weatherIconUrl;
        private String weatherDesc;
        private String precipMM;
    }
}
