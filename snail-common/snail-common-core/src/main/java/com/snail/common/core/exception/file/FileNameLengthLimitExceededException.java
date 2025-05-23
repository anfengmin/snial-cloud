package com.snail.common.core.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
