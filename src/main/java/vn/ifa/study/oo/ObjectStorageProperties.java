package vn.ifa.study.oo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ObjectStorageProperties {
    private String endpoint;
    private String clientKey;
    private String clientSecret;
    private String defaultBucket;
}
