package nc.vo.logging;

/**
 * Created by IntelliJ IDEA.
 * User: 何冠宇
 * Date: ${DATE}$
 * Time: ${TIME}$
 * <p/>
 * <p/>
 *
 * 日志的运行时刻异常，日志系统中的异常采用non-catchable的异常形式
 */
public class LoggerException extends RuntimeException {
    protected Throwable cause = null;

    public LoggerException() {
        super();
    }

    public LoggerException(String message) {
        super(message);
    }

    public LoggerException(Throwable cause) {
        this((cause == null) ? null : cause.toString(), cause);
    }

    public LoggerException(String message, Throwable cause) {

        super(message + " (caused by  " + cause + ")");
        this.cause = cause;

    }

    public Throwable getCause() {
        return (this.cause);
    }
}
