package vn.ifa.study.oo.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GCSProperties extends AbstractObjectStorageProperties{
    private String projectId;
}
