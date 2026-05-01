package com.snail.sys.vo.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 用户导出VO
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@ExcelIgnoreUnannotated
public class SysUserExportExcelVO {

    @ExcelProperty("用户账号")
    private String userCode;

    @ExcelProperty("用户名称")
    private String userName;

    @ExcelProperty("用户昵称")
    private String nickName;

    @ExcelProperty("手机号")
    private String phoneNo;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private String createTime;
}
