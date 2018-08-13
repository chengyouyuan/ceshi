package com.winhxd.b2c.common.context.support;

import brave.Tracer;
import com.winhxd.b2c.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

public class ContextInitFilter implements Filter {
    @Autowired
    private Tracer tracer;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserContext.initContext(tracer);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
