package cloudStorage.web.Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import cloudStorage.domain.User;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cloudStorage.util.UploadUtils;

@WebServlet("/uploadServlet")
public class UploadServlet extends HttpServlet {

    //实例化工具类
    static UploadUtils uploadUtil = new UploadUtils();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        String currentUserDir = (String) session.getAttribute("currentUserDir");
//        System.out.println("上传时用户目录为："+session.getAttribute("currentUserDir"));

        System.out.println(currentUserDir);
        //判断上传的表单是普通表单还是带文件的表单，是返回true,否返回false；
        if (!ServletFileUpload.isMultipartContent(request)) {
            return;
        }//如果通过了这个if，说明我们的表单是带文件上传的
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        //创建上传文件的保存目录，为了安全建议在WEB-INF目录下，用户无法访问
        String uploadPath = this.getServletContext().getRealPath("WEB-INF/userResources/" + currentUser.getUsername()+"/"+currentUserDir);//获取上传文件的保存路径
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();//如果目录不存在就创建这样一个目录
        }
        //临时文件
        //临时路径，如果上传的文件超过预期的大小，我们将它存放到一个临时目录中，过几天自动删除，或者提醒用户转存为永久
        String tmpPath = this.getServletContext().getRealPath("WEB-INF/tmp/" + currentUser.getUsername());
        File file = new File(tmpPath);
        if (!file.exists()) {
            file.mkdirs();//如果目录不存在就创建这样临时目录
        }
        //处理上传的文件一般需要通过流来获取，我们可以通过request.getInputstream(),原生态文件上传流获取，十分麻烦
        //但是我们都建议使用Apache的文件上传组件来实现，common-fileupload,它需要依赖于common-io组件；

        //1、创建DiskFileItemFactory对象，处理文件上传路径或限制文件大小
        DiskFileItemFactory factory = uploadUtil.getDiskFileItemFactory(file);
        //2、获取ServletFileUpload
        ServletFileUpload upload = uploadUtil.getServletFileUpload(factory);
        //3、处理上传文件
        try {
            uploadUtil.uploadParseRequest(upload, request, uploadPath);
            response.sendRedirect("/wolf/menuPage.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}


//    1、准备工作
//    对于文件上传，浏览器在上传的过程中是将文件以流的形式提交到服务器端的。
//    一般采用Apache的开源工具common-fileupload这个文件上传组件。
//    common-fileupload是依赖于common-io这个包的，所以还需要下载这个包。
//    我们下载最新的jar包:
//    common-fileupload : 点击下载
//    common-io :点击下载
//    在JavaWeb项目中导入Jar包：
//            2、使用类介绍
//            【文件上传注意事项】
//            1、为保证服务器的安全，上传的文件应放在外界无法访问的目录下，如WEN-INF。
//            2、为防止同名文件产生覆盖现象，要为文件指定一个唯一的文件名。
//            3、要对上传文件的大小进行限制。
//            4、限制上传文件的类型，收到文件时，判断文件名是否合法。


//    form表单中method必须设置为post，并且要设置enctype=”multipart/form-data”：

//    核心API—DiskFileItemFactory
//    DiskFileItemFactory 是创建FileItem 对象的工厂，这个工厂类常用方法：
//            1、public void setSizeThreshold(int sizeThreshold) ：设置内存缓冲区的大小，默认值为10K。当上传文件大于缓冲区大小时，fileupload组件将使用临时文件缓存上传文件。
//            2、public void setRepository(Java.io.File repository) ：指定临时文件目录，默认值为System.getProperty(“java.io.tmpdir”).
//            3、public DiskFileItemFactory(int sizeThreshold,java.io.File repository) ：构造函数
//
//    核心API—-ServletFileUpload
//    ServletFileUpload 负责处理上传的文件数据，并将表单中每个输入项封装成一个FileItem 对象中。常用方法有：
//            1、boolean isMultipartContent(HttpServletRequest request) ：判断上传表单是否为multipart/form-data类型
//
//            2、List parseRequest(HttpServletRequest request)：解析request对象，并把表单中的每一个输入项包装成一个fileItem 对象，并返回一个保存了所有FileItem的list集合。
//
//            3、setFileSizeMax(long fileSizeMax) ：设置上传文件的最大值（单个文件），用于设置单个上传文件的最大尺寸限制，以防止客户端恶意上传超大文件来浪费服务器端的存储空间。其参数是以字节为单位的long型数字。
//
//            4、setSizeMax(long sizeMax) ：设置上传文件总量的最大值（所有上传文件），用于设置请求消息实体内容（即所有上传数据）的最大尺寸限制，以防止客户端恶意上传超大文件来浪费服务器端的存储空间。其参数是以字节为单位的long型数字。
//
//            5、setHeaderEncoding(java.lang.String encoding) ：设置编码格式。在文件上传请求的消息体中，除了普通表单域的值是文本内容以外，文件上传字段中的文件路径名也是文本，在内存中保存的是它们的某种字符集编码的字节数组，Apache文件上传组件在读取这些内容时，必须知道它们所采用的字符集编码，才能将它们转换成正确的字符文本返回。
//
//    核心API—FileItem
//    FileItem类的常用方法：
//            1、boolean isFormField()： isFormField方法用于判断FileItem类对象封装的数据是一个普通文本表单字段，还是一个文件表单字段，如果是普通表单字段则返回true，否则返回false。
//
//            2、 String getName()
//    用于获得文件上传字段中的文件名。注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名。
//
//            3、String getFieldName()
//    用于返回表单标签name属性的值。
//
//            4、 void write(File file)：用于将FileItem对象中保存的主体内容保存到某个指定的文件中。如果FileItem对象中的主体内容是保存在某个临时文件中，该方法顺利完成后，临时文件有可能会被清除。该方法也可将普通表单字段内容写入到一个文件中，但它主要用途是将上传的文件内容保存在本地文件系统中。
//
//            5、 String getString()：用于将FileItem对象中保存的数据流内容以一个字符串返回，它有两个重载的定义形式：
//    public Java.lang.String getString()；
//    public java.lang.String getString(java.lang.String encoding) throws java.io.UnsupportedEncodingException
//    前者使用缺省的字符集编码将主体内容转换成字符串，后者使用参数指定的字符集编码将主体内容转换成字符串。如果在读取普通表单字段元素的内容时出现了中文乱码现象，请调用第二个getString方法，并为之传递正确的字符集编码名称。
//
//            6、 void delete()：delete方法用来清空FileItem类对象中存放的主体内容，如果主体内容被保存在临时文件中，delete方法将删除该临时文件。尽管当FileItem对象被垃圾收集器收集时会自动清除临时文件，但及时调用delete方法可以更早的清除临时文件，释放系统存储资源。
//
//    实现步骤：
//            1、创建DiskFileItemFactory对象，设置缓冲区大小和临时文件目录。
//            2、使用DiskFileItemFactory 对象创建ServletFileUpload对象，并设置上传文件的大小限制。
//            3、调用ServletFileUpload.parseRequest方法解析request对象，得到一个保存了所有上传内容的List对象。
//            4、对list进行迭代，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件：
//            4.1、 为普通表单字段，则调用getFieldName、getString方法得到字段名和字段值。
//            4.2、为上传文件，则调用getInputStream方法得到数据输入流，从而读取上传数据。


