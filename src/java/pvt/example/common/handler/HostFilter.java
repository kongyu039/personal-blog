package pvt.example.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pvt.example.common.config.HostIpConfig;
import pvt.example.common.utils.AppUtil;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 信息：src/java/pvt/example/common/config/HostFilter.java
 * <p>日期：2025/8/2
 * <p>描述：Host和Ip白名单过滤器
 */
@Component
public class HostFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(HostFilter.class);
    @Resource
    private HostIpConfig hostIpConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI(); // 获取请求的 URI
        String queryString = ((HttpServletRequest) request).getQueryString();
        String host = httpRequest.getServerName(); // 服务器名称(host)
        String ip = httpRequest.getRemoteAddr(); // 客户端ip
        logger.debug("HOST:" + host + "-IP:" + ip + "-URL:" + requestURI + (null == queryString ? "" : "?" + queryString));
        // 定义可以跳过过滤的特定 URL 路径
        if (requestURI.startsWith("/basic/change-ip_host") || requestURI.startsWith("/static")) {
            chain.doFilter(request, response); // 直接放行请求
            return; // 结束当前方法
        }
        if (AppUtil.isPrivateIP(host) || hostIpConfig.getAllowedHosts().contains(host) || hostIpConfig.getAllowedIPs().contains(ip)) {
            chain.doFilter(request, response); // 允许请求继续
        } else {
            throw new ServletException("不允许的主机或IP地址"); // 拒绝请求
        }
    }
}
