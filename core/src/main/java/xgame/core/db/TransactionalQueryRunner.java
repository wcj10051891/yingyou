package xgame.core.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalQueryRunner extends QueryRunner {
	private static final Logger log = LoggerFactory.getLogger(TransactionalQueryRunner.class);
	
	private boolean defaultAutoCommit;
	private ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();
	private ThreadLocal<Boolean> inTransaction = new ThreadLocal<Boolean>();

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> to use.
     *
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     */
    public TransactionalQueryRunner(DataSource ds, boolean defaultAutoCommit) {
        super(ds);
        this.defaultAutoCommit = defaultAutoCommit;
        inTransaction.set(false);
    }
    
    @Override
    protected Connection prepareConnection() throws SQLException {
    	Connection connection = localConnection.get();
    	if(connection != null)
    		return connection;
    	connection = super.prepareConnection();
    	localConnection.set(connection);
    	return connection;
    }
    
    /**
     * 关闭连接
     * 如果conn和线程本地连接不是同一个，那么直接close conn连接
     * 如果一样，则判断连接的是否有事务存在（检测AutoCommit为false则存在事务），
     * 有事务时候不会关闭连接，直到事务代码设置AutoCommit设置为true来标记事务结束
     */
    @Override
    protected void close(Connection conn) throws SQLException {
    	if(conn == null)
    		return;
    	Connection connection = localConnection.get();
    	if(connection != conn)
    		super.close(conn);
    	else {
    		//not in transaction
        	if(inTransaction.get() == null || !inTransaction.get()) {
        		try {
    				connection.setAutoCommit(defaultAutoCommit);
    			} finally {
    	    		super.close(connection);
    				localConnection.set(null);
    			}
        	}
    	}
    }
    
    public static interface Callable<V> {
    	V call();
    }
    /**
     *	1.事务内的所有jdbc操作要使用同一连接，
     *	2.事务内的每个jdbc操作不能关闭连接，要事务提交时候统一关闭
     *	3.非事务jdbc操作要正确关闭连接
     *	4.事务的标志就是该连接的autocommit状态, true为自动提交，没有事务。false为手动提交，存在事务。
     *	5.首先所有连接获取都是从线程本地ThreadLocal获取的，也就是各个线程单独的获取connection，互不干扰。
     *	6.连接的close()操作检测当前连接的autocommit状态是否标记过， false为在事务内，会由事务逻辑代码关闭，那么这个close操作就什么都不做，否则就正常关闭
     *  7.支持事务嵌套，默认如果上下文存在事务会在该事务下执行，不会改变任务状态。
     * 	@author wcj10051891@gmail.com
     */
	public <V> V doInTransaction(Callable<V> callable) {
		Boolean inTran = inTransaction.get();
		if(inTran != null && inTransaction.get())
			return callable.call();
		
		Connection connection = null;
		try {
			connection = prepareConnection();
		} catch (Exception e) {
			throw new DaoException("connection fetch error.", e);
		}
		try {
			connection.setAutoCommit(false);
			log.debug("set autocommit=false on connection:{}", connection);
		} catch (Exception e) {
			throw new DaoException("connection autoCommit disabled error.", e);
		}
		log.debug("thread {} transaction begin on connection {}.", Thread.currentThread(), connection);
		inTransaction.set(true);
		V result = null;
		try {
			result = callable.call();
			connection.commit();
			log.debug("thread {} commit transaction on connection:{}", Thread.currentThread(), connection);
		} catch (Exception ex) {
			try {
				connection.rollback();
				log.debug("thread {} rollback transaction on connection:{}", Thread.currentThread(), connection);
			} catch (Exception rollbackEx) {
				log.error("rollback exception.", rollbackEx);
			}
			throw new DaoException("do in transaction occur exception, rollback transaction.", ex);
		} finally {
			inTransaction.remove();
			try {
				close(connection);
			} catch (Exception ex) {
				log.error("close connection exception.", ex);
			}
		}
		return result;
	}
	
	public void doInTransaction(final Runnable runnable) {
		doInTransaction(new Callable<Object>() {
			@Override
			public Object call() {
				runnable.run();
				return null;
			}
		});
	}
	
	public Connection getConnection() throws Exception {
		return prepareConnection();
	}
	
}