package vn.ifa.study.oo.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class S3CompatibleProperties extends AbstractObjectStorageProperties {
    private String endpoint;
    private String clientKey;
    private String clientSecret;
}
