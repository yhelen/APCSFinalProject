public class RSA {

    private int[] privateKey;
    private int[] publicKey;

    // Keys are generated with the following formula:
    // First two distinct prime numbers, p and q, are picked
    //   p = 97 and q = 107
    // Then the product of p & q is computed, n
    //   p * q = n = 10379
    // Then the totient of the product is computed
    // Which can also be computed as the Least Common Multiple
    //  of p-1 and q-1
    //   totient(n) = totient(10379) = LCM(p-1,q-1) = LCM(96,106) = 10176
    // Then any number coprime to totient(n) is chosen, e
    //   e = 7
    // Then find the modular multiplicative inverse of e(mod(totient(n))), d
    //   d = 5815
    // The private key array is {d,n}
    // The public key array is {e,n}
    // The encrypter can only encrypt integers/XPs from 0 inclusive
    //  to n exclusive, [0,n)
    // This could be used to generate bigger keys for a larger allowed range of
    //  numbers for encryption, but would result in a longer runtime
    public RSA() {
        privateKey = new int[]{5815,10379};
        publicKey = new int[]{7,10379};
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

    //same methods but for XPs
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
