package com.kuang.springcloud.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XiaoXia
 * @date 2021/12/27 16:55
 * 网站特定异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoXiaException extends RuntimeException{
   private Integer code;
   private String msg;
}
