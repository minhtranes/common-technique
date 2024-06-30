package vn.ifa.study.oo.properties;

import lombok.Getter;
import lombok.Setter;
import vn.ifa.study.oo.model.StorageType;

@Getter
@Setter
abstract class AbstractObjectStorageProperties {
    private StorageType type;
    private String defaultBucket;
}
