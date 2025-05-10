package com.snail.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@RestController
@RequestMapping("/hello")
public class Hello {

    @GetMapping("/d")
    public String hello() {
        return "hello";
    }
}
