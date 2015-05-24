package xgame.core.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.BatchInsert;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.IdModSharding;
import xgame.core.db.annotation.Page;
import xgame.core.db.annotation.Sql;
import xgame.core.db.page.PageResult;
import xgame.core.db.page.dialect.Dialect;
import xgame.core.db.page.dialect.MySqlDialect;
import xgame.core.db.page.dialect.Oracle10gDialect;
import xgame.core.db.page.dialect.Oracle8iDialect;
import xgame.core.db.page.dialect.Oracle9iDialect;
import xgame.core.db.page.dialect.SqlServerDialect;
import xgame.core.db.sharding.IdModStrategy;
import xgame.core.db.sharding.ShardingStrategy;
import xgame.core.util.BeanUtils;
import xgame.core.util.StringUtils;

/**
 * Dao接口实例工厂，依赖apache Commons DBUtils。
 * <br>首先定义Dao接口，用@Dao标注，然后在接口中定义操作方法，用@Sql指定执行的sql语句（@Sql标记方法执定或者@Sql标记方法的String类型的参数来运行时动态执定sql），
 * 支持select，insert，delete，update语句。</br>
 * <br>example：</br>
	<pre>插入语句  @Sql(value="insert into active(`id`,`name`) values(:active.id,	:active.name)")
	Integer insert(@Arg(value="active") Active o);如果有自增字段，则返回自增字段值，否则返回影响行数</pre>

	<pre>批量插入 	@Sql(value="insert into active(`id`,`name`) values(:active.id,:active.name)")
	void insert(@BatchInsert("active") Collection&lt;Active&gt; o);</pre>
	
	<pre>返回Map结果，结果必须为单行@Sql("select * from active where id=:id")
	Map&lt;String, Object&gt; getById(@Arg("id")Integer id);</pre>

	<pre>返回JavaBean结果，结果必须为单行@Sql@Sql("select * from active where id=:id")
    Active getAciveById(@Arg("id")Integer id);</pre>
    
	<pre>查询标量结果@Sql("select count(*) from active")
    Long getCount();</pre>
    
    <pre>每一列转换成bean@Sql("select * from active")
    List&lt;Active&gt; getBeanList();</pre>
    
    <pre>每一列为一个Map，包括列名和值@Sql("select * from active")
    List&lt;Map&gt; getAll();</pre>
    
    <pre>返回想要得类型的查询@Sql("select id from active")
    public List&lt;Integer&gt; getIds();</pre>
    
    <pre>返回想要得类型的查询@Sql("select description from active")
    public List&lt;String&gt; getDescs();</pre>
    
    <pre>in查询@Sql("select * from active where id in(:ids)")
    List&lt;Map&lt;String, Object&gt;&gt; getByIds(@Arg("ids")int[] ids);</pre>
    
    <pre>更新	@Sql("update active set description=:des where id=:id")
    public void update(@Arg("id") Integer id, @Arg("des")String des);</pre>
    
    <pre>删除	@Sql("delete from active where id=:id")
    public void delete(@Arg("id") Integer id);</pre>
    
    <pre>分页查询，返回Active List	@Sql("select * from active")
    public PageResult getPage(@Page(Active.class) PageResult page);</pre>
    
    <pre>分页查询，返回StringL类型的List    @Sql("select name from active where name in(:names)")
    public PageResult getPage2(@Page(String.class) PageResult page, @Arg("names")List&lt;String&gt; names);</pre>
    
    <pre>动态指定sql
	public PageResult getPage3(@Page(Map.class) PageResult page, @Sql String sql, @Arg("id")Integer id);</pre>
	
	<pre>简单sharding支持，以某个传入的整型参数shardArg（如playerId，userId）对分表个数取模加1，计算得到正确表名后缀数字来替换，生成最终正确的表名（数据库表名要定义为数字结尾，而且要创建表的个数为分表的数目）。
	insert  @Sql(value="insert into player_item_1(	`id`,	`itemId`,	`binding`,	`source`,	`playerId`,	`stackNum`,	`currDurability`,	`used`,	`createTime`,	`extAttribute`,	`rmb`,	`gold`,	`goldTicket`,	`icon`,	`hp`,	`mp`,	`normalHurt`,	`attackSpeed`,	`pp`,	`sp`,	`phyDef`,	`magDef`,	`aim`,	`av`,	`vio`,	`speed`,	`forgeLevel`,	`forgeFailTimes`,	`enchaseHole1`,	`enchaseHole2`,	`enchaseHole3`,	`forgeHole1`,	`forgeHole2`,	`recastValues`,	`multiRecast`,	`suitItemId`,	`wish`,	`forgeAddLv`,	`score`	) values(	:playerItem.id,	:playerItem.itemId,	:playerItem.binding,	:playerItem.source,	:playerItem.playerId,	:playerItem.stackNum,	:playerItem.currDurability,	:playerItem.used,	:playerItem.createTime,	:playerItem.extAttribute,	:playerItem.rmb,	:playerItem.gold,	:playerItem.goldTicket,	:playerItem.icon,	:playerItem.hp,	:playerItem.mp,	:playerItem.normalHurt,	:playerItem.attackSpeed,	:playerItem.pp,	:playerItem.sp,	:playerItem.phyDef,	:playerItem.magDef,	:playerItem.aim,	:playerItem.av,	:playerItem.vio,	:playerItem.speed,	:playerItem.forgeLevel,	:playerItem.forgeFailTimes,	:playerItem.enchaseHole1,	:playerItem.enchaseHole2,	:playerItem.enchaseHole3,	:playerItem.forgeHole1,	:playerItem.forgeHole2,	:playerItem.recastValues,	:playerItem.multiRecast,	:playerItem.suitItemId,	:playerItem.wish,	:playerItem.forgeAddLv,	:playerItem.score	)")
	void insert(@Arg(value="playerItem") PlayerItem entity, @IdModSharding(tableName="player_item_1")Integer shardArg);</pre>
	<pre>
	delete @Sql(value="delete from player_item_1 where id=:id")
	void delete(@Arg(value="id")Long id, @IdModSharding(tableName="player_item_1")Integer shardArg);</pre>
	<pre>
	update @Sql(value="update player_item_1 set 	`id`=:playerItem.id,	`itemId`=:playerItem.itemId,	`binding`=:playerItem.binding,	`source`=:playerItem.source,	`playerId`=:playerItem.playerId,	`stackNum`=:playerItem.stackNum,	`currDurability`=:playerItem.currDurability,	`used`=:playerItem.used,	`createTime`=:playerItem.createTime,	`extAttribute`=:playerItem.extAttribute,	`rmb`=:playerItem.rmb,	`gold`=:playerItem.gold,	`goldTicket`=:playerItem.goldTicket,	`icon`=:playerItem.icon,	`hp`=:playerItem.hp,	`mp`=:playerItem.mp,	`normalHurt`=:playerItem.normalHurt,	`attackSpeed`=:playerItem.attackSpeed,	`pp`=:playerItem.pp,	`sp`=:playerItem.sp,	`phyDef`=:playerItem.phyDef,	`magDef`=:playerItem.magDef,	`aim`=:playerItem.aim,	`av`=:playerItem.av,	`vio`=:playerItem.vio,	`speed`=:playerItem.speed,	`forgeLevel`=:playerItem.forgeLevel,	`forgeFailTimes`=:playerItem.forgeFailTimes,	`enchaseHole1`=:playerItem.enchaseHole1,	`enchaseHole2`=:playerItem.enchaseHole2,	`enchaseHole3`=:playerItem.enchaseHole3,	`forgeHole1`=:playerItem.forgeHole1,	`forgeHole2`=:playerItem.forgeHole2,	`recastValues`=:playerItem.recastValues,	`multiRecast`=:playerItem.multiRecast,	`suitItemId`=:playerItem.suitItemId,	`wish`=:playerItem.wish,	`forgeAddLv`=:playerItem.forgeAddLv,	`score`=:playerItem.score	 where id=:playerItem.id")
	void update(@Arg(value="playerItem") PlayerItem entity, @IdModSharding(tableName="player_item_1")Integer shardArg);</pre>
	<pre>
	get @Sql(value="select * from player_item_1 where id=:id")
	PlayerItem get(@Arg(value="id") Long id, @IdModSharding(tableName="player_item_1")Integer shardArg);</pre>
	
	编程式事务支持：
	daoContext.jdbc.doInTransaction(new Runnable() {
			@Override
			public void run() {
				事务逻辑代码
			}
		});
 *	@author wcj
 */
