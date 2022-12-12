package vn.ifa.study.oo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoredObject {
    private String bucket;
    private String key;
    private long size;
    private int version;
    /** Should contain less than 5MB size object */
    private byte[] smallContent;
}
