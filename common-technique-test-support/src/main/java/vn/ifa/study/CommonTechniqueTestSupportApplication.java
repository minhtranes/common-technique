package vn.ifa.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.service.OSClient;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SpringBootApplication
public class CommonTechniqueTestSupportApplication {

    @Autowired
    private OSClient minio;

    public static void main(String[] args) {
        SpringApplication.run(CommonTechniqueTestSupportApplication.class, args);
    }


    @PostConstruct
    void init() throws IOException {

        Path filePath = Path.of("./data", "kofile/019/00473.tif");
        File file = filePath.toFile();
        if (!file.exists()) {
            file.getParentFile()
                .mkdirs();
            file.createNewFile();
        }
        OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.WRITE);
        minio.getObject(StoredObject.builder()
                                    .bucket("intodigital")
                                    .key("kofile/019/00473.tif")
                                    .build(), os);

    }

}
