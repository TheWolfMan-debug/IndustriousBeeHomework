import java.io.File;

//
///**
// * 大二同学，第二次考核任务：
// * 多线程读取文件，文件内容自定义，使用多个线程读取一个文件，每个线程读取一部分，
// * 然后拼接到另一个文件，在每个线程读取内容的前面加上线程名。
// * <p>
// * 大二考核任务二举例：
// * 假设有一文件F,F的内容分为a,b,即有F=A+B,线程a读取F的内容A，线程b读取B,
// * 然后输出到文件F2,文件内容为a:A  b:B
// * 以上仅为例子，线程数，线程名，文件内容可自定义
// * <p>
// * 注意：
// * 任务提交时间：11月6日之前（20天）。提交方式：github链接+学号姓名。
// * 本次任务完成后将会安排一次面试，考核各位同学对于所学习内容的情况。
// */


public class ThreadReadFile {
    // 设置读取文件指针移动的距离
    public static int endPointer = 1024;

    public static void main(String[] args) throws Exception {
        // 创建读取文件地址
        File f1 = new File("SecondHomeWork\\src\\oldFile.txt");
        // 创建输出文件地址
        File f2 = new File("SecondHomeWork\\src\\newFile.txt");

        // 判断文件是否存在
        if (!f1.exists()) {
            System.out.println("读取文件不存在！");
            return;
        }

        // 将文件分块
        long divide = f1.length() / endPointer;
        divide = f1.length() % endPointer == 0 ? divide : divide + 1;

        // 循环调用多线程读取并写入文件
        for (int i = 0; i < divide; i++) {
            new Thread(new MyRunnableImpl(f1, f2, i*endPointer,endPointer)).start();
            // 延迟十毫秒 使线程名顺序打出来，若没有延迟，线程名会重叠（个人观点）
            Thread.sleep(10);
        }

        // 延时
        Thread.sleep(1000);
        // 打印提示信息
        System.out.println("复制完成！");
    }

}

