package model;

public class User {
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "User{name=\"" + name + "\", age=\"" + age + "\"}";
    }
}
