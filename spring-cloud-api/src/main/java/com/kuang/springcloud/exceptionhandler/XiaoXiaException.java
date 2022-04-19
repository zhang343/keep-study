package com.kuang.springcloud.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoXiaException extends RuntimeException{
   private Integer code;
   private String msg;
}
