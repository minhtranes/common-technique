## Overview

A library to expose convenient API to work with lot of common technology such as ObjectStorage, File, Encryption,...

:::mermaid
classDiagram
   Creature <|-- Superman
:::

# Object Storage
The target of this implementation is providing the convenient interface that work with common object storage such as AWS S3, GCS, Azure Blob Storage.

During boot up, a single bean of this implementation.

# How to use?
## S3 compatible client
1. Configuration 

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

Then we can use the bean `s3` in any where of your code

## GCS client