package com.snail.common.log.aspect;


import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.satoken.vo.LoginUser;
import com.snail.common.core.enums.BusinessStatus;
import com.snail.common.core.utils.ServletUtils;
import com.snail.common.core.utils.SpringUtils;
import com.snail.common.core.utils.ip.AddressUtils;
import com.snail.common.log.annotation.Log;
import com.snail.common.log.event.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 操作日志记录处理
 *
 * @author Levi.
 * Created time 2025/5/18
 * @since 1.0
 */
@Slf4j
@Aspect
@AutoConfiguration
public class LogAspect {


    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * handleLog
     *
     * @param joinPoint     joinPoint
     * @param controllerLog controllerLog
     * @param e             e
     * @param jsonResult    jsonResult
     * @since 1.0
     */
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            OperateLogEvent operLog = new OperateLogEvent();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = ServletUtils.getClientIP();
            operLog.setOperateIp(ip);
            operLog.setOperateLocation(AddressUtils.getRealAddressByIP(ip));
            operLog.setOperateUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            LoginUser loginUser = LoginUtils.getLoginUser();
            operLog.setOperatorName(loginUser.getUserName());
            operLog.setDeptName(loginUser.getDeptName());

            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 发布事件保存数据库
            SpringUtils.context().publishEvent(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log        日志
     * @param operateLog 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperateLogEvent operateLog, Object jsonResult) {
        // 设置action动作
        operateLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operateLog.setTitle(log.title());
        // 设置操作人类别
        operateLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operateLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
            operateLog.setJsonResult(StringUtils.substring(JSONUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operateLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, OperateLogEvent operateLog, String[] excludeParamNames) {
        Map<String, String> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operateLog.getRequestMethod();
        boolean flag = MapUtil.isEmpty(paramsMap) && HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod);
        if (flag) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operateLog.setOperateParam(StringUtils.substring(params, 0, 2000));
        } else {
            MapUtil.removeAny(paramsMap, EXCLUDE_PROPERTIES);
            MapUtil.removeAny(paramsMap, excludeParamNames);
            operateLog.setOperateParam(StringUtils.substring(JSONUtil.toJsonStr(paramsMap), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringJoiner params = new StringJoiner(" ");
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString();
        }
        for (Object o : paramsArray) {
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                String str = JSONUtil.toJsonStr(o);
                Dict dict = JSONUtil.parseObj(str).toBean(Dict.class);
                if (MapUtil.isNotEmpty(dict)) {
                    MapUtil.removeAny(dict, EXCLUDE_PROPERTIES);
                    MapUtil.removeAny(dict, excludeParamNames);
                    str = JSONUtil.toJsonStr(dict);
                }
                params.add(str);
            }
        }
        return params.toString();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                return value instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
