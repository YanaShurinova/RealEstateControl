package servlets.auth;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(){}
    public AccessDeniedException(String msg){
        super(msg);
    }
    public String getMessage(){
        return super.getMessage();
    }
}
