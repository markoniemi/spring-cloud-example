package org.example.repository.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class HelloController {
    @Value("${user.role}")
    private String role;
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/hello/{username}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(@PathVariable("username") String username) {
        return String.format("Hello %s, %s", username, role);
    }

    @RequestMapping(value = "/env", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String env() {
        return environment.toString();
    }
}
