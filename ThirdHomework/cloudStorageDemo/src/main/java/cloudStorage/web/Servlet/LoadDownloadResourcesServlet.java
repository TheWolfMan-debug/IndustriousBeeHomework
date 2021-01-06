package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@WebServlet("/loadDownloadResourcesServlet")
public class LoadDownloadResourcesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应的数据格式为json
        response.setContentType("application/json;charset=utf-8");

        ServletContext servletContext = this.getServletContext();
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        String realDownloadPath = servletContext.getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/");
        System.out.println(realDownloadPath);
        File file = new File(realDownloadPath + "DownloadRecord.txt");

        //序列化一个字符串数组
        if (!file.exists()) {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new LinkedList<String>());
            objectOutputStream.close();
        }

        //读取数据
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
        Object o = null;
        try {
            o = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            //3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //4.使用读取出来的对象(打印)
        LinkedList<String> userDownloadRecords = (LinkedList<String>) o;
        System.out.println("加载缓存时用户的下载记录："+userDownloadRecords);

        //将用户资源存储到map集合中，发送给客户端
        Map<String, Object> map = new HashMap<>();

        if(userDownloadRecords.isEmpty())
        {
            map.put("hasData",false);
        }
        else {
            map.put("hasData",true);
            map.put("userDownloadRecord",userDownloadRecords);
        }

        //将map转为json，并且传递给客户端
        ObjectMapper mapper = new ObjectMapper();
        //并且传递给客户端
        mapper.writeValue(response.getWriter(), map);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
