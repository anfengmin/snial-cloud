package com.snail.sys.api.dto;

import lombok.Data;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
@Data
public class UserRoleDeptDTO {

    private Long userId;
    private String userCode;
    private String userName;

    private Long deptId;
    private String deptName;

    private Long roleId;
    private String roleKey;
    private String roleName;
}
