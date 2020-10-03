package firstHomework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
// import java.util.Iterator;
import java.util.Scanner;

/**
 * firstHomework
 */

/**
 * 将动物信息（种类、性别、年龄）保存在集合中（可以先保存一部分，再添加新的；也可以后面录入。
 * 在这里要不同的动物可能会有不同的属性）。在控制台输入指令，系统可以对动物信息进行基本的
 * 增加、删除、修改、查询对年龄进行排序然后输出。像C语言题库里面的那种菜单式的系统，如输入1进行 XXX操作。
 * 
 * 考察内容：面向对象、泛型、异常、集合
 * 
 * 请在github上面新建公开仓库，然后上传你的代码，然后私发链接，姓名、序号给我。
 * 
 */

public class AnimalManagerSystem {

    public static void main(final String[] args) throws IOException {
        // 创建一个集合存储动物
        ArrayList<Animal> animals = new ArrayList<Animal>();
        // 创建文件对象
        File f1 = new File("animalManger.txt");
        // 创建一个输入流
        Scanner sc = new Scanner(System.in);

        // 加载文件
        loadFile(animals, f1);
        // op记录用户选择
        int op = -1;

        // while循环
        while (true) {
            // 展示菜单
            showMenu();

            // 用户输入选择
            op = sc.nextInt();

            switch (op) {
                // 退出系统
                case 0:
                    System.out.println("欢迎下次使用！！！");

                    // 关闭输入流
                    sc.close();
                    return;
                // 添加动物
                case 1:
                    addAnimal(animals, sc);
                    break;
                // 查看动物(需要对年龄进行排序后输出)
                case 2:
                    showAnimal(animals);
                    break;
                // 查询动物
                case 3:
                    searchAnimal(animals, sc);
                    break;
                case 4:
                    // 查看动物详细信息
                    getInfoAnimal(animals, sc);
                    break;
                // 修改动物
                case 5:
                    modifyAnimal(animals, sc);
                    break;
                // 删除动物
                case 6:
                    deleAnimal(animals, sc);
                    break;
                // 清空动物
                case 7:
                    clearAnimal(animals, sc);
                    break;
                default:
                    System.out.println("输入错误！请重新输入！");
                    break;
            }
            /**
             * 循环结束保存后数据，保证每一次的操作都能记录下来
             */
            saveData(animals, f1);
        }
    }

    // 清空动物列表
    private static void clearAnimal(ArrayList<Animal> animals, Scanner sc) {
        if(animals.isEmpty())
        {
            System.out.println("已经是空的！");
            return;
        }
        System.out.println("请确认您是否要清空动物系统(Y/N):");
        
        char ch = sc.next().charAt(0);
        if (ch == 'Y') {
            animals.clear();
            System.out.println("操作成功！！！");
        } else {
            System.out.println("取消操作成功！！！");
            return;
        }
    }

