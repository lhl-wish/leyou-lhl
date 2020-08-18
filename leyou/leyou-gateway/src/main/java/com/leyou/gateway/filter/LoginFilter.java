package com.leyou.gateway.filter;

import com.leyou.common.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.leyou.pojo.UserInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 9;
    }

    @Override
    public boolean shouldFilter() {
        // 获取白名单集合
        List<String> allowPaths = this.filterProperties.getAllowPaths();
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = context.getRequest();
        // 获取url
        String url = request.getRequestURL().toString();

        // 是否包含白名单路径
        for(String allowPath : allowPaths){
            if(StringUtils.contains(url, allowPath)){
                // 包含则不执行该过滤器
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = context.getRequest();
        // 获取cookie
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        if(StringUtils.isBlank(token)){
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
