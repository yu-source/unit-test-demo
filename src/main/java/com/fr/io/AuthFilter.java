package com.fr.io;

import com.fr.data.NetworkHelper;
import com.fr.decision.authority.data.User;
import com.fr.decision.mobile.terminal.TerminalHandler;
import com.fr.decision.webservice.exception.user.UserNotExistException;
import com.fr.decision.webservice.utils.DecisionServiceConstants;
import com.fr.decision.webservice.v10.login.LoginService;
import com.fr.decision.webservice.v10.login.TokenResource;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.security.JwtUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Device;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * AuthFilter，采用cookie方式
 *
 * @author Anner
 * created on 2020-12-07
 */
public class AuthFilter implements Filter {

    // 携带用户名的参数名称，不能使用username，会存在冲突
    private static final String userNameParameter = "fr_username";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 这个地方username的获取，根据具体你请求的时候把username放在哪了来确定
        String username = request.getParameter(userNameParameter);
        //String username = "sysadmin";
        // 用户名参数不为空时，走登录逻辑
        if (StringUtils.isNotEmpty(username)) {
            // 获取该用户名的token
            String oldToken = TokenResource.COOKIE.getToken(request);
            // 无token/token失效，则走登录逻辑
            if (StringUtils.isEmpty(oldToken) || !checkTokenValid(request, oldToken, username)) {
                try {
                    // 系统中无该用户则抛出异常
                    User user = UserService.getInstance().getUserByUserName(username);
                    if (user == null) {
                        throw new UserNotExistException();
                    }
                    // 将token写入response
                    String token = LoginService.getInstance().login(request, response, username);
                    // 将token写入request
                    request.setAttribute(DecisionServiceConstants.FINE_AUTH_TOKEN_NAME, token);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
            // 检查用户名参数是否位于URL，是的话去掉并重定向，以隐藏URL中的用户名
            if (StringUtils.equals(request.getMethod(), "GET") && request.getQueryString() != null && request.getQueryString().contains(userNameParameter)) {
                response.sendRedirect(generateURIWithoutUsername(request));
            } else {
                // 不是位于URL的话直接doFilter
                filterChain.doFilter(request, response);
            }
        } else {
            // 用户名参数为空，直接doFilter
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    // token有效性检验
    private boolean checkTokenValid(HttpServletRequest req, String token, String currentUserName) {

        try {
            // 当前登录用户和token对应的用户名不同，需要重新生成token
            if (!ComparatorUtils.equals(currentUserName, JwtUtils.parseJWT(token).getSubject())) {
                FineLoggerFactory.getLogger().info("username changed：" + currentUserName);
                return false;
            }
            Device device = NetworkHelper.getDevice(req);
            // 判断该token是否还保存在状态服务器中，也就是判断该token是否还有用
            LoginService.getInstance().loginStatusValid(token, TerminalHandler.getTerminal(req, device));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    // 去除URL参数中的用户名参数
    private String generateURIWithoutUsername(HttpServletRequest request) {

        List<String> queryParams = Arrays.asList(request.getQueryString().split("&"));
        if (!queryParams.isEmpty()) {
            StringBuilder queryStringWithoutUsername = new StringBuilder("?");
            for (String param : queryParams) {
                if (!StringUtils.contains(param, userNameParameter)) {
                    queryStringWithoutUsername.append(param);
                    queryStringWithoutUsername.append("&");
                }
            }
            String newUrl = queryStringWithoutUsername.toString();
            return request.getRequestURI() + newUrl.substring(0, newUrl.length() - 1);
        } else {
            return request.getRequestURI();
        }
    }

}