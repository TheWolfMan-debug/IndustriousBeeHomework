package firstHomework;

public class Cat extends Animal {

    @Override
    public void selfIntroduction() {
        System.out.println(
                "我是一小只" + this.type + "，我的名字叫" + this.name + "，我的性别是 " + this.sex + "，我" + this.age + "岁了，" + "我最爱吃小鱼");
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Cat ob = (Cat) obj;
        return ob.getName().equals(name) && ob.getSex().equals(sex) && ob.getAge() == age&& ob.getType().equals(type);
    }

    public Cat(String type, String name, String sex, int age) {
        super(type, name, sex, age);
    }

}