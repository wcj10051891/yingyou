package com.shadowgame.rpg.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.map.AbstractSpirit;
import com.shadowgame.rpg.modules.map.GameMap;
import com.shadowgame.rpg.modules.map.Grid;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;

public abstract class MapUtil {
	
	/**
	 * Returns distance between two 2D points
	 * 
	 * @param point1
	 *            first point
	 * @param point2
	 *            second point
	 * @return distance between points
	 */
	public static double calcDistance(Point point1, Point point2)
	{
		return calcDistance(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * 计算2点间距离(返回像素)
	 */
	public static double calcDistance(int x1, int y1, int x2, int y2)
	{
		// using long to avoid possible overflows when multiplying
		long dx = x2 - x1;
		long dy = y2 - y1;

		// return Math.hypot(x2 - x1, y2 - y1); // Extremely slow
		// return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // 20 times faster than hypot
		return Math.sqrt(dx * dx + dy * dy); // 10 times faster then previous line
	}

	/**
	 * 计算2格子之间距离(返回格子x，y差值中得最大值)
	 * @param grid1
	 * @param grid2
	 * @return
	 */
	public static int calcDistance(Grid grid1, Grid grid2) {
		return Math.max(Math.abs(grid2.x - grid1.x), Math.abs(grid2.y - grid1.y));
	}
	
	/**
	 * 
	 * @param object
	 * @param x
	 * @param y
	 * @return
	 */
	public static double calcDistance(AbstractFighter object, int x, int y)
	{
		Position pos = object.getPosition();
		return calcDistance(pos.getX(), pos.getY(), x, y);
	}
	
	public static double calcDistance(AbstractFighter f1, AbstractFighter f2)
	{
		Position pos1 = f1.getPosition();
		Position pos2 = f2.getPosition();
		
		return calcDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
	}
	
	/**
	 * 直线上的点
	 * @return
	 */
	public static LinkedList<Point> getLinePoints(Point start, Point end, GameMap map, int px) {
		LinkedList<Point> path = new LinkedList<Point>();
		if (start == null || end == null) {
			return path;
		}
		path.add(start);
		if (!start.equals(end)) {
			int _pointY = end.y - start.y;
			int _pointX = end.x - start.x;
			if (Math.abs(_pointY) >= Math.abs(_pointX)) {
				if (_pointY < 0) {
					px = -px;
				}
				double per = (double) _pointX / _pointY * px;
				int y = start.y;
				int n = Math.abs(_pointY / px);
				for (int i = 0; i < n; i++) {
					y = y + px;
					int x = start.x + (int) Math.round(per * (i + 1));
					path.add(new Point(x, y));
				}
			} else {
				if (_pointX < 0) {
					px = -px;
				}
				double per = (double) _pointY / _pointX * px;
				int x = start.x;
				int n = Math.abs(_pointX / px);
				for (int i = 0; i < n; i++) {
					x = x + px;
					int y = start.y + (int) Math.round(per * (i + 1));
					path.add(new Point(x, y));
				}
			}
		}
		Point last = path.getLast();
		if(last.equals(end))
			path.addLast(end);
		return path;
	}
	
	/**
	 * 获取同一地图上，两点之间所有的格子
	 * @return
	 */
	public static List<Grid> getLineGrids(Point pos1, Point pos2, GameMap map) {
		List<Grid> grids = new ArrayList<Grid>();
		Grid start = map.getGridByMapPoint(pos1.x, pos1.y);
		Grid end = map.getGridByMapPoint(pos2.x, pos2.y);
		if (start == null || end == null) {
			return grids;
		}
		grids.add(start);
		if (!start.equals(end)) {
			int _gridY = end.y - start.y;
			int _gridX = end.x - start.x;
			if (Math.abs(_gridY) >= Math.abs(_gridX)) {
				int step = 1;
				if (_gridY < 0) {
					step = -1;
				}
				double per = (double) _gridX / _gridY;
				int gridY = start.y;
				for (int i = 0; i < Math.abs(_gridY); i++) {
					gridY = gridY + step;
					int gridX = start.x + (int) Math.round(per * (i + 1));
					Grid grid = map.getGridByGridXY(gridX, gridY);
					if (grid != null) {
						grids.add(grid);
					}
				}
			} else {
				int step = 1;
				if (_gridX < 0) {
					step = -1;
				}
				double per = (double) _gridY / _gridX;
				int gridX = start.x;
				for (int i = 0; i < Math.abs(_gridX); i++) {
					gridX = gridX + step;
					int gridY = start.y + (int) Math.round(per * (i + 1));
					Grid grid = map.getGridByGridXY(gridX, gridY);
					if (grid != null) {
						grids.add(grid);
					}
				}
			}
		}
		return grids;
	}
	
	/**
	 * 计算两点之间的朝向
	 */
	public static int calcDirection(Point pos1, Point pos2) {
		if (pos1.y == pos2.y && pos1.x == pos2.x) {
			return 4;
		} else if (pos1.y == pos2.y) {
			if (pos1.x < pos2.x) {
				return 2;
			} else {
				return 6;
			}
		} else if (pos1.x == pos2.x) {
			if (pos1.y < pos2.y) {
				return 4;
			} else {
				return 0;
			}
		} else {
			double angle = Math.atan(((double) (pos2.y - pos1.y)) / -(pos2.x - pos1.x));
			double angle22d5 = Math.PI / 8;
			double angle67d5 = Math.PI / 2 - angle22d5;
			if (angle > -angle22d5 && angle <= angle22d5) {
				if (pos1.x < pos2.x) {
					return 2;
				} else {
					return 6;
				}
			} else if (angle > angle22d5 && angle <= angle67d5) {
				if (pos1.x < pos2.x) {
					return 1;
				} else {
					return 5;
				}
			} else if (angle > -angle67d5 && angle <= -angle22d5) {
				if (pos1.x < pos2.x) {
					return 3;
				} else {
					return 7;
				}
			} else {
				if (pos1.y < pos2.y) {
					return 4;
				} else {
					return 0;
				}
			}
		}
	}
	
	/**
	 * 计算两个格子的朝向（格子1和格子2相邻）
	 */
	public static int calcDirection(Grid grid1, Grid grid2) {
		if (grid1.y == grid2.y && grid1.x == grid2.x) {
			return 4;
		} else if (grid1.y == grid2.y) {
			if (grid1.x < grid2.x) {
				return 2;
			} else {
				return 6;
			}
		} else if (grid1.x == grid2.x) {
			if (grid1.y < grid2.y) {
				return 4;
			} else {
				return 0;
			}
		} else if (grid1.x < grid2.x) {
			if (grid1.y < grid2.y) {
				return 3;
			} else {
				return 1;
			}
		} else {
			if (grid1.y < grid2.y) {
				return 5;
			} else {
				return 7;
			}
		}
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param ss
	 *            segment start point
	 * @param se
	 *            segment end point
	 * @param p
	 *            point to found closest point on segment
	 * @return closest point on segment to p
	 */
	public static Point getClosestPointOnSegment(Point ss, Point se, Point p)
	{
		return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param sx1
	 *            segment x coord 1
	 * @param sy1
	 *            segment y coord 1
	 * @param sx2
	 *            segment x coord 2
	 * @param sy2
	 *            segment y coord 2
	 * @param px
	 *            point x coord
	 * @param py
	 *            point y coord
	 * @return closets point on segment to point
	 */
	public static Point getClosestPointOnSegment(int sx1, int sy1, int sx2, int sy2, int px, int py)
	{
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		if((xDelta == 0) && (yDelta == 0))
		{
			throw new IllegalArgumentException("Segment start equals segment end");
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if(u < 0)
		{
			closestPoint = new Point(sx1, sy1);
		}
		else if(u > 1)
		{
			closestPoint = new Point(sx2, sy2);
		}
		else
		{
			closestPoint = new Point((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
		}

		return closestPoint;
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param ss
	 *            segment start point
	 * @param se
	 *            segment end point
	 * @param p
	 *            point to found closest point on segment
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(Point ss, Point se, Point p)
	{
		return getDistanceToSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param sx1
	 *            segment x coord 1
	 * @param sy1
	 *            segment y coord 1
	 * @param sx2
	 *            segment x coord 2
	 * @param sy2
	 *            segment y coord 2
	 * @param px
	 *            point x coord
	 * @param py
	 *            point y coord
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(int sx1, int sy1, int sx2, int sy2, int px, int py)
	{
		Point closestPoint = getClosestPointOnSegment(sx1, sy1, sx2, sy2, px, py);
		return calcDistance(closestPoint.x, closestPoint.y, px, py);
	}

	/**
	 * 
	 * @param obj1X
	 * @param obj1Y
	 * @param obj2X
	 * @param obj2Y
	 * @return int
	 */
	public final static float calculateAngleFrom(float obj1X, float obj1Y, float obj2X, float obj2Y)
	{
		float angleTarget = (float) Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
			angleTarget = 360 + angleTarget;
		return angleTarget;
	}
	
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return float
	 */
	public static float calculateAngleFrom(AbstractSpirit obj1, AbstractSpirit obj2)
	{
		return calculateAngleFrom(obj1.getPosition().getX(), obj1.getPosition().getY(), obj2.getPosition().getX(), obj2.getPosition().getY());
	}
	
	/**
	 * 
	 * @param clientHeading
	 * @return float
	 */
	public final static float convertHeadingToDegree(byte clientHeading)
	{
		float degree = clientHeading * 3;
		return degree;
	}
}
