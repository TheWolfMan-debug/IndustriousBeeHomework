package cloudStorage.web.Filter;

import cloudStorage.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录验证的过滤器
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println(req);
        //0.强制转换
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;


        //1.获取资源请求路径
        String uri = request.getRequestURI();
        //2.判断是否包含登录相关资源路径,要注意排除掉 css/js/图片/验证码等资源
        if (uri.contains("/login") || uri.contains("/register") || uri.contains("/css/") || uri.contains("/js/") || uri.contains("/fonts/")) {
            //包含，用户就是想登录。放行
            chain.doFilter(req, resp);
        } else {
            //不包含，需要验证用户是否登录
            //获取当前登录的用户
            User user = (User) request.getSession().getAttribute("currentUser");
            //3.从获取session中获取user
            if (user != null) {
                //登录了。放行
                chain.doFilter(req, resp);
            } else {
                //没有登录。
                //使用重定向，使地址栏发生改变，避免跳转页面后请求地址不改变，导致登录不上的问题
                response.sendRedirect("/wolf/loginPage.html");
            }

        }
//        chain.doFilter(req, resp); //如果是上传或者下载文件，相应完之后无法调用此方法，应注销此方法
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

}
