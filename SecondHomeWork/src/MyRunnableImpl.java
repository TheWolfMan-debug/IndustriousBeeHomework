import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MyRunnableImpl implements Runnable {

    private final byte[] by;
    // 设置开始位置的偏移
    private final long startPointer;
    // 读取文件的长度
    private final int block;
    // 创建随机流
    private RandomAccessFile rafR;
    private RandomAccessFile rafW;


    // 接收两个随机流文件参数，一个位置偏移参数
    public MyRunnableImpl(File fR, File fW, long startPointer, int block) {
        try {
            this.rafR = new RandomAccessFile(fR, "r");

            this.rafW = new RandomAccessFile(fW, "rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 初始化开始位置
        this.startPointer = startPointer;
        this.by = new byte[block];
        this.block = block;
    }


    @Override
    public void run() {
        // 线程名称
        byte[] name = ("\n" + Thread.currentThread().getName() + "\n").getBytes();

        try {
            // 判断文件指针是否合法
            if (startPointer >= 0 && startPointer <= rafR.length()) {
                // 设置文件指针的偏移
                rafR.seek(startPointer);
                // 为每个线程设置多余的指针偏移
                // 开始位置为：  指针增加的偏移量为 开始位置+线程名称长度*开始的位置/一次读取的长度

                // 设 读取长度=1024 线程名长度 = 10
                // 例如：
                // 开始位置为1024*0 指针增加的偏移量为 1024*0+10*0/1024
                // 开始位置为1024*1 指针增加的偏移量为 1024*1+10*1024/1024
                // 开始位置为1024*2 指针增加的偏移量为 1024*2+10*1024*2/1024
                // 开始位置为1024*3 指针增加的偏移量为 1024*3+10*1024*3/1024
                // ……………………
                rafW.seek(startPointer + name.length * startPointer / block);
            }


            int len;
            // 读一块内容
            if ((len = rafR.read(by)) != -1) {
                // 打印线程名称（如果加上此行则会乱码，去掉就不会乱码）
                rafW.write(name, 0, name.length);

                // 写入文件
                rafW.write(by, 0, len);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                rafR.close();
                rafW.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
