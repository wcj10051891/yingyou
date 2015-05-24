package xgame.core.db.sharding;

import java.lang.annotation.Annotation;

public abstract class ShardingStrategy {
	public abstract String process(String sql, Annotation annotation, Object arg);
}
