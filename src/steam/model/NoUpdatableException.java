package steam.model;

public class NoUpdatableException extends RuntimeException {
    public NoUpdatableException(String mensaje){
        super (mensaje);
    }
}
