package com.eshop.client.strategy;

import com.eshop.client.enums.NetworkType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NetworkStrategyFactory implements ApplicationContextAware {
    private Map<Class<? extends NetworkStrategy>, NetworkStrategy> strategyMap;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, NetworkStrategy> beans = applicationContext.getBeansOfType(NetworkStrategy.class);
        strategyMap = new HashMap<>();
        for (NetworkStrategy strategy : beans.values()) {
            strategyMap.put(strategy.getClass(), strategy);
        }
    }

    public NetworkStrategy get(NetworkType networkType) {
        NetworkStrategy strategy = strategyMap.get(networkType.getImplementationClass());
        if (strategy == null) {
            throw new RuntimeException("No implementation found for " + networkType);
        }
        return strategy;
    }
}
