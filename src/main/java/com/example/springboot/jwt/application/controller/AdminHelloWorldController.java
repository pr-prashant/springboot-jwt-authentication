package com.example.springboot.jwt.application.controller;

import com.example.springboot.jwt.application.common.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Prashant Patel
 * Date: 7/21/2020
 **/
@RestController
@RequestMapping(value = ControllerConstants.ADMIN_ROOT)
public class AdminHelloWorldController {

    @GetMapping(value = ControllerConstants.HELLO_WORLD)
    public RestResponse<String> helloWorld() {
        return new RestResponse<>("Hello World!");
    }
}
