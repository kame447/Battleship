import java.util.Scanner;

class Input {
    public static int intInput() {
        int x = GetValue.getInt();
        Confirmation(x);
        return x;
    }

    public static char charInput() {
        char x = GetValue.getChar();
        Confirmation(x);
        return x;
    }

    public static String strInput() {
        String x = GetValue.getStr();
        Confirmation(x);
        return x;
    }

    public static Object Confirmation(Object x) {
        while (true) {
            System.out.print(x + "を入力します YES(y) or NO(n): ");
            char decision = GetValue.getChar();
            if (decision == 'y') {
                break;
            } else {
                System.out.print("\n値をもう一度入力してください: ");
                switch(x.getClass().getSimpleName()) {
                    case "Integer":
                        x = GetValue.getInt();
                        break;
                    case "char":
                        x = GetValue.getChar();
                        break;
                    case "String":
                        x = GetValue.getStr();
                        break;
                }
            }
        }
        return x;
    }
}

class GetValue {
    static Scanner sc = new Scanner(System.in);
    public static int getInt() {
        int i = sc.nextInt();
        return i;
    }

    public static char getChar() {
        char i = sc.next().charAt(0);
        return i;
    }

    public static String getStr() {
        String i = sc.next();
        return i;
    }
}
