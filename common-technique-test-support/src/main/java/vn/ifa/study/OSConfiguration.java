package vn.ifa.study;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.OSClient;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

@Configuration
public class OSConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "minio")
    S3CompatibleProperties properties() {
        return new S3CompatibleProperties();
    }

    @Bean
    OSClient minio() {
        return OSClientBuilder.s3(properties());
    }
}
