package vn.ifa.study.oo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ifa.study.oo.StoredObject;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class HTTPObjectStorage implements ObjectStorageService{
    @Override
    public StoredObject putObject(final StoredObject object, final InputStream is) {
        return null;
    }

    @Override
    public StoredObject getObject(final StoredObject object, final OutputStream os) {
        return null;
    }
}
