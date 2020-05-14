package com.codegym.demo.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(namespace = "com.codegym.demo.model.Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
    private String type;
    private String query;
}
