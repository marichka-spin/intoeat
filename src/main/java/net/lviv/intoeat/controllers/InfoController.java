package net.lviv.intoeat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@PropertySource("classpath:conf/configuration.properties")
public class InfoController {

    @Autowired
    private Environment environment;

    @RequestMapping("/profiles")
    @ResponseBody
    public String[] needsProfile() {
        return  environment.getActiveProfiles();
    }

    @RequestMapping("/version")
    @ResponseBody
    public Map<String, String> version() {
        Map<String, String> version = new HashMap<String, String>();
        version.put("version", environment.getProperty("application.version", "1.0-SNAPSHOT"));
        return  version;
    }

}
