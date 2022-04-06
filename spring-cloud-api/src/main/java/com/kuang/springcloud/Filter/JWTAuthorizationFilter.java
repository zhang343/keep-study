package com.kuang.springcloud.Filter;


import com.kuang.springcloud.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * 用户鉴权过滤器
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 权限认证
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("token");
        //没有token信息直接放行
        if(token == null){
            chain.doFilter(request , response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        super.doFilterInternal(request, response, chain);
    }

    // 这里从token中获取用户信息并查询出用户权限
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String id = JwtUtils.getMemberIdByJwtToken(token);
        String role = JwtUtils.getMemberRoleByJwtToken(token);
        if(role != null){
            //第三个是权限
            return new UsernamePasswordAuthenticationToken(id, null, Collections.singleton(new SimpleGrantedAuthority(role)));
        }
        return null;
    }
}
