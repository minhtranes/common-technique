package vn.ifa.study.oo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class ObjectStorageConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "object-storage")
    ObjectStorageProperties objectStorageProperties() {

        return new ObjectStorageProperties();
    }
}
