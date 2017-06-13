// Stores large numbers, and performs basic arithmetic operations
// + , - , * , / , % and comparison
public class XP implements Comparable<XP>{

    // Array to store all the digits of the number
    // Index 0 is the ones digit, 1 is the tens, 2 is the hundreds, etc...
    private int[] num;
    private int numDigits;
    // The max allowed digits when initiated externally
    // Let this variable equal N
    private static final int MAX_LENGTH = 100;
    // The max allocated digits (max digits when adding 2N digit nums)
    private static final int ALLOCATED_LEN = 2 * MAX_LENGTH + 1;

    // Constructor to set an XP equal to an array of digits
    // Checks that all digits are, in fact, digits
    private XP(int[] digits) {
        if(digits.length != ALLOCATED_LEN) {
            throw new IllegalArgumentException("Incorrect num digits");
        }

        for(int x: digits) {
            if(x < 0 || x > 9) {
                throw new IllegalArgumentException("Input array has a number < 0 or > 9");
            }
        }
        num = digits;
        numDigits = countDigits();
    }

    // Private constructor that takes a string of digits and adds them
    // to the digit array, but has a boolean input so that the public
    // constructor only accepts N digits, but can privately accept 2N+1
    private XP(String digits, boolean initial) {
        if(initial && digits.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Too many digits: " +
                    digits.length() + " > " + MAX_LENGTH);
        }

