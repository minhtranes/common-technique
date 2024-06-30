package vn.ifa.study.oo.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class S3CompatibleProperties extends AbstractObjectStorageProperties {
    private String endpoint;
    private String clientKey;
    private String clientSecret;
}
