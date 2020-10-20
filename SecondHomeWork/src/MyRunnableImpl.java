import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MyRunnableImpl implements Runnable {

    private final byte[] by;
    // 设置开始位置的偏移
    private long startPointer;
    // 创建随机流
    private RandomAccessFile rafR;
    private RandomAccessFile rafW;


    // 接收两个随机流文件参数，一个位置偏移参数
    public MyRunnableImpl(File fR, File fW, long startPointer, int endPointer) {
        try {
            this.rafR = new RandomAccessFile(fR, "r");
            this.rafW = new RandomAccessFile(fW, "rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 初始化开始位置
        this.startPointer = startPointer;
        this.by = new byte[endPointer];
    }


    @Override
    public void run() {

        try {
            // 判断文件指针是否合法
            if (startPointer >= 0 && startPointer <= rafR.length()) {
                // 设置文件指针的偏移
                rafR.seek(startPointer);
                rafW.seek(startPointer);
            }

            // 线程名称
            byte[] name = ("\n" + Thread.currentThread().getName() + "\n").getBytes();

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
