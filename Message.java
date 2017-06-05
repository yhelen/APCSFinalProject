public class Message extends RSA{

    private String _msg;
    private String _msgasc;
    private XP[] _msgarray;
    private int _size;
    private static final int LEN = 50;

    public Message(String msg){
        _msg = msg;
        _msgasc = convert(_msg);
        _msgarray = convertXP(_msgasc);
        _size = size();
    }

    public String getMsg(){
        return _msg;
    }

    public String getMsgasc(){
        return _msgasc;
    }

    public XP[] getMsgarray(){
        return _msgarray;
    }

    public int size(){
        int count = 0;
        for(XP x: _msgarray){
            if(x != null)
                count++;
            else
                break;
        }
        return count;
    }

    public int getSize(){
        return _size;
    }

    //converts msg to ascii string
    public String convert(String s){
        String ans = "";
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            //adding spaces between each character
            ans += (int)c + "32";
        }
        return ans;
    }

    //design option: add some character to the string and when that character comes
    //make the int smt weird or null
    //or maybe create an array of XPs for each letter a diff XP?
    //or maybe combine both ideas and create an array of XPs for long msgs,
    //separating each word by a space ascii number
    //for a space, it would be 323232, so middle character would be printed
    //make a boolean that tells whether there was a space before this space
    //converts ascii string to XP
    public XP[] convertXP(String s){
        //create an XP array
        if(s.length() / 100 > LEN)
            throw new IllegalArgumentException("message is too long");
        XP[] ans = new XP[LEN];
        int i = 0;
        while(i < s.length() / 100){
            //because the limit of an XP is currently 100;
            int lower = i  * 100;
            XP x = new XP(s.substring(lower,lower+100));
            ans[i] = x;
            i++;
        }
        ans[i] = new XP(s.substring(i*100));
        return ans;
    }

    //converts ascii string to encrypted XP directly?
    public XP[] encrypt(XP[] xps){
        //create an XP array
        //convert the String to XPs for the array
        //send each item in the array to be encrypted
        //return the array
        XP[] ans = new XP[LEN];
        for(int i = 0; i < _size; i++){
            XP temp = xps[i];
            XP x = encrypt(temp);
            ans[i] = x;
        }
        return ans;
    }

    //converts encrypted XP to decrypted String
    public XP[] decrypt(XP[] xps){
        XP[] ans = new XP[LEN];
        for(int i = 0; i < _size; i++){
            XP temp = xps[i];
            XP x = decrypt(temp);
            ans[i] = x;
        }
        return ans;
    }

    public String deconvert(XP[] xps){
        String ans = "";
        return ans;
    }

}
