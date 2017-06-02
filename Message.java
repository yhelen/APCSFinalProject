public class Message{

    private String _msg;
    private String _msgasc;    

    public Message(String msg){
	_msg = msg;
	_msgasc = convert(_msg);
    }

    //converts msg to ascii string
    public String convert(String s){
	return "";
    }

    //converts ascii string to encrypted string
    public String encrypt(String s){
	return "";
    }

}
