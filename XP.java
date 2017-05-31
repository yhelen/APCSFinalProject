public class XP implements Comparable<XP>{

    private int[] num;
    private int numDigits;
    private static final int MAX_LENGTH = 20;
    private static final int ALLOCATED_LEN = 2 * MAX_LENGTH + 1;

    private XP(int[] digits) {
        if(digits.length != ALLOCATED_LEN) {
            throw new IllegalArgumentException("Incorrect num digits");
        }
        num = digits;
        numDigits = countDigits();
    }

    private XP(String digits, boolean initial) {
        if(digits.length() == 0) {
            throw new IllegalArgumentException("No number inputted");
        }
        
        if(initial && digits.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Too many digits: " + digits.length() + " > " + MAX_LENGTH);
        }
        
        num = new int[ALLOCATED_LEN];
        setNum(digits);
        numDigits = countDigits();
    }

    public XP(String digits) {
        this(digits, true);
    }

    private void setNum(String digits) {
        int strInd = digits.length();
        int i = 0;
        while(strInd > 0) {
            num[i] = Integer.parseInt(digits.substring(strInd - 1, strInd));
            strInd--;
            i++;
        }
    }

    private int countDigits() {
        int i = ALLOCATED_LEN - 1;
        while(i >= 0 && num[i] == 0) {
            i--;
        }
        return i + 1;
    }

    public boolean isOdd() {
        return num[0] % 2 == 1;
    }

    public XP add(XP n) {
        if(this.getNumDigits() > MAX_LENGTH || n.getNumDigits() > MAX_LENGTH) {
            throw new IllegalArgumentException("Numbers too big");
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

    public XP sub(XP n) {
        if(this.compareTo(n) < 0) {
            throw new IllegalArgumentException("input must be less than this number");
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

    public XP mult(XP n) {
        String num1 = "";
        for(int i = 0; i < this.numDigits; i++){
            num1 = this.num[i] + num1;
        }
        String num2 = "";
        for(int j = 0; j < n.numDigits; j++){
            num2 = n.num[j] + num2;
        }
        int a = Integer.parseInt(num1);
        int b = Integer.parseInt(num2);
        return new XP("" + karatsuba(a,b));
        //for XP version later
        //	return new XP("" + karatsuba(this,n));
    }

    //using integers
    //I think it works but should be tested more
    private int karatsuba(int n1, int n2){
        //	System.out.println(n1 + " " + n2);
        if(n1 < 10 || n2 < 10)
            return n1 * n2;
        String a = "" + n1;
        String b = "" + n2;
        int exp = Math.max(a.length(),b.length());
        int ex = (Math.round(exp/2));
        int low1 = Integer.parseInt(a.substring(a.length() - ex, a.length()));
        int high1 = Integer.parseInt(a.substring(0,a.length() - ex));
        int low2 = Integer.parseInt(b.substring(b.length() - ex, b.length()));
        int high2 = Integer.parseInt(b.substring(0,b.length()-ex));
        int z0 = karatsuba(low1,low2);
        int z1 = karatsuba((low1+high1),(low2+high2));
        int z2 = karatsuba(high1,high2);
        return (int)((z2*Math.pow(10,(ex * 2)))+((z1-z2-z0)*Math.pow(10,ex))+z0);
    }

    //XP version
    //work in progress, really ugly code below
    //and I can't really explain it b/c it doesnt work
    private XP karatsuba(XP a, XP b){
        if(a.numDigits <= 1 || b.numDigits <= 1) {
            return new XP("" + a.num[0] * b.num[0]);
        }
        int exp = Math.min(a.numDigits,b.numDigits);
        int ex = exp  / 2;
        System.out.println("" + a + " " + b);
        String h1 = "";
        for(int i = a.numDigits - 1; i > ex-1; i--) {
            h1 += a.num[i];
        }
        XP high1 = new XP(h1);
        System.out.println("h1:" + h1);
        String l1 = "";
        for(int j = ex-1; j >= 0; j--) {
            l1 += a.num[j];
        }
        XP low1 = new XP(l1);
        System.out.println("l1:" + l1);
        String h2 = "";
        for(int k = b.numDigits - 1; k > ex-1; k--) {
            h2 += b.num[k];
        }
        XP high2 = new XP(h2);
        System.out.println("h2:" + h2);
        String l2 = "";
        for(int l = ex-1; l >= 0; l--) {
            l2 += b.num[l];
        }
        XP low2 = new XP(l2);
        System.out.println("l2:" + l2);
        XP lows = low1.add(high1);
        System.out.println("Low1:" + low1 + " high1:" + high1 + " Lows:" + lows);	
        XP highs = low2.add(high2);	
        System.out.println("Low2:" + low2 + " high2:" + high2 + " Highs:" + highs);
        XP z0 = karatsuba(low1,low2);
        System.out.println(z0);
        XP z1 = karatsuba(lows,highs);
        System.out.println(z1);
        XP z2 = karatsuba(high1,high2);
        System.out.println(z2);
        //return (int)((z2*Math.pow(10,(exp)))+((z1-z2-z0)*Math.pow(10,ex))+z0);
        return new XP("0");
    }

    //TODO:
    //UNDERSTAND WHY THIS ALGORITHM WORKS???
    //TEST -> CANNOT BE TESTED ATM BC MULT DOESN'T WORK YET
    private XP[] division(XP a, XP b) {
        if(a.compareTo(b) < 0) {
            return new XP[]{new XP("0"),a};
        }
        XP two = new XP("2");
        XP[] temp = division(a, karatsuba(a,b.mult(two)));
        temp[0] = karatsuba(temp[0],two);
        if(temp[1].compareTo(b) >= 0) {
            temp[0] = temp[0].add(new XP("1"));
            temp[1] = temp[1].sub(b);
        }
        return temp;
    }

    public XP div(XP n) {
        return division(this, n)[0];
    }

    public XP mod(XP n) {
        return division(this, n)[1];
    }

    public int getNumDigits() {
        return numDigits;
    }

    // TODO
    // TEST FURTHER
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
