package vn.ifa.study.oo.service;

import vn.ifa.study.oo.model.StoredObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface OSClient {

    boolean createBucket(final String bucket);

    StoredObject getObject(final StoredObject object, final OutputStream os);

    StoredObject putObject(final StoredObject object, final InputStream is);

    public List<StoredObject> listObjects(final String bucket, final String keyPrefix);
}
