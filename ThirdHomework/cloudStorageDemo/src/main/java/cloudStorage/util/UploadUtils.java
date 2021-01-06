package cloudStorage.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public class UploadUtils {
    public UploadUtils() {
    }

    public static DiskFileItemFactory getDiskFileItemFactory(File file) {
        //1、创建DiskFileItemFactory对象，处理文件上传路径或限制文件大小
        DiskFileItemFactory factory = new DiskFileItemFactory();

        //通过这个工厂设置一个缓冲区，当上传的文件大小大于缓冲区的时候，将它放到临时文件中；
//        factory.setSizeThreshold(1024 * 1024);//缓冲区大小为1M
        factory.setSizeThreshold(1024);//缓冲区大小为1024k
        factory.setRepository(file);
        return factory;
    }

    public static ServletFileUpload getServletFileUpload(DiskFileItemFactory factory) {
        //2、获取ServletFileUpload
        ServletFileUpload upload = new ServletFileUpload(factory);
        //监听文件上传进度
        upload.setProgressListener(new ProgressListener() {
            @Override
            public void update(long pBytesRead, long lpContentLenght, int i) {
                //pBytesRead:已读取到的文件大小
                //pContentLenght：文件大小
                System.out.println("总大小：" + lpContentLenght + "已上传：" + pBytesRead);
            }
        });

        //处理乱码问题
        upload.setHeaderEncoding("UTF-8");
        //设置单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 * 10);
        //设置总共能够上传文件的大小
        //1024 = 1kb * 1024 = 1M * 10 = 10M
        upload.setSizeMax(1024 * 1024 * 10);
        return upload;
    }

    public static String uploadParseRequest(ServletFileUpload upload, HttpServletRequest request, String uploadPath) throws Exception {
        String msg = "";
        String newFileName = "";
        //3、处理上传文件
        //把前端的请求解析，封装成一个FileItem对象
        List<FileItem> fileItems = null;
        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        //从后向前遍历，先拿到重命名名字，再处理上传的文件
        for (int i = fileItems.size()-1; i >=0; i--) {
            FileItem fileItem= fileItems.get(i);
            if (fileItem.isFormField()) { //判断是普通表单还是带文件的表单
                //getFieldName指的是前端表单控件的name
                String name = fileItem.getFieldName();
                String value = null;//处理乱码
                try {
                    value = fileItem.getString("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("name为"+name+"普通表单的名称："+value);
                if(!"".equals(value))
                {
                    newFileName = value;
                }
                continue;
            } //判断它是带文件的表单

            //======================处理文件=======================//

            //拿到文件的名字
            String uploadFileName = fileItem.getName();
            System.out.println("此文件上传的文件名：" + uploadFileName);

            if (uploadFileName == null || uploadFileName.trim().equals("")) {//两个判断条件相同
                continue;
            }

            //获得上传的文件名，例如/img/girl/ooa.jpg,只需要ooa，其前面的后面的都不需要
            String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            System.out.println("文件名"+fileName);
            //获得文件的后缀名
            String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
            System.out.println("后缀名"+fileExtName);

            /*
             * 如果后缀名 fileExtName 不是我们需要的
             *就直接return，不处理，告诉用户类型不对
             * */
            System.out.println("文件信息【文件名：" + fileName + "文件类型：" + fileExtName + "】");

            //可以使用UUID(唯一通用识别码)来保证文件名的统一
//                String uuidFileName = UUID.randomUUID().toString();


            //=======================传输文件=========================//
            //获得文件上传的流
            InputStream inputStream = fileItem.getInputStream();

            //创建一个文件输出流
//                FileOutputStream fos = new FileOutputStream(uploadPath + "/" + uuidFileName + "." + fileExtName);

//            System.out.println("前newFileName名称为"+newFileName);
            if ("".equals(newFileName)) {
                System.out.println("没有改变名称");
                newFileName = fileName.substring(0, fileName.indexOf("."));
            }
//            System.out.println("后newFileName名称为"+newFileName);

            //使用新名字命名
            FileOutputStream fos = new FileOutputStream(uploadPath + "/" + newFileName + "." + fileExtName);

            //创建一个缓冲区
            byte[] buffer = new byte[1024 * 1024];

            //判断是否读取完毕
            int len;

            //如果大于0，说明还存在数据
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            //关闭流
            fos.close();
            inputStream.close();

            msg = "文件上传成功！";
            fileItem.delete();//上传成功，清除临时文件
        }

        return msg;
    }
}



//从前往后遍历，得不到重命名的名字
//        for (FileItem fileItem : fileItems) {
//            if (fileItem.isFormField()) { //判断是普通表单还是带文件的表单
//                //getFieldName指的是前端表单控件的name
//                String name = fileItem.getFieldName();
//                String value = null;//处理乱码
//                try {
//                    value = fileItem.getString("UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("name为"+name+"普通表单的名称："+value);
//                if(!"".equals(value))
//                {
//                    newFileName = value;
//                }
//                continue;
//            } //判断它是带文件的表单
//
//            //======================处理文件=======================//
//
//            //拿到文件的名字
//            String uploadFileName = fileItem.getName();
//            System.out.println("此文件上传的文件名：" + uploadFileName);
//
//            if (uploadFileName == null || uploadFileName.trim().equals("")) {//两个判断条件相同
//                continue;
//            }
//
//            //获得上传的文件名，例如/img/girl/ooa.jpg,只需要ooa，其前面的后面的都不需要
//            String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
//            System.out.println("文件名"+fileName);
//            //获得文件的后缀名
//            String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
//            System.out.println("后缀名"+fileExtName);
//
//            /*
//             * 如果后缀名 fileExtName 不是我们需要的
//             *就直接return，不处理，告诉用户类型不对
//             * */
//            System.out.println("文件信息【文件名：" + fileName + "文件类型：" + fileExtName + "】");
//
//            //可以使用UUID(唯一通用识别码)来保证文件名的统一
////                String uuidFileName = UUID.randomUUID().toString();
//
//
//            //=======================传输文件=========================//
//            //获得文件上传的流
//            InputStream inputStream = fileItem.getInputStream();
//
//            //创建一个文件输出流
////                FileOutputStream fos = new FileOutputStream(uploadPath + "/" + uuidFileName + "." + fileExtName);
//
//            System.out.println("前newFileName名称为"+newFileName);
//            if ("".equals(newFileName)) {
//                System.out.println("没有改变名称");
//                newFileName = fileName.substring(0, fileName.indexOf("."));
//            }
//            System.out.println("后newFileName名称为"+newFileName);
//
//            //使用新名字命名
//            FileOutputStream fos = new FileOutputStream(uploadPath + "/" + newFileName + "." + fileExtName);
//
//            //创建一个缓冲区
//            byte[] buffer = new byte[1024 * 1024];
//
//            //判断是否读取完毕
//            int len;
//
//            //如果大于0，说明还存在数据
//            while ((len = inputStream.read(buffer)) > 0) {
//                fos.write(buffer, 0, len);
//            }
//
//            //关闭流
//            fos.close();
//            inputStream.close();
//
//            msg = "文件上传成功！";
//            fileItem.delete();//上传成功，清除临时文件
//
//        }
