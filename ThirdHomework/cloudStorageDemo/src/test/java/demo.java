import java.io.*;
import java.util.ArrayList;

public class demo {

    public static void main(String[] args) {

        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
        ObjectInputStream is = null;
        Object l = null;
        try {
            File file = new File("person.txt");
            if(!file.exists())
            {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(new ArrayList<String>());
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            is = new ObjectInputStream(fileInputStream);
            //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            l = is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            //3.释放资源
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> h = (ArrayList<String>) l;
        h.add("我很好");
        h.add("你好");


        //1.创建ObjectOutputStream对象,构造方法中传递字节输出流
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("person.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.使用ObjectOutputStream对象中的方法writeObject,把对象写入到文件中
        try {
            oos.writeObject(h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.释放资源
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("person.txt"));
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
        ArrayList<String> p = (ArrayList<String>) o;
        System.out.println(p);







        //        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
//        ObjectInputStream is = null;
//        try {
//            is = new ObjectInputStream(new FileInputStream("person.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
//        Object l = null;
//        try {
//            l = is.readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            //3.释放资源
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ArrayList<String> h = (ArrayList<String>) l;
//        h.add("我很好");
//
//
//
////        ArrayList<String> strings = new ArrayList<>();
//        h.add("你好");
////
//        //1.创建ObjectOutputStream对象,构造方法中传递字节输出流
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(new FileOutputStream("person.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //2.使用ObjectOutputStream对象中的方法writeObject,把对象写入到文件中
//        try {
//            oos.writeObject(h);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //3.释放资源
//        try {
//            oos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        //1.创建ObjectInputStream对象,构造方法中传递字节输入流
//        ObjectInputStream ois = null;
//        try {
//            ois = new ObjectInputStream(new FileInputStream("person.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //2.使用ObjectInputStream对象中的方法readObject读取保存对象的文件
//        Object o = null;
//        try {
//            o = ois.readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            //3.释放资源
//            try {
//                ois.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //4.使用读取出来的对象(打印)
//        ArrayList<String> p = (ArrayList<String>) o;
//        System.out.println(p);
    }
}
