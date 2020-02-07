package yapi.exceptions;

public class NeuralNetException extends YAPIException {

    public NeuralNetException() {
        super();
    }

    public NeuralNetException(String message) {
        super(message);
    }

    public NeuralNetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeuralNetException(Throwable cause) {
        super(cause);
    }

    protected NeuralNetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}