package vn.ifa.study.oo;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class ObjectStorage {

    private final static Object locker = new Object();

    private static MinioClient minioInstance;

    private static ObjectStorageProperties props;

    public static boolean createBucket(final String bucket) {

        try {
            boolean found = minioClient().bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());

            if (found) {
                return true;
            }

            minioClient().makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
            log.info("Bucket [{}] has been created", bucket);
            return true;
        }
        catch (Exception e) {
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
            GetObjectResponse res = minioClient().getObject(arg);

            if (res == null) {
                return null;
            }

            if (os == null) {
                object.setSmallContent(res.readAllBytes());
                return object;
            }

            res.transferTo(os);
            return object;
        }
        catch (Exception e) {
            log.error("Failed to put object {} to remote storage at bucket {}", arg.object(), arg.bucket(), e);
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
        }
        catch (Exception e) {
            log.error("Failed to put object {} to remote storage at bucket {}", arg.object(), arg.bucket(), e);
        }

        return null;
    }

    @Autowired
    public ObjectStorage(final ObjectStorageProperties props) {

        ObjectStorage.props = props;

    }
}
