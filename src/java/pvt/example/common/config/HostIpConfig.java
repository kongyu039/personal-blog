package pvt.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 信息：src/java/pvt/example/common/config/HostIpConfig.java
 * <p>日期：2025/8/2
 * <p>描述：Host白名单初始化配置
 */
@Component
public class HostIpConfig {
    @Value("#{'${app.allowed.hosts}'.split(',')}")
    private Set<String> allowedHosts; // 直接映射为 Set
    @Value("#{'${app.allowed.ips}'.split(',')}")
    private Set<String> allowedIPs;    // 直接映射为 Set

    /**
     * 添加 IP 地址
     * @param ip 要添加的 IP 地址
     */
    public void addIp(String ip) { allowedIPs.add(ip); }

    /**
     * 删除 IP 地址
     * @param ip 要删除的 IP 地址
     */
    public void removeIp(String ip) { allowedIPs.remove(ip); }

    /**
     * 添加 Host 地址
     * @param host 要添加的 Host 地址
     */
    public void addHost(String host) { allowedHosts.add(host); }

    /**
     * 删除 Host 地址
     * @param host 要删除的 Host 地址
     */
    public void removeHost(String host) { allowedHosts.remove(host); }

    public Set<String> getAllowedHosts() { return allowedHosts; }

    public void setAllowedHosts(Set<String> allowedHosts) { this.allowedHosts = allowedHosts; }

    public Set<String> getAllowedIPs() { return allowedIPs; }

    public void setAllowedIPs(Set<String> allowedIPs) { this.allowedIPs = allowedIPs; }
}
