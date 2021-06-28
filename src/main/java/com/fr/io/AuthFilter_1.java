package com.fr.io;

import com.fr.data.NetworkHelper;
import com.fr.decision.authority.data.User;
import com.fr.decision.mobile.terminal.TerminalHandler;
import com.fr.decision.webservice.exception.user.UserNotExistException;
import com.fr.decision.webservice.login.LogInOutResultInfo;
import com.fr.decision.webservice.v10.login.LoginService;
import com.fr.decision.webservice.v10.login.event.LogInOutEvent;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter，采用session方式
 * FindBI，单点登录认证过滤器.
 *
 * @author Anner
 * created on 2020-12-07
 */
public class AuthFilter_1 implements Filter {


    // 携带用户名的参数名称，不能使用username，会存在冲突
    private static final String userNameParameter = "fr_username";
    private static final String TOKEN = "fine_auth_token";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // username的获取，根据具体情况而定，测试情况下可写死用户名测试
        //String username = "sysadmin";
        String username = request.getParameter(userNameParameter);
        FineLoggerFactory.getLogger().info("username=" + username);
        // 用户名参数不为空时，走登录逻辑
        if (StringUtils.isNotEmpty(username)) {
            // 获取该用户名的token
            String oldToken = (String) request.getSession().getAttribute("fine_auth_token");
            FineLoggerFactory.getLogger().info("oldToken==" + oldToken);
            // 无token/token失效，则走登录逻辑
            if (StringUtils.isEmpty(oldToken) || !checkTokenValid(request, oldToken, username)) {
                try {
                    // 系统中无该用户则抛出异常
                    User user = UserService.getInstance().getUserByUserName(username);
                    FineLoggerFactory.getLogger().info("user=" + user);
                    if (user == null) {
                        throw new UserNotExistException();
                    }
                    // 将token写入response
                    String token = LoginService.getInstance().login(request, response, username);
                    // 将token写入request
                    request.getSession().setAttribute("fine_auth_token", token);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            } else {
                filterChain.doFilter(request, response);
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

    private String generateURIWithoutUsername(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        registerLogoutEvent();
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

    private void registerLogoutEvent() {
        EventDispatcher.listen(LogInOutEvent.LOGOUT, new Listener<LogInOutResultInfo>() {

            @Override
            public void on(Event event, LogInOutResultInfo resultInfo) {
                HttpSession session = resultInfo.getRequest().getSession(false);
                if (session != null && StringUtils.isNotEmpty((String) session.getAttribute(TOKEN))) {
                    session.removeAttribute(TOKEN);
                }
            }
        });
    }
}