package com.winhxd.b2c.admin.common.context;

import com.winhxd.b2c.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 初始化msVer数据
 *
 * @author lixiaodong
 */
@Component
public class ContextInitFilter implements Filter {
    @Autowired
    private Cache cache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserManager.initUser((HttpServletRequest) request, cache);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
