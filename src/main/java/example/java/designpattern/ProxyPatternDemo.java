package example.java.designpattern;

interface UserService {

    void load();

    void insert();
}

class UserServiceImpl implements UserService {

    private String name;

    public UserServiceImpl(String name) {
        this.name = name;
    }

    @Override
    public void load() {
        System.out.println(name + " can load");
    }

    @Override
    public void insert() {
        System.out.println(name + " can insert");
    }

}

class UserServiceProxy implements UserService {

    private String role;
    private UserService userService;

    public UserServiceProxy(String name, String role) {
        this.role = role;
        userService = new UserServiceImpl(name);
    }

    @Override
    public void load() {
        userService.load();
    }

    @Override
    public void insert() {
        if ("admin".equalsIgnoreCase(this.role)) {
            userService.insert();
        } else {
            throw new IllegalAccessError("Access denied");
        }
    }
}

public class ProxyPatternDemo {
    public static void main(String[] args) {
        UserService admin = new UserServiceProxy("Admin", "admin");
        admin.load();
        admin.insert();
 
        UserService customer = new UserServiceProxy("customer", "guest");
        customer.load();
        customer.insert();
}
}
