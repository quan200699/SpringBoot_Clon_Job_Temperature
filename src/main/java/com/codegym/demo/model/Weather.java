package com.codegym.demo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class Weather {
    String date;
    String tempMaxC;
    String tempMaxF;
    String tempMinC;
    String tempMinF;
    String windSpeedMiles;
    String windSpeedKmph;
    String windDirection;
    String weatherCode;
    String weatherIconUrl;
    String weatherDesc;
    String precipMM;
}
