package com.codegym.demo.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(namespace = "com.codegym.demo.model.Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Weather {
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
