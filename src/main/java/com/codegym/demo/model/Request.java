package com.codegym.demo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "com.codegym.demo.model.Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
    String type;
    String query;
}
