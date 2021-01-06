package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import cloudStorage.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置响应的数据格式为json
        response.setContentType("application/json;charset=utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String isVip = request.getParameter("vip");

        Boolean vip="yes".equals(isVip);

        UserServiceImpl userService = new UserServiceImpl();
        User newUser = new User(username, password, vip);


        Boolean result = userService.register(newUser);

        Map<String, Object> map = new HashMap<>();

        //5.判断user
        if (result) {
            //注册失败
            map.put("registerResult", true);
            map.put("msg", "注册成功");
        } else {
            //注册成功
            map.put("registerResult", false);
            map.put("msg", "注册失败");
        }

        //将map转为json，并且传递给客户端
        ObjectMapper mapper = new ObjectMapper();
        //并且传递给客户端
        mapper.writeValue(response.getWriter(), map);


        //创建上传文件的保存目录，为了安全建议在WEB-INF目录下，用户无法访问
        String uploadPath = this.getServletContext().getRealPath("WEB-INF/userResources/" + username);//获取上传文件的保存路径
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();//如果目录不存在就创建这样一个目录
        }
        //临时文件
        //临时路径，如果上传的文件超过预期的大小，我们将它存放到一个临时目录中，过几天自动删除，或者提醒用户转存为永久
        String tmpPath = this.getServletContext().getRealPath("WEB-INF/tmp/" + username);
        File file = new File(tmpPath);
        if (!file.exists()) {
            file.mkdirs();//如果目录不存在就创建这样临时目录
        }
    }
}
