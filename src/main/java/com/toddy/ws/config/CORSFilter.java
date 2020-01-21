package com.toddy.ws.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, x-auth-token, origin, content-type, accept");

        /**
         * IN USE
         *     Access-Control-Allow-Origin : specifies the authorized domains to make cross-domain request. Use “*” as value if there is no restrictions.
         *     Access-Control-Max-Age : indicates how long the results of a preflight request can be cached.
         *     Access-Control-Allow-Methods : indicates the methods allowed when accessing the resource (sec).
         *     Access-Control-Allow-Headers : indicates which header field names can be used during the actual request.
         *
         * OTHERS
         *     Access-Control-Allow-Credentials : specifies if cross-domain requests can have authorization credentials or not.
         *     Access-Control-Expose-Headers : indicates which headers are safe to expose.
         * **/

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Try replace for filterChain.doFilter(request, response); site exemple: request, servletResponse
            filterChain.doFilter(request, servletResponse); // servletRequest, servletResponse
        }
    }
}
