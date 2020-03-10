package com.telcom.isdp.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)  //session过期时间  如果部署多机环境,需要打开注释
@ConditionalOnProperty(prefix = "isdp", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
