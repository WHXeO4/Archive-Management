package main;

import main.controller.UserController;
import main.entity.Result;
import main.entity.User;
import main.util.State;
import org.ibex.nestedvm.Runtime;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Deprecated
public class DocumentManagementSystemImpl {
    static State state = State.GREETING_PAGE;
    final static List<String> ROLE_LIST = new ArrayList<>();
    static {
        ROLE_LIST.add("Administrator");
        ROLE_LIST.add("Browser");
        ROLE_LIST.add("Operator");
    }
    static boolean running = true;
    static User current;
    public static void app() throws IOException, IllegalAccessException {
        welcome();
        while (running) {
            while (state.equals(State.GREETING_PAGE)) {
                greetingPage();
            }
            while (state.equals(State.MAIN_PAGE)) {

            }
        }
    }

    public static void welcome() {
        System.out.print("欢迎使用\n " +
                "-----------------------------------\n");
    }

    public static void greetingPage() throws IOException, IllegalAccessException {
        System.out.print("1. 注册\n" +
                "2. 登录\n" +
                "3. 以访客身份进入\n" +
                "4. 退出\n" +
                ">>>");
        judge((char) System.in.read());
    }

    public static void judge(char c) throws IllegalAccessException, IOException {
        switch (c) {
            case '1': register(); break;
            case '2': login(); break;
            case '3': browserAccess(); break;
            case '4': quit(); break;
        }
    }

    public static void register() throws IllegalAccessException, IOException {
        String name;
        String password;
        String role;
        System.in.read();

        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入用户名: ");
            name = scanner.nextLine();

            System.out.println("请输入密码: ");
            password = scanner.nextLine();

            System.out.print("请选择你的身份: \n" +
                    "1. 管理员\n" +
                    "2. 操作员\n" +
                    "3. 访客\n" +
                    ">>>");
            role = ROLE_LIST.get(scanner.nextInt() - 1);
            scanner.nextLine(); // 消费掉 nextInt() 后的换行符

            Result result = UserController.register(name, password, role);
            System.out.println(result.getMessage());

            if (result.getCode()==0) break;
        }
    }

    public static void login() throws IOException {
        String name;
        String password;
        String role;
        System.in.read();
        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) scanner.nextLine();
        System.out.println("请输入用户名: ");
        name = scanner.nextLine();

        System.out.println("请输入密码: ");
        password = scanner.nextLine();

        Result result = UserController.login(name, password);
        if (result.getCode()==0) {
            state = State.MAIN_PAGE;
            current = (User) result.getData();
        } else {
            System.out.println(result.getMessage());
        }
    }

    public static void browserAccess() {

    }

    public static void quit() {
        System.out.println("Bye");

        running = false;
    }
}
