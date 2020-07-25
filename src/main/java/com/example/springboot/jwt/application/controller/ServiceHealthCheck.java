package com.example.springboot.jwt.application.controller;

import com.example.springboot.jwt.application.beans.Healthcheck;
import com.example.springboot.jwt.application.common.Constants;
import com.example.springboot.jwt.application.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Prashant Patel
 * Date: 1/30/2019
 **/
@RestController
@RequestMapping("/health")
public class ServiceHealthCheck {

    @Autowired
    private Environment env;

    @Autowired
    private BuildProperties buildProperties;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResponse<Healthcheck> getStatus() throws Exception {
        Healthcheck healthCheck = new Healthcheck();

        Map<String, String> metadata = new HashMap<>();
        metadata.put(Constants.ENV_KEY, env.getProperty(Constants.ENV_KEY));
        metadata.put(Constants.BRANCH_KEY, buildProperties.get(Constants.BRANCH_KEY));
        metadata.put(Constants.VERSION_KEY, buildProperties.getVersion());
        metadata.put(Constants.TIME_KEY, buildProperties.getTime().toString());
        healthCheck.setMetadata(metadata);

        return new RestResponse<>(healthCheck);
    }

}