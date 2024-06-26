package vn.ifa.study.oo.service.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.rpc.context.AttributeContext;
import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.properties.GCSProperties;
import vn.ifa.study.oo.service.OSClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class GCSObjectService implements OSClient {

    private final GCSProperties props;

    GCSObjectService(GCSProperties props) {
        this.props = props;
    }

    @Override
    public boolean createBucket(final String bucket) {
        return false;
    }

    @Override
    public StoredObject getObject(final StoredObject object, final OutputStream os) {

        log.info("Get object [{}] from GCS", object.getKey());

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

        try {
            log.info("Put object [{}] into GCS", object.getKey());
            final Storage storage = StorageOptions.newBuilder()
                                                  .setProjectId(props.getProjectId())
                                                  .build()
                                                  .getService();
            final BlobId blobId = BlobId.of(object.getBucket(), object.getKey());
            final BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                                              .build();
            storage.createFrom(blobInfo, is);
            return object;
        } catch (IOException e) {
            log.error("Failed to upload object {} to GCS", object.getKey(), e);
        }

        return null;
    }

    @Override
    public boolean deleteObject(final StoredObject object) {
        try {
            log.info("Delete object [{}] from GCS", object.getKey());
            Storage storage = StorageOptions.newBuilder()
                                            .setProjectId(props.getProjectId())
                                            .build()
                                            .getService();
            BlobId blobId = BlobId.of(object.getBucket(), object.getKey());

            storage.delete(blobId);
            log.info("Delete object {} successfully", object.getKey());
            return true;
        } catch (Exception e) {
            log.error("Failed to delete object {} from GCS", object.getKey(), e);
        }
        return false;
    }


    @Override
    public List<StoredObject> listObjects(final String bucket, final String keyPrefix) {

        Storage storage = StorageOptions.newBuilder()
                                        .setProjectId(props.getProjectId())
                                        .build()
                                        .getService();

        log.info("List the object with prefix [{}]", keyPrefix);

        Page<Blob> blobs = storage.list(bucket, Storage.BlobListOption.prefix(keyPrefix));

        return StreamSupport.stream(blobs.iterateAll()
                                         .spliterator(), false)
                            .map(blob -> StoredObject.builder()
                                                     .key(blob.getName())
                                                     .size(blob.getSize())
                                                     .build())
                            .collect(Collectors.toList());
    }
}
