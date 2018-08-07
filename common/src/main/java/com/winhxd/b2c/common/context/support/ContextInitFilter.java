package com.winhxd.b2c.common.context.support;

import com.winhxd.b2c.common.context.UserContext;

import javax.servlet.*;
import java.io.IOException;

public class ContextInitFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserContext.initContext();
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
