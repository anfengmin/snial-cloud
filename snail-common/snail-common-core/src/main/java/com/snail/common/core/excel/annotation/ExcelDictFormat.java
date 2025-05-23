package com.snail.common.core.excel.annotation;

import cn.hutool.core.util.StrUtil;

import java.lang.annotation.*;

/**
 * 字典格式化
 *
 * @author Levi.
 * Created time 2025/5/23
 * @since 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelDictFormat {


    /**
     * 如果是字典类型，请设置字典的type值 (如: sys_user_sex)
     */
    String dictType() default "";

    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     */
    String readConverterExp() default "";

    /**
     * 分隔符，读取字符串组内容
     */
    String separator() default StrUtil.COMMA;
}
