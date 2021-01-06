package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@WebServlet("/loadUserDirStructServlet")
public class LoadUserDirStructServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应的数据格式为json
        response.setContentType("application/json;charset=utf-8");
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        //结构文件
        String userResourcesPath = this.getServletContext().getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/userDirsStruct.txt");//获取上传文件的保存路径
        File userResourcesFiles = new File(userResourcesPath);

        //索引文件
        String userIndexResourcesPath = this.getServletContext().getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/userDirsStructIndex.txt");//获取上传文件的保存路径
        File userIndexResourcesFiles = new File(userIndexResourcesPath);

        if (!userResourcesFiles.exists()) {
            //创建用户目录文件
            userResourcesFiles.createNewFile();//如果文件不存在就创建这样一个文件
            FileOutputStream fileOutputStream = new FileOutputStream(userResourcesFiles);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new LinkedHashMap<String, LinkedList<String>>());
            objectOutputStream.close();

            //创建索引文件
            userIndexResourcesFiles.createNewFile();
            FileOutputStream fileIndexOutputStream = new FileOutputStream(userIndexResourcesFiles);
            ObjectOutputStream objectIndexOutputStream = new ObjectOutputStream(fileIndexOutputStream);
            objectIndexOutputStream.writeObject(new LinkedList<String>());
            objectIndexOutputStream.close();

            return;
        }


        ObjectInputStream is = null;
        ObjectInputStream isIndex = null;
        Object l = null;
        Object lIndex = null;
        //反序列化
        try {
            FileInputStream fileInputStream = new FileInputStream(userResourcesFiles);
            is = new ObjectInputStream(fileInputStream);

            FileInputStream fileIndexInputStream = new FileInputStream(userIndexResourcesFiles);
            isIndex = new ObjectInputStream(fileIndexInputStream);
            //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            l = is.readObject();
            lIndex = isIndex.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //3.释放资源
            try {
                is.close();
                isIndex.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LinkedHashMap<String, LinkedList<String>> h = (LinkedHashMap<String, LinkedList<String>>) l;
        LinkedList<String> hIndex = (LinkedList<String>)lIndex;


        //将用户资源存储到map集合中，发送给客户端
        Map<String, Object> map = new HashMap<>();

        map.put("userDirStructData",h);
        map.put("userDirStructDataIndex",hIndex);

        //将map转为json，并且传递给客户端
        ObjectMapper mapper = new ObjectMapper();
        //并且传递给客户端
        mapper.writeValue(response.getWriter(), map);

        System.out.println("索引文件："+hIndex);
        System.out.println("这是转化为json的样子："+mapper.writeValueAsString(map));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
