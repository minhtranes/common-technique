# Overview

A library to expose convenient API to work with lot of common technology such as ObjectStorage, File, Encryption,...

# Object Storage Client
The target of this implementation is providing the convenient interface that work with common object storage such as AWS S3, GCS, Azure Blob Storage.

During boot up, a single bean of this implementation.

## How to use?
### Minio Object Storage
1. Configuration

Go to `application.yml` and add following properties
```yaml
minio:
    endpoint: https://minio:9000
    clientKey: minio
    clientSecret: minio!123
```
2. Initialize the client bean

Configure the minio client bean

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.OSClient;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

@Configuration
class OSConfiguration {

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
```

Then we can use the bean `minio` in anywhere of your code by using `@Autowired` to inject the client bean

### AWS S3
1. Configuration

Go to `application.yml` and add following properties
```yaml
s3:
    endpoint: https://minio:9000
    clientKey: minio
    clientSecret: minio!123
```
2. Initialize the client bean

Configure the s3 client bean

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.OSClient;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

@Configuration
class OSConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "s3")
    S3CompatibleProperties properties() {
        return new S3CompatibleProperties();
    }

    @Bean
    OSClient s3() {
        return OSClientBuilder.s3(properties());
    }
}
```

Then we can use the bean `s3` in anywhere of your code by using `@Autowired` to inject the client bean

### Google Cloud Storage
1. Configuration

Go to `application.yml` and add following configuration
```yaml
gcs:
    projectId: project-id-123
```

Add environment variable `GOOGLE_APPLICATION_CREDENTIALS` to point to the the key file generated from Google Cloud Console IAM
```yaml
export GOOGLE_APPLICATION_CREDENTIALS=/opt/app/config/splendid-sonar-174914-c483e3e6fb4f.json
```

2. Initialize the client bean

Configure the gcs client bean

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ifa.study.oo.properties.GCSProperties;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.OSClient;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

@Configuration
class OSConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "gcs")
    GCSProperties properties() {
        return new GCSProperties();
    }

    @Bean
    OSClient gcs() {
        return OSClientBuilder.gcs(properties());
    }
}
```

Then we can use the bean `gcs` in anywhere of your code by using `@Autowired` to inject the client bean
