package firstHomework;

public class Animal {

    public String name;
    public String sex;
    public String type;
    public int age;

    @Override
    public String toString() {
        return "种类：" + type + " \t" + "姓名：" + name + " \t" + "性别：" + sex + " \t" + "年龄" + age;
    }

    // 继承方法(自我介绍)
    public void selfIntroduction() {
    }

    // 继承方法（工作）
    public void work() {
    }

    public Animal() {
    }

    // @Override
    // public boolean equals(Object obj) {
    //     return super.equals(obj);
    // }

    public Animal(String type, String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.type = type;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return this.sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
