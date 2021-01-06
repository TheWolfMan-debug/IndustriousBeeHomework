package cloudStorage.web.Servlet;

import cloudStorage.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exitServlet")
public class ExitServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //注销用户，将session中的当前用户置为空
        request.getSession().setAttribute("currentUser", null);
        User nextUser = (User) request.getSession().getAttribute("currentUser");
//        if(nextUser==null)
//        {
//            System.out.println("已注销");
//        }
        response.sendRedirect("/wolf/loginPage.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
