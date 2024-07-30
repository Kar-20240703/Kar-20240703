package com.kar20240703.be.base.web.model.interfaces;

import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface IBaseFileUploadType {

    int getCode(); // 建议从：10001（包含）开始

    String getFolderName();

    Set<String> getAcceptFileTypeSet();

    long getMaxFileSize();

    boolean isPublicFlag();

    String checkFileType(MultipartFile file);

    void checkFileSize(MultipartFile file);

}
