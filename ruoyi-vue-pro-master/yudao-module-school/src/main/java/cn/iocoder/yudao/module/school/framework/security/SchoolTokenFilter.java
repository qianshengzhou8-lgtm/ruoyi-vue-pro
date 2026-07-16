package cn.iocoder.yudao.module.school.framework.security;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * School Token 过滤器，处理教职工/学生的登录 Token
 * 注册在 Spring Security 过滤器链之前，确保先于 TokenAuthenticationFilter 执行
 */
@Component
@Slf4j
public class SchoolTokenFilter extends OncePerRequestFilter implements Ordered {

    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_PREFIX = "school_token:";

    /** 在租户解析(-254)之后、Spring Security(0)之前执行 */
    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotEmpty(token) && !token.startsWith(securityProperties.getMockSecret())) {
            String redisValue = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + token);
            if (redisValue != null) {
                try {
                    String[] parts = redisValue.split(":", 2);
                    if (parts.length == 2 && ("staff".equals(parts[0]) || "student".equals(parts[0]))) {
                        Long userId = Long.valueOf(parts[1]);
                        LoginUser loginUser = new LoginUser()
                                .setId(userId)
                                .setUserType("staff".equals(parts[0])
                                        ? UserTypeEnum.ADMIN.getValue()
                                        : UserTypeEnum.MEMBER.getValue())
                                .setTenantId(WebFrameworkUtils.getTenantId(request));
                        SecurityFrameworkUtils.setLoginUser(loginUser, request);
                    }
                } catch (NumberFormatException e) {
                    log.warn("[SchoolTokenFilter] 无效的 token 值: {}", redisValue);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
