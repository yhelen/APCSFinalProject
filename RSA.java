public class RSA {

    private int[] privateKey;
    private int[] publicKey;

    //public[1] && private[1] MUST BE EQUAL
    //TODO
    // KEY GENERATION?
    public RSA() {
        privateKey = new int[]{2563,3713};
        publicKey = new int[]{7,3713};
    }

    // Modular exp:
    // Based off the principle that
    // (x * y) mod m = ((x mod m) * (y mod m) mod m)
    // to compute b^e mod m (represented by c)
    // 1. c = 1, e' = 0
    // 2. e' += 1
    // 3. c = (b * c) mod m
    // 4. if e' < e step 2 else terminate return c
    public int modEx(XP b, int e, int m) {
        XP c = new XP("1");
        for(int i = 0; i < e; i++) {
            c = b.mult(c).mod(new XP("" + m));
        }
        return Integer.parseInt(c.toString());
    }

    // The encryption algorithm works as so:
    // Given a public key (e,n)
    // to get E(M), the encrypted message:
    // E(M) = M ^ e mod n
    public int encrypt(int input) {
        return modEx(new XP("" + input), publicKey[0], publicKey[1]);
    }

    // The decryption algorithm works as so:
    // Given a private key (d,n) and encrypted message E(M)
    // to get M, the decrypted message:
    // M = E(M) ^ d mod n
    public int decrypt(int input) {
        return modEx(new XP("" + input), privateKey[0], privateKey[1]);
    }

    //for XPs
    public XP modExXP(XP b, int e, int m){
        XP c = new XP("1");
        for(int i = 0; i < e; i++) {
            c = b.mult(c).mod(new XP("" + m));
        }
        return c;
    }

    public XP encrypt(XP input){
        return modExXP(input, publicKey[0], publicKey[1]);
    }

    public XP decrypt(XP input){
        return modExXP(input, privateKey[0],privateKey[1]);
    }

}
