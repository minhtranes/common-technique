package vn.ifa.study.oo.service;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Testcontainers
class MinioClientTest {
    private static final String CONTAINER_IMAGE = "minio/minio:RELEASE.2023-09-04T19-57-37Z";
    private static final String BUCKET = "test-bucket";
    private OSClient minio;

    @Container
    MinIOContainer container = new MinIOContainer(CONTAINER_IMAGE)
            .withUserName("minio")
            .withPassword("minioadmin");

    @Autowired
    ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        log.info("Initialize the OSClient");
        minio = OSClientBuilder.s3(S3CompatibleProperties.builder()
                                                         .endpoint(container.getS3URL())
                                                         .clientKey(container.getUserName())
                                                         .clientSecret(container.getPassword())
                                                         .build());
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void getObject() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(container.getS3URL())
                .credentials(container.getUserName(), container.getPassword())
                .build();
        log.info("Create the default bucket {}", BUCKET);
        minioClient.makeBucket(MakeBucketArgs.builder()
                                             .bucket(BUCKET)
                                             .build());

        final File imageFile = loadFileFromResource("classpath:my_kids.jpg");

        log.info("Upload a test image into bucket {}", BUCKET);
        minioClient.putObject(PutObjectArgs.builder()
                                           .bucket(BUCKET)
                                           .object("my_kids.jpg")
                                           .stream(Files.newInputStream(imageFile.toPath(), StandardOpenOption.READ), imageFile.length(), -1)
                                           .build());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        minio.getObject(StoredObject.builder()
                                    .bucket(BUCKET)
                                    .key("my_kids.jpg")
                                    .build(), os);
        log.info("Get object from bucket {} with size {}", BUCKET, os.size());
    }

    private File loadFileFromResource(String resourcePath) throws FileNotFoundException {
        return ResourceUtils.getFile(resourcePath);
    }

//    @Test
//    void putObject() {
//    }
//
//    @Test
//    void deleteObject() {
//    }
}