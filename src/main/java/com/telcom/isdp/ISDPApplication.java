package com.telcom.isdp;

import cn.stylefeng.roses.core.config.WebAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {WebAutoConfiguration.class})
@EnableScheduling
@MapperScan("com.telcom.isdp.modular.*.mapper")
public class ISDPApplication {

    private final static Logger logger = LoggerFactory.getLogger(ISDPApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ISDPApplication.class, args);
        logger.info(ISDPApplication.class.getSimpleName() + " is success!");
    }
}
