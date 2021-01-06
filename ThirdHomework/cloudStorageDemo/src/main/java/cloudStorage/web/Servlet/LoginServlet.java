package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import cloudStorage.service.UserService;
import cloudStorage.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //实例化一个service
        UserService service = new UserServiceImpl();

        //设置响应的数据格式为json
        resp.setContentType("application/json;charset=utf-8");
        //获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //查询登录用户是否为vip
        Boolean vip = service.findUserIsVip(username);

        //封装user对象
        User user = new User(username,password,vip);

         //调用service的login方法
        User loginUser = service.login(user);

        Map<String,Object> map = new HashMap<>();

        //判断user
        if(loginUser == null){
            //登录失败
            map.put("userExist",false);
            map.put("msg","登陆失败");

        }else{
            //登录成功
            //保存登录用户的数据
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);
            map.put("userExist",true);
            map.put("msg","登录成功");

        }

        //将map转为json，并且传递给客户端
        //将map转为json
        ObjectMapper mapper = new ObjectMapper();
        //并且传递给客户端
        mapper.writeValue(resp.getWriter(),map);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
