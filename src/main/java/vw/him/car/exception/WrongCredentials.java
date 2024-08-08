package vw.him.car.exception;

public class WrongCredentials extends RuntimeException{

    public WrongCredentials(String message) {
        super(message);
    }
}
