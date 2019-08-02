package com.example.oauth2.webflux.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义前置拦截器
 *
 * Created by mike on 2019-08-02
 */
@Component
public class PreGatewayFilterFactory extends AbstractGatewayFilterFactory<PreGatewayFilterFactory.Config> {

    private static final String NAME = "name";
    private static final String NUM = "num";

    public PreGatewayFilterFactory() {
        super(Config.class);
    }

    /**
     * 需要配置此处，才能从配置文件注入Config属性字段
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(NAME, NUM);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.err.println(config.getName() + "," + config.getNum());
            return chain.filter(exchange);
        };
    }

    public static class Config {
        private String name;

        private int num;

        public String getName() {
            return name;
        }

        public Config setName(String name) {
            this.name = name;
            return this;
        }

        public int getNum() {
            return num;
        }

        public Config setNum(int num) {
            this.num = num;
            return this;
        }
    }
}
