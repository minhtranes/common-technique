package vn.ifa.study.oo.service.impl;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.oo.properties.GCSProperties;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.OSClient;

@Slf4j
public final class OSClientBuilder {

    public static OSClient s3(S3CompatibleProperties props){
        log.info("Initialize the S3 compatible client");
        return new S3CompatibleObjectService(props);
    }

    public static OSClient gcs(GCSProperties props){
        log.info("Initialize the CGS client");
        return new GCSObjectService(props);
    }
}
