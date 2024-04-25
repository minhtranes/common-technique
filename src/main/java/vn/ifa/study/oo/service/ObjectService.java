package vn.ifa.study.oo.service;

import vn.ifa.study.oo.model.StoredObject;

import java.io.InputStream;
import java.io.OutputStream;

public interface ObjectService {
    
    StoredObject getObject(final StoredObject object, final OutputStream os);

    StoredObject putObject(final StoredObject object, final InputStream is);
}
