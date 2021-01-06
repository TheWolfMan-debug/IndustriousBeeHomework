package cloudStorage.web.Servlet;

import cloudStorage.domain.User;
import cloudStorage.util.DownLoadUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@WebServlet("/userDownloadServlet")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.获取请求参数，文件名称
        String filename = request.getParameter("filename");
        String currentFileDir = request.getParameter("currentFileDir");
        System.out.println("文件名：" + filename);
        System.out.println("目录名：" + currentFileDir);
        String preCodeFilename =filename;


        User currentUser = (User) request.getSession().getAttribute("currentUser");

        //2.使用字节输入流加载文件进内存
        //2.1找到文件服务器路径
        ServletContext servletContext = this.getServletContext();


        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------

        //在用户目录下查找文件
        String recordRealDownloadPath = servletContext.getRealPath("WEB-INF/userResources/" + currentUser.getUsername()+"/"+currentFileDir);
        File recordFile = new File(recordRealDownloadPath);

        if(!recordFile.exists())
        {
            System.out.println("该目录不存在");
            try {
                response.sendRedirect("/wolf/menuPage.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        String[] fileLists = recordFile.list();
        for (String currentDirFile : fileLists) {

            if(currentDirFile.substring(0,currentDirFile.indexOf('.')).equals(filename))
            {
                filename = currentDirFile;
                System.out.println(filename);
                break;
            }
        }

        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------


        String downloadFile = servletContext.getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/" + currentFileDir + "/" + filename);
        System.out.println(downloadFile);
        if (!(new File(downloadFile).exists()) || "".equals(filename)) {
            System.out.println("该文件不存在");
            try {
                response.sendRedirect("/wolf/menuPage.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //2.2用字节流关联
        FileInputStream fis = new FileInputStream(downloadFile);
        System.out.println("流的大小："+fis.available());
        double downloadFileLength = fis.available();
        //设置response的响应头
        //设置响应头类型：content-type
        String mimeType = servletContext.getMimeType(filename);//获取文件的mime类型
        response.setHeader("content-type", mimeType);
        //设置响应头打开方式:content-disposition

        //解决中文文件名问题
        //1.获取user-agent请求头、
        String agent = request.getHeader("user-agent");
        //2.使用工具类方法编码文件名即可
        System.out.println("编码前："+filename);
        filename = DownLoadUtils.getFileName(agent, filename);
        System.out.println("编码后："+filename);


        response.setHeader("content-disposition", "attachment;filename=" + filename);
        //4.将输入流的数据写出到输出流中
        ServletOutputStream sos = response.getOutputStream();
        byte[] buff;
        //判断当前用户是否为vip
        System.out.println("当前用户：" + currentUser.getUsername());
        System.out.println("是否为vip：" + currentUser.getVip());
        System.out.println("下载进度：");
        double sum = 0;
        long startTime = System.currentTimeMillis();
        if (currentUser.getVip()) {
            //加速
            buff = new byte[1024 * 8];
            //下载文件
            int len;
            while ((len = fis.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
        } else {
            //减速
            buff = new byte[1024];
            //下载文件
            int len;
            while ((len = fis.read(buff)) != -1) {
                try {
                    Thread.sleep(3); //睡眠
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sos.write(buff, 0, len);
                sum+=len;
                System.out.println(sum/downloadFileLength);
            }
        }
        System.out.println("sum="+sum);
        long endTime = System.currentTimeMillis();
        System.out.println("所用时间：" + (endTime - startTime));

        fis.close();

        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------------------------



        //下载时记录用户下载的文件历史
        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
        ObjectInputStream is = null;
        Object l = null;

        //获取当前用户的文件路径
        String realDownloadPath = servletContext.getRealPath("WEB-INF/userResources/" + currentUser.getUsername() + "/");

        System.out.println("下载文件时，记录用户的下载历史：" + realDownloadPath);
        File file = new File(realDownloadPath + "DownloadRecord.txt");

        //反序列化
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            is = new ObjectInputStream(fileInputStream);
            //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            l = is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //3.释放资源
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LinkedList<String> h = (LinkedList<String>) l;
        //将文件名写入，添加数据
        System.out.println("存之前："+filename);
        h.addFirst(preCodeFilename);
        System.out.println("存之后："+h);

        //序列化，存储数据
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //使用ObjectOutputStream对象中的方法writeObject,把对象写入到文件中
        try {
            oos.writeObject(h);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {        //释放资源
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("用户下载结束-------------------------------");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
