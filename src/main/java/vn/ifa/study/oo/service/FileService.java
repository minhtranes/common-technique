package vn.ifa.study.oo.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "fileService")
public interface FileService {
}
