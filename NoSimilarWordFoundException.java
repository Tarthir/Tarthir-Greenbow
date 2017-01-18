/**
 * Created by tyler on 1/18/2017.
 */

public class NoSimilarWordFoundException extends Exception {
        public NoSimilarWordFoundException() { super(); }
        public NoSimilarWordFoundException(String message) { super(message); }
        public NoSimilarWordFoundException(String message, Throwable cause) { super(message, cause); }
        public NoSimilarWordFoundException(Throwable cause) { super(cause); }
}
