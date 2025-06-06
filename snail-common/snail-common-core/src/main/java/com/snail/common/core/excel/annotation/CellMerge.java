package com.snail.common.core.excel.annotation;

import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项) 需搭配 CellMergeStrategy 策略使用
 *
 * @author Levi.
 * Created time 2025/5/23
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

    /**
     * col index
     */
    int index() default -1;
}
