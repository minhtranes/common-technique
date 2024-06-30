package vn.ifa.study.oo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.MissingRequiredPropertiesException;
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
        log.info("Initialize the CGS client for project [{}]", props.getProjectId());

        log.info("Checking google default credentials...");
        final String googleApplicationCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (googleApplicationCredentials == null || googleApplicationCredentials.isEmpty()) {
            log.error("Missing GOOGLE_APPLICATION_CREDENTIALS environment variable! GCS client use this to locate the credential file");
            throw new IllegalStateException("Missing GOOGLE_APPLICATION_CREDENTIALS environment variable! GCS client use this to locate the credential file");
        }
        return new GCSObjectService(props);
    }
}
