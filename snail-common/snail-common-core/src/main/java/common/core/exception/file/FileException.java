package common.core.exception.file;

import common.core.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
