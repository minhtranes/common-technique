package vn.ifa.study.oo.service;

import vn.ifa.study.oo.StoredObject;

import java.io.InputStream;
import java.io.OutputStream;

public interface ObjectStorageService {

    StoredObject putObject(final StoredObject object, final InputStream is);

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
    StoredObject getObject(final StoredObject object, final OutputStream os);
}
