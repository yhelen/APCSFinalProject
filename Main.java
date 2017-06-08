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
	/*   RSA rsa = new RSA();
	     int enc = rsa.encrypt(2003);
	     System.out.println(enc);
	     System.out.println(rsa.decrypt(enc));

	     XP en = rsa.encrypt(new XP("2003"));
	     System.out.println(en);
	     System.out.println(rsa.decrypt(en));
	*/

	System.out.println("Enter your short message: ");
	Scanner scanner = new Scanner(System.in);
	String message = scanner.nextLine();
	System.out.println("Your message is " + message);

        Message m = new Message(message);
	System.out.println(m.getMsg());
	System.out.println(m.getMsgasc());
        //	System.out.println(m.getMsgint());
	XP[] a = m.convertXP(m.getMsgasc());
	for(int i = 0; i < m.getSize(); i++){
	    System.out.print(a[i]);
	}
	
	System.out.println();
        System.out.println(m.getSize());
        XP[] ar = m.encrypt(m.getMsgarray());
	for(int i = 0; i < m.getSize(); i++){
	    System.out.print(ar[i]);
        }
	System.out.println();
        XP[] ar2 = m.decrypt(ar);
	for(int i = 0; i < m.getSize(); i++){
	    System.out.print(ar2[i]);
	}
	
        System.out.println();
	String deconvert = m.deconvert(ar2);
	System.out.println(m.deconvertString(deconvert));
    }

}
