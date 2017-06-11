// Encrypts and Decrypts messages
// Currently only for short messages <=30 characters for runtime purposes
// but could be implemented for longer messages 
public class Message extends RSA{

    // Stores the message
    private String _msg;
    // The maximum possible length of an XP array for a message with 30
    // should be 110
    // as each ascii number could have up to 3 digits
    // and each character has a 32 after it, 2 digits
    // resulting in a string with up to 150 characters
    // and would require 38 splits with 4 character splits
    // hypothetically, each 4 character split in stringToArray
    // could result in leading zeros, except for the first two splits,
    // which would require up to 3 array slots
    // first two splits: 1 array spot each
    // rest of the splits: 36 * 3
    // splitting and leading zeros explained in stringToArray
    private static final int LEN = 110;

    // constructor
    // checks that the message is less than or equal to 30 characters
    public Message(String msg){
        if(msg.length() > 30)
            throw new IllegalArgumentException("message is too long");
        _msg = msg;
    }

    // accessor methods
    public String getMsg(){
        return _msg;
    }

    // counts the number of occupied slots in the XP array
    // by incrementing the count for every slot that is not null
    public int size(XP[] msgarray){
        int count = 0;
        for(XP x: msgarray){
            if(x != null)
                count++;
            else
                break;
        }
        return count;
    }

    // converts string to XP array
    // splits the string into smaller strings of length 4
    // because the encrypter can currently only encrypt numbers up to 10379
    // then converts each smaller string into an XP and adds it to the array
    private XP[] stringToArray(String s){
        XP[] ans = new XP[LEN];
        // i is for adding it to the array
        // j is for spliting the string
        int i = 0;
        int j = 0;
        while(j < s.length() / 4){
            int lower = j * 4;
            // to account for the string starting with 0
            // as XP does not allow for leading zeros
            // a split could have up to two leading 0s as
            // the only ascii character with two zeros is d
            // with an ascii value of 100
            // an empty string creates an XP with value 0
            if(s.substring(lower,lower+2).equals("00")){
                ans[i] = new XP("");
                i++;
                ans[i] = new XP("");
                i++;
            }
            else if(s.substring(lower,lower+1).equals("0")){
                ans[i] = new XP("");
                i++;
            }
            XP x = new XP(s.substring(lower,lower+4));
            ans[i] = x;
            i++;
            j++;
        }
        // to account for left over characters
        if(s.length() % 4 != 0){
            int lower = j*4;
            // once again taking care of leading 0s
            if(s.length() > lower + 2 && s.substring(lower,lower+2).equals("00")){
                ans[i] = new XP("");
                i++;
                ans[i] = new XP("");
                i++;
            }
            else if(s.length() > lower + 1 && s.substring(lower,lower+1).equals("0")){
                ans[i] = new XP("");
                i++;
            }
            ans[i] = new XP(s.substring(lower));
        }
        return ans;
    }

    // returns a String of all the XPs in the array
    private String arrayToString(XP[] xps){
        String ans = "";
        int size = size(xps);
        for(int i = 0; i < size; i++){
            ans += xps[i];
        }
        return ans;
    }

    // returns the XP array of the encrypted message
    public XP[] encryptMessage(){
        return encryptMessage(_msg);
    }

    // encrypts the message
    // first converts the message to its modified ascii form
    // then creates an XP array of the ascii String
    // and returns it
    private XP[] encryptMessage(String msg){
        String ascii = convertascii(msg);
        XP[] xps = stringToArray(ascii);
        XP[] encrypted = encrypt(xps);
        return encrypted;
    }

    // converts String to a modified ascii String
    // with a 32 (the ascii for a space) after each character
    // to make the characters distinguishable for decrypting
    private String convertascii(String s){
        String ans = "";
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            ans += (int)c + "32";
        }
        return ans;
    }

    // encrypts the XP array
    // takes each XP of the array and encrypts it
    // to make a new array of encrypted XPs
    private XP[] encrypt(XP[] xps){
        int size = size(xps);
        XP[] ans = new XP[LEN];
        for(int i = 0; i < size; i++){
            XP temp = xps[i];		
            XP x = encrypt(temp);
            ans[i] = x;
        }
        return ans;
    }

    // decrypts the message
    // first decrypts the XPs of the given array to make a new XP array
    // then converts that XP array to a String
    // and converts that modified ascii String to the message
    public String decryptMessage(XP[] xps){
        XP[] decrypted = decrypt(xps);
        String msg = deconvertString(arrayToString(decrypted));
        return msg;
    } 

    // decrypts XP array
    // takes each XP of the array and decrypts it
    // to make a new array of decrypted XPs
    private XP[] decrypt(XP[] xps){
        XP[] ans = new XP[LEN];
        int size = size(xps);
        for(int i = 0; i < size; i++){
            XP temp = xps[i];
            XP x = decrypt(temp);
            ans[i] = x;
        }
        return ans;
    }

    // deconverts ascii String to message
    // splits the string by each 32 and converts each ascii number
    // to its corresponding character    
    private String deconvertString(String s){
        String ans = "";
        int split = s.indexOf("32");
        while(split != -1){
            // if split is 0, then the character is a space and the
            //  string has to be split to skip the 32 for the space
            //  and the 32 after the space
            if(split == 0){
                ans += " ";
                s = s.substring(split + 4);
            }
            else{
                int seg = Integer.parseInt(s.substring(0,split));
                ans += Character.toString((char)seg);
                s = s.substring(split + 2);
            }
            split = s.indexOf("32");;
        }
        return ans;
    }

}

