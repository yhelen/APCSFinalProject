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
        String sum = "";
        //broken??
        for(int i = 0; i <= Math.max(this.getNumDigits(), n.getNumDigits()); i++) {
            int total = this.num[i] + n.num[i];
            if(total > 9) {
                this.num[i + 1] += total / 10;
            }
            sum = total % 10 + sum;
        }
        return new XP(sum, false);
    }

    // TODO
    // Doesn't work --> 
    public XP sub(XP n) {
        //if(this.compareTo(n) < 0) {
        //    throw new IllegalArgumentException("input must be less than this number");
        //}

        String diff = "";
        for(int i = 0; i < ALLOCATED_LEN; i++) {
            if(this.num[i] < n.num[i]) {
                this.num[i + 1]--;
                this.num[i] += 10;
            }
            int total = this.num[i] - n.num[i];
            diff = total + diff;
        }
        return new XP(diff,false);
    }

    public XP mult(XP n) {
        return null;
    }
    
    public XP div(XP n) {
        return null;
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
