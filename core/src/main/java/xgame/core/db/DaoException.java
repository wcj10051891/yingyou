package xgame.core.db;

public class DaoException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public DaoException(String msg)
    {
        super(msg);
    }
    
    public DaoException(String msg, Throwable throwable)
    {
        super(msg, throwable);
    }
}
