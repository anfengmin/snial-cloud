package com.snail.auth.service;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
public interface SysLoginService {

    /**
     * login
     *
     * @param userCode userCode
     * @param password password
     * @return java.lang.String
     * @since 1.0
     */
    String login(String userCode, String password);

    /**
     * logout
     *
     * @since 1.0
     */
    void logout();
}
