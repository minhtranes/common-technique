package vn.ifa.study.oo.service.impl;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.properties.GCSProperties;
import vn.ifa.study.oo.service.OSClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Slf4j
public class GCSObjectService implements OSClient {

    private final GCSProperties props;

    GCSObjectService(GCSProperties props) {
        this.props = props;
    }

    @Override
    public StoredObject getObject(final StoredObject object, final OutputStream os) {

        Storage storage = StorageOptions.newBuilder()
                                        .setProjectId(props.getProjectId())
                                        .build()
                                        .getService();
        return Optional.of(storage.readAllBytes(object.getBucket(), object.getKey()))
                       .map(bytes -> {
                           object.setSmallContent(bytes);
                           return object;
                       })
                       .orElse(object);
    }

    @Override
    public StoredObject putObject(final StoredObject object, final InputStream is) {
        return null;
    }

    @Override
    public List<StoredObject> listObjects(final String bucket, final String keyPrefix) {
        return List.of();
    }

    public static InputStream downloadObjectIntoMemory(
            String projectId, String bucket, String objectKey) {

        Storage storage = StorageOptions.newBuilder()
                                        .setProjectId(projectId)
                                        .build()
                                        .getService();
        return new ByteArrayInputStream(storage.readAllBytes(bucket, objectKey));
    }
}
