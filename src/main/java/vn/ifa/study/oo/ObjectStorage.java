package vn.ifa.study.oo;

import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ifa.study.oo.model.StoredObject;
import vn.ifa.study.oo.properties.S3CompatibleProperties;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public final class ObjectStorage {

    private final static Object locker = new Object();

    private static MinioClient minioInstance;

    private static S3CompatibleProperties props;

    @Autowired
    public ObjectStorage(final S3CompatibleProperties props) {

        ObjectStorage.props = props;

    }

    private static boolean bucketExists(final String bucket) {

        try {
            return minioClient().bucketExists(BucketExistsArgs.builder()
                                                              .bucket(bucket)
                                                              .build());
        } catch (Exception e) {
            log.error("Failed to check bucket {} existence", bucket, e);
        }

        return false;
    }

    public static boolean createBucket(final String bucket) {

        try {

            if (bucketExists(bucket)) {
                return true;
            }

            minioClient().makeBucket(MakeBucketArgs.builder()
                                                   .bucket(bucket)
                                                   .build());
            log.info("Bucket [{}] has been created", bucket);
            return true;
        } catch (Exception e) {
            log.error("Failed to create bucket {}", bucket, e);
        }

        return false;
    }

    /**
     * Get object from object storage
     * <p>
     *
     * @param object information of the reading object:
     *               <li>Default bucket will be used if
     *               <code>StoredObject.bucket</code> is <code>null</code>.
     *               <li><code>StoredObject.smallContent</code> is not required
     * @param os     if output stream is indicated, the object will be transfered to
     *               that stream else the <code>StoredObject.smallContent</code>
     *               will contains all byte array of the object
     * @return null if reading failed
     */
    public static StoredObject getObject(final StoredObject object, final OutputStream os) {

        GetObjectArgs arg = GetObjectArgs.builder()
                                         .bucket(object.getBucket() == null ? props.getDefaultBucket() : object.getBucket())
                                         .object(object.getKey())
                                         .build();

        try {

            try (InputStream is = minioClient().getObject(arg)) {

                if (os == null) {
                    byte[] bytes = IOUtils.toByteArray(is);
                    object.setSmallContent(bytes);
                    object.setSize(bytes.length);
                } else {
                    is.transferTo(os);
                }

            }

            return object;
        } catch (Exception e) {
            log.error("Failed to get object {} from remote storage at bucket {}", arg.object(), arg.bucket(), e);
        }

        return null;
    }

    public static List<StoredObject> listObjects(final String bucket, final String keyPrefix) {

        return listObjects(bucket, keyPrefix, false);
    }

    public static List<StoredObject> listObjects(final String bucket, final String keyPrefix, final boolean recursive) {

        if (!bucketExists(bucket)) {
            return new ArrayList<>();
        }

        ListObjectsArgs arg = ListObjectsArgs.builder()
                                             .bucket(bucket)
                                             .prefix(keyPrefix)
                                             .recursive(recursive)
                                             .includeVersions(false)
                                             .build();

        try {
            List<StoredObject> res = new ArrayList<>();
            Iterable<Result<Item>> objects = minioClient().listObjects(arg);
            Iterator<Result<Item>> it = objects.iterator();

            while (it.hasNext()) {
                Item item = it.next()
                              .get();
                res.add(StoredObject.builder()
                                    .bucket(bucket)
                                    .size(item.size())
                                    .key(item.objectName())
                                    .build());
            }

            return res;
        } catch (Exception e) {
            log.error("Failed to list objects prefix {} from remote storage at bucket {}",
                      arg.prefix(),
                      arg.bucket(),
                      e);
        }

        return null;
    }

    private static MinioClient minioClient() {

        synchronized (locker) {

            if (minioInstance == null) {
                minioInstance = MinioClient.builder()
                                           .endpoint(props.getEndpoint())
                                           .credentials(props.getClientKey(), props.getClientSecret())
                                           .build();
                log.info("Initialize minio client");
            }

            return minioInstance;
        }

    }

    public static StoredObject putObject(final StoredObject object, final InputStream is) {

        String bucket = object.getBucket() == null ? props.getDefaultBucket() : object.getBucket();

        if (!createBucket(bucket)) {
            return null;
        }

        PutObjectArgs arg = PutObjectArgs.builder()
                                         .bucket(bucket)
                                         .object(object.getKey())
                                         .stream(is, object.getSize(), -1)
                                         .build();

        try {
            minioClient().putObject(arg);
            return object;
        } catch (Exception e) {
            log.error("Failed to put object {} to remote storage at bucket {}", arg.object(), arg.bucket(), e);
        }

        return null;
    }
}
