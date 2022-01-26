package com.vnakhoa.crudtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;

@SpringBootApplication
public class CrudtestApplication {
    public static String URL_IMAGE = "";
    public static void main(String[] args) {
        ClassLoader classLoader = CrudtestApplication.class.getClassLoader();
        URL url = classLoader.getResource("static");
        URL_IMAGE = url.getPath();
        SpringApplication.run(CrudtestApplication.class, args);
    }

}
