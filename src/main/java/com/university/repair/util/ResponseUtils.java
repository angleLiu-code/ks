package com.university.repair.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应工具类
 */
public class ResponseUtils {

    /**
     * 成功响应
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("message", "请求成功");
        response.put("data", data);
        return response;
    }

    /**
     * 成功响应 - 带消息
     */
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    /**
     * 错误响应
     */
    public static Map<String, Object> error(String code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", null);
        return response;
    }

    /**
     * 错误响应
     */
    public static Map<String, Object> error(String message) {
        return error("500", message);
    }

    /**
     * 分页响应
     */
    public static Map<String, Object> pageSuccess(long total, long pageSize, long pageNum, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("message", "请求成功");
        response.put("total", total);
        response.put("pageSize", pageSize);
        response.put("pageNum", pageNum);
        response.put("data", data);
        return response;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResult<T> {
        private long total;
        private long pageSize;
        private long pageNum;
        private java.util.List<T> data;
    }
}