public class DaoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
	private static final String comma = ",";
	private static final String argPrefix = ":";
	private static final String quote = "'";
	private static final String dateFormat = "yyyy-MM-dd hh:mm:ss";
	private static final String select = "select";
	private static final String insert = "insert";
	private static final String delete = "delete";
	private static final String update = "update";

	private static final Map<Class<?>, BeanHandler<?>> beanHandlers = new ConcurrentHashMap<Class<?>, BeanHandler<?>>();
	private static final Map<Class<?>, BeanListHandler<?>> beanListHandlers = new ConcurrentHashMap<Class<?>, BeanListHandler<?>>();
	private static final MapHandler mapHandler = new MapHandler();
	private static final ColumnListHandler<?> columnListHandler = new ColumnListHandler<Object>();
	private static final MapListHandler mapListHandler = new MapListHandler();
	private static final ScalarHandler<?> scalarHandler = new ScalarHandler<Object>();
	private static final RowProcessor basicRowProcessor = new BasicRowProcessor();
	private static final Map<Class<?>, ShardingStrategy> shardingStrategys = new HashMap<Class<?>, ShardingStrategy>();

	private static final Map<Class<?>, Class<?>> buildInTypes = new HashMap<Class<?>, Class<?>>();
	static {
		buildInTypes.put(byte.class, Byte.class);
		buildInTypes.put(boolean.class, Boolean.class);
		buildInTypes.put(char.class, Character.class);
		buildInTypes.put(short.class, Short.class);
		buildInTypes.put(int.class, Integer.class);
		buildInTypes.put(long.class, Long.class);
		buildInTypes.put(float.class, Float.class);
		buildInTypes.put(double.class, Double.class);
		buildInTypes.put(Byte.class, Byte.class);
		buildInTypes.put(Boolean.class, Boolean.class);
		buildInTypes.put(Character.class, Character.class);
		buildInTypes.put(Short.class, Short.class);
		buildInTypes.put(Integer.class, Integer.class);
		buildInTypes.put(Long.class, Long.class);
		buildInTypes.put(Float.class, Float.class);
		buildInTypes.put(Double.class, Double.class);
		buildInTypes.put(String.class, String.class);
	}
	
	private static final Map<String, Dialect> dialects = new HashMap<String, Dialect>();
	static {
		dialects.put( "MySQL", new MySqlDialect());
		dialects.put( "Microsoft SQL Server", new SqlServerDialect());
		dialects.put( "Oracle8", new Oracle8iDialect());
		dialects.put( "Oracle9", new Oracle9iDialect());
		dialects.put( "Oracle10", new Oracle10gDialect());
	}

	private final Map<Class<?>, Object> proxyCache = new ConcurrentHashMap<Class<?>, Object>();
	private final TransactionalQueryRunner jdbc;
	private Dialect currentDialect;
	
	public DaoFactory(TransactionalQueryRunner queryRunner) {
		this.jdbc = queryRunner;
		try {
			Connection conn = this.jdbc.getDataSource().getConnection();
			try {
				DatabaseMetaData meta = conn.getMetaData();
				String databaseName = meta.getDatabaseProductName();
				currentDialect = dialects.get(databaseName);
				if(currentDialect == null){
					int databaseMajorVersion = 0;
					try {
						Method gdbmvMethod = DatabaseMetaData.class.getMethod("getDatabaseMajorVersion");
						databaseMajorVersion = ( (Integer) gdbmvMethod.invoke(meta) ).intValue();
					}
					catch (Exception e) {
						throw new DaoException("datasource init error.", e);
					}
					currentDialect = dialects.get(databaseName + "" + databaseMajorVersion);
				}
				if(currentDialect == null)
					currentDialect = new Dialect();
			}
			finally {
				DbUtils.close(conn);
			}
		}
		catch (Exception e) {
			throw new DaoException("fetch database info error.", e);
		}
		shardingStrategys.put(IdModSharding.class, new IdModStrategy());
	}

	private InvocationHandler handler = new InvocationHandler() {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) {
			try {
				return invoke0(proxy, method, args);
			} catch (Exception e) {
				throw new DaoException("dao method invoke error.", e);
			}
		}
		
		@SuppressWarnings("rawtypes")
		public Object invoke0(Object proxy, Method method, Object[] args) throws Exception {
			String sql = null;
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			if (method.isAnnotationPresent(Sql.class))
				sql = method.getAnnotation(Sql.class).value().trim();
			else {
				for (int i = 0; i < parameterAnnotations.length; i++) {
					Annotation[] anno = parameterAnnotations[i];
					if (anno.length > 0)
						if(anno[0].annotationType() == Sql.class) {
							if(!(args[i] instanceof String))
								throw new DaoException("@Sql annotationed method parameter must instanceof String.");
							sql = args[i].toString().trim();
							break;
						}
				}
			}
			if(!StringUtils.hasText(sql))
				throw new DaoException("sql not found or empty value, dao method must annotationed by @Sql, or provide @Sql method parameter.");

			boolean isSelect = sql.startsWith(select);
			if (isSelect && method.getReturnType() == void.class)
				return null;

			boolean isInsert = sql.startsWith(insert);
			if (!(isSelect || isInsert || sql.startsWith(delete) || sql.startsWith(update)))
				throw new DaoException("unsupported sql operation.");
			
			PageResult pageResult = null;
			Class<?> pageReturnType = null;
			boolean isBatchInsert = false;
			for (int i = 0; i < parameterAnnotations.length; i++) {
				Annotation[] anno = parameterAnnotations[i];
				if (anno.length > 0)
					if (anno[0].annotationType() == Arg.class) {
						for (Entry<String, String> entry : convert(((Arg) anno[0]).value(), args[i]).entrySet()) {
							String key = entry.getKey();
							Matcher matcher = Pattern.compile(argPrefix + key).matcher(sql);
							StringBuffer sqlTemp = new StringBuffer();
							while (matcher.find())
								matcher.appendReplacement(sqlTemp, entry.getValue());

							matcher.appendTail(sqlTemp);
							sql = sqlTemp.toString();
						}
					} else if (anno[0].annotationType() == Page.class) {
						if(!isSelect)
							throw new DaoException("@Page only support select operation.");
						if(!(args[i] instanceof PageResult))
							throw new DaoException("@Page annotationed param must instanceof PageResult.");
					    pageReturnType = ((Page) anno[0]).value();
					    pageResult = (PageResult)args[i];
					} else if (anno[0].annotationType() == BatchInsert.class) {
						if(!isInsert)
							throw new DaoException("@BatchInsert only support insert operation.");
						if(!(args[i] instanceof Collection))
							throw new DaoException("@BatchInsert annotationed param must instanceof Collection.");
						
						isBatchInsert = true;
						sql = sql.replace(";", "");
						String argName = ((BatchInsert) anno[0]).value();
						int index = sql.indexOf(" values");
						String valueSql = sql.substring(index + 7);
						sql = sql.substring(0, index);
						List<String> valueSqls = new ArrayList<String>();
						for(Object v : (Collection)args[i]) {
							String temp = valueSql;
							for (Entry<String, String> entry : convert(argName, v).entrySet()) {
								String key = entry.getKey();
								StringBuffer sqlTemp = new StringBuffer();
								Matcher matcher = Pattern.compile(argPrefix + key).matcher(temp);
								while (matcher.find())
									matcher.appendReplacement(sqlTemp, entry.getValue());

								matcher.appendTail(sqlTemp);
								temp = sqlTemp.toString();
							}
							valueSqls.add(temp);
						}
						sql += " values" + StringUtils.join(valueSqls, ",");
					} else if (anno[0].annotationType() == IdModSharding.class) {
						sql = shardingStrategys.get(IdModSharding.class).process(sql, anno[0], args[i]);
					}
			}
			local.remove();
			logger.debug(sql);
			if (isSelect) {
				//process page
				if(pageResult != null && PageResult.class.isAssignableFrom(method.getReturnType())){
					Long total = (Long) jdbc.query(getTotalSql(sql), scalarHandler);
					if(total == 0)
						return pageResult;
					
					pageResult.init(total.intValue());
					if(pageResult.getPageIndex() > pageResult.getPageCount()){
					    pageResult.setData(Collections.EMPTY_LIST);
					    return pageResult;
					}
					String limitSql = currentDialect.getLimitString(sql, pageResult.getStart(), pageResult.getPageSize());
					if(StringUtils.hasText(limitSql)){
						if (pageReturnType == Map.class)// List<Map<String, Object>
						    pageResult.setData((List<?>) jdbc.query(limitSql, mapListHandler));
						else if (buildInTypes.containsKey(pageReturnType))// primitive list
						    pageResult.setData((List<?>) jdbc.query(limitSql, columnListHandler));
						else// bean list
						    pageResult.setData((List<?>) jdbc.query(limitSql, getBeanListHandler(pageReturnType)));
						return pageResult;
					}else{
					    final PageResult result = pageResult;
					    final Class<?> pageReturnTypeClass = pageReturnType;
					    return jdbc.query(sql, new ResultSetHandler<PageResult>(){
							@Override
							public PageResult handle(ResultSet rs) throws SQLException {
							    if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
							    	rs.absolute(result.getStart() + 1);
							    } else {
							    	for (int i = 0; i < result.getStart(); i++)
							    		rs.next();
							    }
							    int limit = result.getPageSize();
							    List<Object> rows = new ArrayList<Object>(); 
							    for (int count = 0; rs.next() && count < limit; count++) {
									if (pageReturnTypeClass == Map.class)// List<Map<String, Object>
									    rows.add(basicRowProcessor.toMap(rs));
									else if (buildInTypes.containsKey(pageReturnTypeClass))// primitive list
									    rows.add(basicRowProcessor.toArray(rs)[0]);
									else// bean list
									    rows.add(basicRowProcessor.toBean(rs, pageReturnTypeClass));
							    }
							    result.setData(rows);
							    return result;
							}
					    });
					}
				}else{
					return query(sql, method.getGenericReturnType());
				}
			}else{
				Type returnType = method.getReturnType();
				Class<?> c = (Class<?>) returnType;
				if (c == void.class) {
					jdbc.update(sql);
				} else {
					if (c.isPrimitive() && buildInTypes.containsKey(c))
						c = buildInTypes.get(c);

					if (Number.class.isAssignableFrom(c)) {
						if (isInsert && !isBatchInsert) {
							Connection conn = jdbc.getConnection();
							PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
							try {
								int effectRowCount = stmt.executeUpdate();
								ResultSet keys = stmt.getGeneratedKeys();
								Integer result = keys.next() ? keys.getInt(1) : effectRowCount;
								DbUtils.close(keys);
								return result;
							} catch (Exception e) {
								throw new DaoException("insert sql execute error, sql:" + sql, e);
							} finally {
								DbUtils.close(stmt);
								jdbc.close(conn);
							}
						} else {
							return jdbc.update(sql);
						}
					} else {
						jdbc.update(sql);
					}
				}
			}
			return null;
		}
	};
	
	private Object query(String sql, Type returnType) throws Exception {
		if (returnType instanceof Class<?>) {
			Class<?> c = (Class<?>) returnType;
			// primitive
			if (buildInTypes.containsKey(c)) {
				return jdbc.query(sql, scalarHandler);
			} else if (Map.class.isAssignableFrom(c))
				// map<String, Object>
				return jdbc.query(sql, mapHandler);
			else if (Collection.class.isAssignableFrom(c))
				// List<Map<String, Object>
				return jdbc.query(sql, mapListHandler);
			else
				// Bean
				return jdbc.query(sql, getBeanHandler(c));
		} else if (returnType instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) returnType;
			Class<?> rawType = (Class<?>) paramType.getRawType();
			if (Collection.class.isAssignableFrom(rawType)) {
				Type[] actualTypeArguments = paramType.getActualTypeArguments();
				if (actualTypeArguments.length == 1) {
					Type t = actualTypeArguments[0];
					if (t instanceof ParameterizedType) {
						ParameterizedType tt = (ParameterizedType) t;
						if ((Class<?>) tt.getRawType() == Map.class) {
							Type[] ttt = tt.getActualTypeArguments();
							if ((Class<?>) ttt[0] == String.class && (Class<?>) ttt[1] == Object.class)
								return jdbc.query(sql, mapListHandler);
						}
					} else if (t instanceof Class<?>) {
						Class<?> tt = (Class<?>) t;
						if (tt == Map.class)
							// List<Map<String, Object>
							return jdbc.query(sql, mapListHandler);
						else if (buildInTypes.containsKey(tt))
							// primitive list
							return jdbc.query(sql, columnListHandler);
						else
							// bean list
							return jdbc.query(sql, getBeanListHandler(tt));
					} else {
						return jdbc.query(sql, mapListHandler);
					}
				}
			} else if (rawType == Map.class) {
				// map<String, Object>
				return jdbc.query(sql, mapHandler);
			} else {
				// Bean
				return jdbc.query(sql, getBeanHandler(rawType));
			}
		}
		return null;
	}

	private static ThreadLocal<Set<Object>> local = new ThreadLocal<Set<Object>>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String, String> convert(String key, Object value) {
		if (key == null)
			return Collections.EMPTY_MAP;

		Map<String, String> result = new LinkedHashMap<String, String>();
		if (value == null || value instanceof Number || value instanceof Boolean) {
			result.put(key, String.valueOf(value));
		} else if (value instanceof String || value.getClass().isArray() || value instanceof Collection || value instanceof Date) {
			result.put(key, toString(value));
		} else {
			checkDeadLoop(value);
			if (value instanceof Map) {
				for (Object en : ((Map) value).entrySet()) {
					Entry entry = (Entry) en;
					Object entryKey = entry.getKey();
					if (!(entryKey instanceof String))
						throw new DaoException("Map param key only support string type.");

					for (Entry<String, String> entry2 : convert(String.valueOf(entryKey), entry.getValue()).entrySet())
						result.put(key + "." + entry2.getKey(), entry2.getValue());
				}
			} else {
				result.putAll(convert(key, BeanUtils.toMap(value)));
			}
		}
		return result;
	}
	
	private static String toString(Object value) {
		if (value instanceof String) {
			return quote + String.valueOf(value) + quote;
		} else if (value instanceof Date) {
			return quote + new SimpleDateFormat(dateFormat) .format((Date) value) + quote;
		} else if (value instanceof Collection) {
			Collection<?> values = (Collection<?>)value;
			List<String> strs = new ArrayList<String>(values.size()); 
			for(Object v : values) 
				strs.add(toString(v));
			return StringUtils.join(strs, comma); 
		} else if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			String[] strs = new String[length];
			for (int i = 0; i < length; i++)
				strs[i] = toString(Array.get(value, i));
			return StringUtils.join(strs, comma);
		}
		return String.valueOf(value);
	}

	@SuppressWarnings("unchecked")
	private static <T> BeanHandler<T> getBeanHandler(Class<T> beanClass) {
		if (beanHandlers.containsKey(beanClass))
			return (BeanHandler<T>) beanHandlers.get(beanClass);
		BeanHandler<T> handler = new BeanHandler<T>(beanClass, basicRowProcessor);
		beanHandlers.put(beanClass, handler);
		return handler;
	}

	@SuppressWarnings("unchecked")
	private static <T> BeanListHandler<T> getBeanListHandler(Class<T> beanClass) {
		if (beanListHandlers.containsKey(beanClass))
			return (BeanListHandler<T>) beanListHandlers.get(beanClass);
		BeanListHandler<T> handler = new BeanListHandler<T>(beanClass, basicRowProcessor);
		beanListHandlers.put(beanClass, handler);
		return handler;
	}
	
	private static String getTotalSql(String sql){
		return "select count(*) from (" + sql + ") _temp_count_all_";
	}

	private static void checkDeadLoop(Object value) {
		Set<Object> cls = local.get();
		if (cls == null) {
			cls = new HashSet<Object>();
			cls.add(value);
			local.set(cls);
		} else if (cls.contains(value)) {
			throw new DaoException("dead loop.");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> daoClass) {
		if (!daoClass.isAnnotationPresent(Dao.class))
			throw new DaoException("daoClass must annotated by @Dao Annotation.");

		if (proxyCache.containsKey(daoClass))
			return (T) proxyCache.get(daoClass);

		Object proxy = Proxy.newProxyInstance(DaoFactory.class.getClassLoader(), new Class[] { daoClass }, handler);
		proxyCache.put(daoClass, proxy);
		return (T) proxy;
	}
}
