package com.sh.utils.storage;

import com.sh.vo.FileVO;

import java.util.List;

/**
 * 储存服务
 */
public interface StoreService {

    void deleteFile(List<String> files);

    FileVO saveFile(FileVO fileVO);

    String getStoreName();
}
