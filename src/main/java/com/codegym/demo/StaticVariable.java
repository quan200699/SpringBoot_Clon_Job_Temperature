package com.codegym.demo;

public class StaticVariable {
    public static final String API_URL_HN = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=81fcb678531041aa9b365912201305&q=ha+noi&num_of_days=1&format=xml";
    public static final String API_URL_DN = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=81fcb678531041aa9b365912201305&q=da+nang&num_of_days=1&format=xml";
    public static final String API_URL_HCM = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=81fcb678531041aa9b365912201305&q=ho+chi+minhi&num_of_days=1&format=xml";
    public static final String URL_CRAWL_HN = "https://www.worldweatheronline.com/ha-noi-weather/vn.aspx";
    public static final String PATTERN_TEMPERATURE = "class=\"display-4\">(.*?) &deg;c</span>";
    public static final String PATTERN_CITY_HN = "href=\"https://www.worldweatheronline.com/ha-noi-weather/vn.aspx\">(.*?)</a>";
    public static final String URL_CRAWL_DN = "https://www.worldweatheronline.com/da-nang-weather/vn.aspx";
    public static final String PATTERN_CITY_DN = "href=\"https://www.worldweatheronline.com/da-nang-weather/vn.aspx\">(.*?)</a>";
    public static final String URL_CRAWL_HCM = "https://www.worldweatheronline.com/ho-chi-minh-city-weather/vn.aspx";
    public static final String PATTERN_CITY_HCM = "href=\"https://www.worldweatheronline.com/ho-chi-minh-city-weather/vn.aspx\">(.*?)</a>";
}
