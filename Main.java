import java.util.Scanner;

public class Main {

    // Generates a random XP
    public static XP rand() {
        String s = "";
        int len = (int) (Math.random() * 100);
        while(len > 0) {
            s += "" + (int) (Math.random() * 10);
            len--;
        }
        return new XP(s);
    }

    public static void main(String[] args) {
        XP a = rand();
        XP b = rand();
        XP sum = a.add(b);

        RSA rsa = new RSA();
        int enc = rsa.encrypt(2003);
        XP en = rsa.encrypt(new XP("2003"));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Testing the creation and arithmetic with random XPs");
        System.out.println("<================>");
        System.out.println("a: " + a);
        System.out.println("b: " + b);
        System.out.println("<================>");
        System.out.println("a + b: " + sum);
        System.out.println("a + b - a (should be b): " + sum.sub(a));
        System.out.println("a * b: " + a.mult(b));
        System.out.println("a / b: " + a.div(b));
        System.out.println("a % b: " + a.mod(b));
        System.out.println("<================>");
        System.out.println("a.getNumDigits: " + a.getNumDigits());
        System.out.println("a.compareTo: " + a.compareTo(b));

        System.out.println("<================>");
        System.out.println("Encryption & decryption example: 2003");
	System.out.println("This may take a few seconds.");
        System.out.println("<================>");
        System.out.println("Using integers:");
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted: " + rsa.decrypt(enc));
        System.out.println("<================>");
        System.out.println("Using XPs:");
        System.out.println("Encrypted: " + en);
        System.out.println("Decrypted: " + rsa.decrypt(en));

        System.out.println("\n\nASCII encryption");
        System.out.println("<================>");
        System.out.println("Enter a short message (<30 characters): ");
        String message = scanner.nextLine();
        System.out.println("Your message is: " + message);

        Message m = new Message(message);
        XP[] ar = m.encryptMessage();
        String s = "";
        for(int i = 0; i < m.size(ar); i++){
            s += ar[i];
        }
        System.out.println("Your encrypted message is: " + s);
	System.out.println("Your decrypted message is... ");
        System.out.println("Sorry, you're going to have to give me a while. :)");
        String ar2 = m.decryptMessage(ar);
        System.out.print("Your decrypted message is: ");
        System.out.println(ar2);

    }

}
