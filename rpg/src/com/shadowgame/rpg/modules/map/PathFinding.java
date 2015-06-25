package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PathFinding {
	public static class PathNode {
		public float f;
		public float g;
		public float h;
		public Grid grid;
		public PathNode parent;
		
		public PathNode(Grid grid) {
			this.grid = grid;
		}

		@Override
		public boolean equals(Object obj) {
			PathNode n = ((PathNode)obj);
			return n.grid.x == grid.x && n.grid.y == grid.y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + grid.x;
			result = prime * result + grid.y;
			return result;
		}
		
		@Override
		public String toString() {
			return grid.x + "," + grid.y + "[" + f + "]";
		}
	}
	
	static class PathNodeComparator implements Comparator<PathNode> {
		static final PathNodeComparator INSTANCE = new PathNodeComparator();
		
		@Override
		public int compare(PathNode o1, PathNode o2) {
			if(o1.f - o2.f < 0)
				return -1;
			else if(o1.f - o2.f == 0)
				return 0;
			return 1;
		}
		
	}
	
	public static List<PathNode> find(GameMap map, Point startPoint, Point endPoint) {
		PathNode start = new PathNode(map.getGrid(startPoint));
		PathNode end = new PathNode(map.getGrid(endPoint));
		TreeSet<PathNode> open = new TreeSet<PathNode>(PathNodeComparator.INSTANCE);
		Set<PathNode> close = new HashSet<PathNode>();
		open.add(start);
		while(open.size() > 0) {
			PathNode current = open.pollFirst();
			if(current.equals(end)) {
				end = current;
				break;
			}
			close.add(current);
			for (PathNode node : getAroundNodes(map, current)) {
				if(node.grid.block)
					continue;
				if(close.contains(node))
					continue;
//				if (node.y != current.y && node.x != current.y) {
//					if (mapInfos[round.getY()][grid.getGrid().getX()].getBlock() == 0 && mapInfos[grid.getGrid().getY()][round.getX()].getBlock() == 0) {
//						continue;
//					}
//				} 
				node.g = Double.valueOf(Math.sqrt(Math.pow(Math.abs(start.grid.x - node.grid.x), 2) + Math.pow(Math.abs(start.grid.y - node.grid.y), 2))).floatValue();
				node.h = Math.abs(end.grid.x - node.grid.x) + Math.abs(end.grid.y - node.grid.y);
				node.f = node.g + node.h;
				node.parent = current;
//				if(open.contains(node)) {
//					float newG = 0;
//					if(node.x == current.x || node.y == current.y)
//						newG = current.g + 1;
//					else
//						newG = current.g + 1.4f;
//					if(newG < node.g) {
//						node.parent = current;
//						node.g = newG;
//						node.h = Math.abs(end.x - node.x) + Math.abs(end.y - node.y);
//						node.f = node.g + node.f;
//						open.remove(node);
//						open.add(node);
//					}
//				} else {
//					open.add(node);
//				}
				open.add(node);
			}
		}
		if(end.parent != null) {
			List<PathNode> result = new ArrayList<PathFinding.PathNode>();
			PathNode current = end.parent;
			while(current != null) {
				result.add(0, current);
				current = current.parent;
			}
			result.add(end);
			return result;
		}
		return null;
	}
	
	private static List<PathNode> getAroundNodes(GameMap map, PathNode current) {
		List<PathNode> result = new ArrayList<PathNode>();
		for(int i = current.grid.x - 1; i <= current.grid.x + 1; i++) {
			if(i < 0)
				continue;
			for(int j = current.grid.y - 1; j <= current.grid.y + 1; j++) {
				if(j < 0)
					continue;
				if(i == current.grid.x && j == current.grid.y)
					continue;
				result.add(new PathNode(map.getGrid(i, j)));
			}
		}
		return result;
	}
	
//	public static void main(String[] args) throws Exception {
//		PathFinding2 finding = new PathFinding2();
//		PathNode start = new PathNode(0, 8);
//		PathNode end = new PathNode(28, 7);
//		List<PathNode> find = finding.find(start, end);
//		if(find == null)
//			System.out.println("not found");
//		else
//			show(find);
//	}
//	
	private static void show(List<PathNode> p) throws Exception {
		int max_x = 0;
		int max_y = 0;
		for (PathNode point : p) {
			if(point.grid.x > max_x)
				max_x = point.grid.x;
			if(point.grid.y > max_y)
				max_y = point.grid.y;
		}
		max_y++;
		max_x++;
		char[][] map = new char[max_y][max_x];
		for (int i = 0; i < max_y; i++) {
			Arrays.fill(map[i], '0');
		}
		boolean first = false;
		for (Iterator<PathNode> it = p.iterator(); it.hasNext();) {
			PathNode pp = it.next();
			if(!first) {
				map[pp.grid.y][pp.grid.x] = 'S';
				first = true;
			} else if(it.hasNext()) {
				map[pp.grid.y][pp.grid.x] = '*';
			} else {
				map[pp.grid.y][pp.grid.x] = 'E';
			}
			Thread.sleep(200l);
			System.out.println("-------------------------------------------------------------");
			for (int i = 0; i < max_y; i++) {
				System.out.println(Arrays.toString(map[i]));
			}
		}
	}
	
}
