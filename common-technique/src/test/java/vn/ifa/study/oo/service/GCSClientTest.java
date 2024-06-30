package vn.ifa.study.oo.service;

import com.github.dockerjava.api.command.CreateContainerCmd;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.properties.GCSProperties;
import vn.ifa.study.oo.properties.S3CompatibleProperties;
import vn.ifa.study.oo.service.impl.OSClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

@Slf4j
@Testcontainers
class GCSClientTest {
    private static final String CONTAINER_IMAGE = "fsouza/fake-gcs-server";
    private static final String BUCKET = "test-bucket";
    private static final String FILE_RESOURCE_PATH = "classpath:my_kids.jpg";
    private static final String OBJECT_KEY = "my_kids.jpg";
    private OSClient gcs;

//    @Container
//    GenericContainer container = new GenericContainer(DockerImageName.parse(CONTAINER_IMAGE))
//            .withExposedPorts(4443)
//            .withCreateContainerCmdModifier(new Consumer<CreateContainerCmd>() {
//                @Override
//                public void accept(final CreateContainerCmd createContainerCmd) {
//                    createContainerCmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http");
//                }
//            });

    @Autowired
    ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        log.info("Initialize the OSClient");
        gcs = OSClientBuilder.gcs(GCSProperties.builder()
                                               .projectId(System.getenv("GOOGLE_CLOUD_PROJECT_ID"))
                                               .build());
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void getObject() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        final File imageFile = loadFileFromResource(FILE_RESOURCE_PATH);

        log.info("Upload a test image into bucket {}", BUCKET);
        gcs.putObject(StoredObject.builder()
                                  .bucket(BUCKET)
                                  .key(OBJECT_KEY)
                                  .build(), Files.newInputStream(imageFile.toPath(), StandardOpenOption.READ));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        gcs.getObject(StoredObject.builder()
                                  .bucket(BUCKET)
                                  .key("my_kids.jpg")
                                  .build(), os);
        final int size = os.size();
        log.info("Get object from bucket {} with size {}", BUCKET, size);
        assert size == imageFile.length();
    }

    private File loadFileFromResource(String resourcePath) throws FileNotFoundException {
        return ResourceUtils.getFile(resourcePath);
    }

    @Test
    void putObject() throws IOException {
        final File imageFile = loadFileFromResource(FILE_RESOURCE_PATH);

        gcs.putObject(StoredObject.builder()
                                  .bucket(BUCKET)
                                  .key(OBJECT_KEY)
                                  .build(), Files.newInputStream(imageFile.toPath(), StandardOpenOption.READ));
    }

    @Test
    void deleteObject() {
        gcs.deleteObject(StoredObject.builder()
                                     .bucket(BUCKET)
                                     .key(OBJECT_KEY)
                                     .build());
    }
}