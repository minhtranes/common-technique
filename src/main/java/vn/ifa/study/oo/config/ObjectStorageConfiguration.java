package vn.ifa.study.oo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ifa.study.oo.properties.S3CompatibleProperties;

@Configuration
public class ObjectStorageConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "object-storage")
    S3CompatibleProperties objectStorageProperties() {

        return new S3CompatibleProperties();
    }
}
