package com.snail.sys.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.snail.common.core.excel.annotation.ExcelDictFormat;
import com.snail.common.core.excel.convert.ExcelDictConvert;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户导入DTO
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@ExcelIgnoreUnannotated
public class SysUserImportExcelDTO {

    @NotBlank(message = "用户账号不能为空")
    @ExcelProperty("用户账号")
    private String userCode;

    @NotBlank(message = "用户名称不能为空")
    @ExcelProperty("用户名称")
    private String userName;

    @ExcelProperty("用户昵称")
    private String nickName;

    @ExcelProperty("手机号")
    private String phoneNo;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    private String sex;

    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private Integer status;

    @ExcelProperty("备注")
    private String remark;
}