        num = new int[ALLOCATED_LEN];
        if(digits.length() != 0) {
            setNum(digits);
        }
        numDigits = countDigits();
    }

    public XP(String digits) {
        this(digits, true);
    }

    // Takes in an int and turns it into a XP
    public XP(int number) {
        this("" + number);
    }

    // Takes the string of digits and parses through, setting each digit
    // to its position in the array
    private void setNum(String digits) {
        int strInd = digits.length();
        int i = 0;
        while(strInd > 0) {
            num[i] = Integer.parseInt(digits.substring(strInd - 1, strInd));
            strInd--;
            i++;
        }
    }

    // Counts the number of digits by traversing down the array 
    // subtracting 1 from the allocated length for every leading zero
    private int countDigits() {
        int i = ALLOCATED_LEN - 1;
        while(i >= 0 && num[i] == 0) {
            i--;
        }
        // If the XP is equal to 0, i will be -1 after loop
        if(i == -1) {
            i++;
        }
        return i + 1;
    }

    // Adds two numbers by going through each digit and carrying over digits if
    // necessary
    public XP add(XP n) {
        if(this.getNumDigits() > 2 * MAX_LENGTH || n.getNumDigits() > 2 * MAX_LENGTH) {
            throw new IllegalArgumentException("Numbers too big--digits of this: " +
                    this.getNumDigits() + " or input: " + n.getNumDigits());
        }

        int[] sum = new int[ALLOCATED_LEN];
        for(int i = 0; i < ALLOCATED_LEN; i++) {
            int total = this.num[i] + n.num[i] + sum[i];
            if(total > 9) {
                sum[i+1] += total / 10;
            }
            sum[i] = total % 10;
        }
        return new XP(sum);
    }

    // Subtracts two numbers by going through each digit and "borrowing" digits
    // if necessary
    public XP sub(XP n) {
        if(this.compareTo(n) < 0) {
            throw new IllegalArgumentException("Input greater than this number");
        }

        int[] diff = new int[ALLOCATED_LEN];
        for(int i = 0; i < ALLOCATED_LEN; i++) {
            if(diff[i] + this.num[i] < n.num[i]) {
                diff[i + 1]--;
                diff[i] += 10;
            }
            int total = this.num[i] - n.num[i];
            diff[i] += total;
        }
        return new XP(diff);
    }

    // Returns the product of the two XPs
    public XP mult(XP n) {
        return new XP("" + karatsuba(this,n), false);
    }

    // Multiplies the two XPs using the karatsuba algorithm:
    // Find the number with the most digits
    // Take half of the number of digits of that number, ex
    // Split both numbers into two parts, using ex as the split point
    // Take the first part of both numbers and multiply them, z2
    // Take the second part of both numbers and multiply them, z0
    // Take the sum of both parts of the first number and 
    //   the sum of both parts of the second number
    //   and multiply them, z1
    // Add z2 multiplied by 10 raised to two times the splitting number (2 * ex)
    //   to (z1-z2-z0) multiplied by 10 raised to ex
    //   and z0 for the final result
    private XP karatsuba(XP a, XP b){
        if(a.numDigits <= 1 && b.numDigits <= 1) {	    
            return new XP("" + (a.num[0] * b.num[0]));
        }

        int exp = Math.max(a.numDigits,b.numDigits);
        int ex = (Math.round(exp  / 2));
        String h1 = "";
        for(int i = a.numDigits - 1; i >= ex; i--) {
            h1 += a.num[i];
        }
        XP high1 = new XP(h1);
        String l1 = "";
        for(int j = ex - 1; j >= 0; j--) {
            l1 += a.num[j];
        }
        XP low1 = new XP(l1);
        String h2 = "";
        for(int k = b.numDigits - 1; k >= ex; k--) {
            h2 += b.num[k];
        }
        XP high2 = new XP(h2);
        String l2 = "";
        for(int l = ex - 1; l >= 0; l--) {
            l2 += b.num[l];
        }

        XP low2 = new XP(l2);
        XP lows = high1.add(low1);
        XP highs = high2.add(low2);
        XP z0 = karatsuba(low1,low2);
        XP z1 = karatsuba(lows,highs);
        XP z2 = karatsuba(high1,high2);
        return helper(z2,(ex * 2)).add(helper(z1.sub(z2).sub(z0),ex)).add(z0);
    } 

    // Multiplies XP by 10 to the exp
    // By placing the ones digit after exp 0s
    private XP helper(XP z, int exp){
        String x = "";
        for(int i = 0; i < exp; i++) {
            x += 0;
        }
        for(int j = 0; j < z.numDigits; j++) {
            x = z.num[j] + x;
        }
        return new XP(x, false);
    }

    // Divides two XPS using the algorithm:
    // Check if the first is less than second, if so return [0, a]
    // Set [q,r] = division(a, 2 * b)
    // Multiply q by 2
    // If the remainder (r in [q,r]) is less than the divisor (b), return
    //      the current [q,r]
    // else add one to quotient and subtract the divisor from the remainder
    //      and return that
    private XP[] division(XP a, XP b) {
        if(a.getNumDigits() > MAX_LENGTH * 2 || b.getNumDigits() > MAX_LENGTH * 2) {
            throw new IllegalArgumentException("Numbers too big--digits of dividend: " +
                    a.getNumDigits() + "or divisor: " + b.getNumDigits());
        }

        if(a.compareTo(b) < 0) {
            return new XP[]{new XP("0"),a};
        }

        XP two = new XP("2");
        XP[] temp = division(a,b.mult(two));
        temp[0] = temp[0].mult(two);
        if(temp[1].compareTo(b) >= 0) {
            temp[0] = temp[0].add(new XP("1"));
            temp[1] = temp[1].sub(b);
        }
        return temp;
    }


    // Returns the quotient, the first element returned by the division method
    public XP div(XP n) {
        return division(this, n)[0];
    }

    // Returns the remainder, the second element returned by the division method
    public XP mod(XP n) {
        return division(this, n)[1];
    }

    public int getNumDigits() {
        return numDigits;
    }

    // Compares two XPs by:
    // comparing the number of digits
    // then if they have the same number of digits
    // comparing each digit starting from the greatest
    public int compareTo(XP b) {
        if(this.getNumDigits() != b.getNumDigits()) {
            return this.getNumDigits() - b.getNumDigits();
        }

        for(int i = this.getNumDigits() - 1; i >= 0; i--) {
            if(this.num[i] != b.num[i]) {
                return this.num[i] - b.num[i];
            }
        }

        return 0;
    }

    public String toString() {
        String ans = "";
        int i = getNumDigits() - 1;
        while(i >= 0) {
            ans += num[i];
            i--;
        }
        return ans;
    }

}
