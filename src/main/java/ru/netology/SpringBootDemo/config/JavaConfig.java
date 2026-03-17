package ru.netology.SpringBootDemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.SpringBootDemo.system.DevProfile;
import ru.netology.SpringBootDemo.system.ProductionProfile;
import ru.netology.SpringBootDemo.system.SystemProfile;

@Configuration
public class JavaConfig {
    @Bean
    @ConditionalOnProperty(name = "netology.profile.dev", havingValue = "true", matchIfMissing = false)
//@ConditionalOnProperty - это аннотация, которая проверяет значение свойства в конфигурации
//Если netology.profile.dev=true - создается бин DevProfile
//Если netology.profile.dev=false или свойство не указано - создается бин ProductionProfile
//Spring автоматически внедряет нужный бин в контроллер
    public SystemProfile devProfile() {
        return new DevProfile();
    }

    @Bean
    @ConditionalOnProperty(name = "netology.profile.dev", havingValue = "false", matchIfMissing = true)
    public SystemProfile prodProfile() {
        return new ProductionProfile();
    }
}
