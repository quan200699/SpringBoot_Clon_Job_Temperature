package com.codegym.demo.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(namespace = "com.codegym.demo.model.Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentCondition {
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
