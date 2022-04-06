package com.kuang.springcloud.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author XiaoXia
 * @date 2021/12/28 20:16
 * token生成器
 */
public class JwtUtils {
    //token过期时间,这里过期时间是一天天
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    //秘钥，这里的密钥是一个UUID
    public static final String APP_SECRET = "187ade78070f496386f1f778f018f075";

    //根据id（这个id一般是用户id或者管理员id）
    public static String getJwtToken(String id) {
        String jwtToken = Jwts.builder()
                //设置头部头部信息
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                //设置载荷
                .setIssuer("XiaoXia")
                .setSubject("kuang-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //下面我们设置自定义信息
                .claim("id", id)
                //设置签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return jwtToken;
    }

    //讲用户信息转变为token
    public static String getJwtToken(String id , String role){
        String jwtToken = Jwts.builder()
                //设置头部头部信息
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                //设置载荷
                .setIssuer("XiaoXia")
                .setSubject("kuang-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //下面我们设置自定义信息
                .claim("id", id)
                .claim("role" , role)
                //设置签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return jwtToken;
    }


    //核查token是否有效
    public static boolean checkToken(String jwtToken) {
        boolean flag = true;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    //核查token是否有效
    public static boolean checkToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        return checkToken(jwtToken);
    }


    //取出id，如果token无效则返回null
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        boolean flag = checkToken(request);
        String id = null;
        if(flag){
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(request.getHeader("token"));
            Claims claims = claimsJws.getBody();
            id = (String) claims.get("id");
        }
        return id;
    }

    //取出id，如果token无效则返回null
    public static String getMemberIdByJwtToken(String token) {
        boolean flag = checkToken(token);
        String id = null;
        if(flag){
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            id = (String) claims.get("id");
        }
        return id;
    }

    //取出role，如果token无效则返回null
    public static String getMemberRoleByJwtToken(String token) {
        boolean flag = checkToken(token);
        String role = null;
        if(flag){
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            role = (String) claims.get("role");
        }
        return role;
    }

}
