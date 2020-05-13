package com.codegym.demo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "com.codegym.demo.model.Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentCondition {
    String observationTime;
    String temp_C;
    String weatherCode;
    String weatherIconUrl;
    String weatherDesc;
    String windSpeedMiles;
    String windSpeedKmph;
    String windDirDegree;
    String windDir16Point;
    String precipMM;
    String humidity;
    String visibility;
    String pressure;
    String cloudCover;
}
