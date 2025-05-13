package com.snial.common.core.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