    // 保存所有数据
    private static void saveData(ArrayList<Animal> animals, File f1) throws IOException {
        // File f1 = new File("animalManger.txt");
        // 先将原文件删除
        f1.delete();
        // 创建一个新文件
        f1.createNewFile();
        // 遍历动物集合，将每个动物保存在animalManger.txt文件中
        for (Animal animal : animals) {
            // 创建缓冲输出流
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1, true)))) {
                // 将文件以#xx#xx#的格式保存，方便读取
                bw.write(animal.type + "#" + animal.name + "#" + animal.sex + "#" + animal.age + "#" + "\r\n");
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }

    }

    // 详细信息
    private static void getInfoAnimal(ArrayList<Animal> animals, Scanner sc) {
        // 判断集合是否空
        if (animals.isEmpty()) {
            System.out.println("没有动物信息！！！");
            return;
        } else {
            boolean flag = false;
            // 刷新回车
            sc.nextLine();
            System.out.println("请输入想要查看动物的信息的姓名：");
            String infoName = sc.nextLine();
            // 遍历集合，查找指定动物
            for (Animal animal : animals) {
                if (animal.getName().equals(infoName)) {
                    // 显示动物特有的方法
                    animal.selfIntroduction();
                    flag = true;
                }
            }
            // 如果没有找到，则直接返回
            if (!flag) {
                System.out.println("没有此动物！！！");
                return;
            }
        }
    }

    // 将文件加载到内存中
    private static void loadFile(ArrayList<Animal> animals, File f1) throws IOException {
        // 判断文件是否存在
        if (!f1.exists()) {
            // 如果不存在，则创建一个文件
            f1.createNewFile();
            return;
        }
        // 创建一个字符缓冲输入流
        try (BufferedReader br = new BufferedReader(new FileReader("animalManger.txt"))) {
            String line;
            // 将文件加载到内存中
            while ((line = br.readLine()) != null) {
                // 使用“#”分割每一行的字符串
                String[] animalArray = line.split("#");
                // 判断动物的种类
                if (animalArray[0].equals("猫")) {
                    animals.add(
                            new Cat(animalArray[0], animalArray[1], animalArray[2], Integer.parseInt(animalArray[3])));
                } else if (animalArray[0].equals("狗")) {
                    animals.add(
                            new Dog(animalArray[0], animalArray[1], animalArray[2], Integer.parseInt(animalArray[3])));
                } else if (animalArray[0].equals("猪")) {
                    animals.add(
                            new Pig(animalArray[0], animalArray[1], animalArray[2], Integer.parseInt(animalArray[3])));
                }

            }
        } catch (Exception e) {
            System.out.println(e);
            return;

        }

    }

    // 删除动物
    private static void deleAnimal(ArrayList<Animal> animals, Scanner sc) {
        // 判断集合是否为空
        if (animals.isEmpty()) {
            System.out.println("没有动物信息！！！");
            return;
        }
        // 清空缓冲区
        sc.nextLine();
        // 得到动物的种类
        String type = getType(sc);

        System.out.println("请输入要删除的动物的姓名：");
        String deleName = sc.nextLine();
        for (Animal animal : animals) {
            // 定位将要删除的动物
            if (animal.getType().equals(type) && animal.getName().equals(deleName)) {
                animals.remove(animal);
                System.out.println("删除成功！！！");
                return;
            }
        }
        System.out.println("删除失败！！！没有找到该动物！！！");

    }

    // 修改信息
    private static void modifyAnimal(ArrayList<Animal> animals, Scanner sc) throws IOException {
        // 判断集合是否为空
        if (animals.isEmpty()) {
            System.out.println("没有动物信息！！！");
            return;
        }
        char ch = 'N';
        boolean flag = false;
        sc.nextLine();

        String type = getType(sc);

        System.out.println("请输入修改动物的姓名、性别、年龄（用空格隔开）：");

        // 输入动物的基本信息
        String name;
        String sex;
        int age;
        String info = sc.nextLine();
        // 将输入字符串用空格分开
        String[] infoArry = info.split(" ");

        name = infoArry[0];
        sex = infoArry[1];
        age = Integer.parseInt(infoArry[2]);

        for (Animal animal : animals) {
            // 判断是否相等
            if (animal.getType().equals(type) && animal.getName().equals(name) && animal.getSex().equals(sex)
                    && animal.getAge() == age) {
                System.out.println("已找到:");
                System.out.println(animal);
                System.out.println("确认修改？（Y/N）");

                ch = sc.next().charAt(0);
                if (ch == 'Y') {
                    animals.remove(animal);
                    sc.nextLine();
                    flag = true;
                    addAnAnimal(animals, sc);
                    System.out.println("修改成功！！！");
                    break;
                }

            }

        }
        if (!flag) {
            System.out.println("此动物不存在！！！");
        }

    }

    // 得到输入的种类
    private static String getType(Scanner sc) {
        System.out.println("请输入动物种类(a->猫、b->狗、c->猪)：");
        char sh = sc.nextLine().charAt(0);

        String type = " ";
        switch (sh) {
            case 'a':
                type = "猫";

                break;
            case 'b':
                type = "狗";
                break;
            case 'c':
                type = "猪";
                break;
            default:
                System.out.println("输入错误！");
                break;
        }
        return type;
    }

    // 查找动物
    private static void searchAnimal(ArrayList<Animal> animals, Scanner sc) {
        // 判断集合是否为空
        if (animals.isEmpty()) {
            System.out.println("没有动物信息！！！");
            return;
        }
        boolean flag = false;
        System.out.println("请输入想要查找的动物的姓名：");
        sc.nextLine();
        String searchName = sc.nextLine();
        for (Animal animal : animals) {
            if (animal.getName().equals(searchName)) {
                System.out.println(animal);
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("没有此动物！！！");
        }

    }

    // 查看动物
    private static void showAnimal(ArrayList<Animal> animals) {
        // 判断集合是否为空

        if (animals.isEmpty()) {
            System.out.println("没有动物信息！！！");
            return;

        }
        // sort方法，重写Comparator
        Collections.sort(animals, new Comparator<Animal>() {
            @Override
            public int compare(Animal o1, Animal o2) {
                if (o1.getAge() >= o2.getAge()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        // 遍历输出
        for (Animal animal : animals) {
            System.out.println(animal);
        }

    }

    // 添加动物
    private static void addAnimal(ArrayList<Animal> animals, Scanner sc) {
        // 缓冲一下
        sc.nextLine();
        addAnAnimal(animals, sc);

    }

    // 添加一个动物
    private static void addAnAnimal(ArrayList<Animal> animals, Scanner sc) {
        // 判断添加的动物类型
        String type = getType(sc);
        char ch = type.charAt(0);
        String name;
        String sex;
        int age;
        sex = getSex(sc);
        sc.nextLine();

        System.out.println("请依次输入动物的姓名、年龄，以空格隔开");

        // 输入动物的基本信息
        String info = sc.nextLine();
        String[] infoArry = info.split(" ");
        name = infoArry[0];
        age = Integer.parseInt(infoArry[1]);

        // 判断动物是否存在
        for (Animal animal : animals) {
            if (animal.getType().equals(type) && animal.getName().equals(name) && animal.getSex().equals(sex)
                    && animal.getAge() == age) {
                System.out.println("添加失败！！！该动物已存在！");
                return;
            }
        }

        switch (ch) {
            case '猫':
                Animal aa = new Cat(type, name, sex, age);
                animals.add(aa);
                break;
            case '狗':
                Animal ba = new Dog(type, name, sex, age);
                animals.add(ba);

                break;
            case '猪':
                Animal ca = new Pig(type, name, sex, age);
                animals.add(ca);

                break;
            default:
                System.out.println("输入错误！！！");
                break;
        }
        System.out.println("添加成功！！！");

    }

    // 获取性别
    private static String getSex(Scanner sc) {
        System.out.println("请输入动物的性别(m:boy，f:girl)：");
        String s = sc.next();
        String sex = "boy";

        while (!s.equals("m") && !s.equals("f")) {
            System.out.println("输入错误！！！请重新输入！！！");
            s = sc.next();
        }
        if (s.equals("m")) {
            sex = "boy";
        } else if (s.equals("f")) {
            sex = "girl";
        }

        return sex;
    }

    // 展示菜单
    private static void showMenu() {
        System.out.println();
        System.out.println("\t动物信息管理系统");
        System.out.println();
        System.out.println("*******0--------------退出系统*******");
        System.out.println("*******1--------------添加动物*******");
        System.out.println("*******2--------------查看动物*******");
        System.out.println("*******3--------------查找动物*******");
        System.out.println("*******4--------------详细信息*******");
        System.out.println("*******5--------------修改动物*******");
        System.out.println("*******6--------------删除动物*******");
        System.out.println("*******7--------------清空动物*******");
    }

}
