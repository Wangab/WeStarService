package com.wangab.filter;

import com.wangab.service.MySqlService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by wanganbang on 8/31/16.
 */
public class TokenFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    MySqlService mySqlService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("token filter init ... ...");
        mySqlService = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext()).getBean(MySqlService.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("执行拦截 -- " + ((HttpServletRequest)servletRequest).getRequestURL());
        MyServletRequestWrapper request = new MyServletRequestWrapper((HttpServletRequest)servletRequest);
        String userID = "";
        if ("DELETE".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
            String[] strs = request.getRequestURI().split("/");
            if (strs.length >= 4){
                userID = strs[3];
            } else {
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                response.sendError(HttpStatus.BAD_REQUEST.value(), "参数不足");
                return;
            }
        } else {
            BufferedReader reader = request.getReader();
            StringBuffer stringBuilder = new StringBuffer();
            char[] chars = new char[1024];
            while (reader.read(chars) != -1){
                stringBuilder.append(chars);
            }
            String jsonstr = stringBuilder.toString();
            log.info("解析请求内容 -- \n" + jsonstr);
            JSONObject jsonObject = new JSONObject(jsonstr);
            userID = jsonObject.optString("userID");

        }
        //        log.info(userID);
        String accessToken = ((HttpServletRequest) servletRequest).getHeader("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = servletRequest.getParameter("accessToken");
        }
        if (accessToken != null) {
            boolean isok = mySqlService.checkToken(accessToken, userID);
            log.info("检查到accessToken -- " + accessToken + "\t result -- " + isok);
            if (!isok) {
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                return;
            }
        } else {
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        log.warn("token filter destroy ... ...");
    }
}
