public class XP implements Comparable<XP>{

    private int[] num;
    private int numDigits;
    private static final int MAX_LENGTH = 100;
    private static final int ALLOCATED_LEN = 2 * MAX_LENGTH + 1;

    private XP(int[] digits) {
        if(digits.length != ALLOCATED_LEN) {
            throw new IllegalArgumentException("Incorrect num digits");
        }
        num = digits;
        numDigits = countDigits();
    }

    private XP(String digits, boolean initial) {
        if(initial && digits.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Too many digits: " + digits.length() + " > " + MAX_LENGTH);
        }

        num = new int[ALLOCATED_LEN];
        if(digits.length() == 0) {
            num[0] =  0;
        }
        else {
            setNum(digits);
        }
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
        if(i == -1) return 1;
        return i + 1;
    }

    public boolean isOdd() {
        return num[0] % 2 == 1;
    }

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

    public XP mult(XP n) {
        return new XP("" + karatsuba(this,n), false);
    }

    //XP version
    //works, but really ugly code below
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
        return temp(z2,(ex * 2)).add(temp(z1.sub(z2).sub(z0),ex)).add(z0);
    } 

    private XP temp(XP z, int exp){
        String x = "";
        for(int i = 0; i < exp; i++) {
            x += 0;
        }
        for(int j = 0; j < z.numDigits; j++) {
            x = z.num[j] + x;
        }
        return new XP(x, false);
    }

    //TODO:
    //UNDERSTAND WHY THIS ALGORITHM WORKS???
    //TEST
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
