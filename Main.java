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
        System.out.println("Creating XP: " + s);
        return new XP(s);
    }

    public static void main(String[] args) {
        /*        XP a = rand();
                  XP b = rand();
                  System.out.println("a: " + a);
                  System.out.println("b: " + b);
                  XP sum = a.add(b);
                  System.out.println("sum: " + sum);
                  System.out.println("diff: " + sum.sub(a));
                  System.out.println("multi: " + a.mult(b));
                  System.out.println("div: " + a.div(b));
                  System.out.println("mod: " + a.mod(b));
                  System.out.println("a.getNumDigits: " + a.getNumDigits());
                  System.out.println("a.compareTo: " + a.compareTo(b));
                  */
        /*   System.out.println("Encryption & decryption example: 2003")
	     System.out.println("Using integers:");
	     RSA rsa = new RSA();
             int enc = rsa.encrypt(2003);
             System.out.println(enc);
             System.out.println(rsa.decrypt(enc));

	     System.out.println("Using XPs:");
             XP en = rsa.encrypt(new XP("2003"));
             System.out.println(en);
             System.out.println(rsa.decrypt(en));
             */

        System.out.println("Enter a short message (<30 characters): ");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        System.out.println("Your message is " + message);

        Message m = new Message(message);
        // System.out.println(m.getMsg());
        XP[] ar = m.encryptMessage();
        String s = "";
        for(int i = 0; i < m.size(ar); i++){
            s += ar[i];
        }
        System.out.println("Your encrypted message is: " + s);
        System.out.println("Your decrypted message is...");
        System.out.println("Sorry, you're going to have to give me a while. :)");
        String ar2 = m.decryptMessage(ar);
        System.out.println("It's: " + ar2);

    }

}
