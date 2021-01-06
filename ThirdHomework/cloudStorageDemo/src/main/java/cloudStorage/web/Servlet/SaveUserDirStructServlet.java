package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@WebServlet("/saveUserDirStructServlet")
public class SaveUserDirStructServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        HttpSession session = request.getSession();

        User currentUser = (User) request.getSession().getAttribute("currentUser");
        ServletContext servletContext = this.getServletContext();

        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
        ObjectInputStream is = null;
        ObjectInputStream isIndex = null;
        Object l = null;
        Object lIndex = null;

        //获取当前用户的文件路径
        String realDownloadPath = servletContext.getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/");
        File file = new File(realDownloadPath + "userDirsStruct.txt");

        File fileIndex =new File(realDownloadPath + "userDirsStructIndex.txt");


        //反序列化
        try {

            //用户文件结构
            FileInputStream fileInputStream = new FileInputStream(file);
            is = new ObjectInputStream(fileInputStream);

            //索引
            FileInputStream fileIndexInputStream = new FileInputStream(fileIndex);
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


        //将文件名写入，添加数据
        //如果为目录
        System.out.println("是否是文件夹" + request.getParameter("isDir"));

        System.out.println("开始写入数据");
        if ("true".equals(request.getParameter("isDir"))) {
            System.out.println("是文件夹");
            String userDirName = request.getParameter("userDirName");

            h.put(userDirName, new LinkedList<>());
            hIndex.add(userDirName);
            System.out.println(hIndex);
        } else {
            System.out.println("是文件");

            //将当前用户文件目录通过session保存
            session.setAttribute("currentUserDir",request.getParameter("parentName"));
            System.out.println("保存文件时，当前文件的目录名："+request.getParameter("parentName"));
            System.out.println("保存时用户目录为："+session.getAttribute("currentUserDir"));

            String parentName = request.getParameter("parentName");
            String userFileStructName = request.getParameter("userDirName");
            LinkedList<String> userFileName = h.get(parentName);
            userFileName.add(userFileStructName);
            h.put(parentName, userFileName);
        }
        System.out.println("写入数据完毕");
        System.out.println("-----------------------------------------------");

        //序列化，存储数据
        ObjectOutputStream oos = null;
        ObjectOutputStream oosIndex = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oosIndex = new ObjectOutputStream(new FileOutputStream(fileIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //使用ObjectOutputStream对象中的方法writeObject,把对象写入到文件中
        try {
            oos.writeObject(h);
            oosIndex.writeObject(hIndex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {        //释放资源
            try {
                oos.close();
                oosIndex.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //回写数据
        Map<String,Object> map = new HashMap<>();

        //将map转为json，并且传递给客户端
        //将map转为json
        ObjectMapper mapper = new ObjectMapper();
        //并且传递给客户端
        mapper.writeValue(response.getWriter(),map);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
