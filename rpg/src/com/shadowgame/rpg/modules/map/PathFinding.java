package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinding {
	static class Node {
		int x;
		int y;
		int f;
		int g;
		int h;
		Node parent;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			Node n = ((Node)obj);
			return n.x == x && n.y == y;
		}
		
		@Override
		public String toString() {
			return "(" + x + "," + y +  ")";
		}
	}
	private List<Node> open = new ArrayList<Node>();
	private Set<Node> close = new HashSet<Node>();
	
	public List<Node> find(Node start, Node end) {
		open.add(start);
		while(open.size() > 0) {
			Node current = open.iterator().next();
			if(current.equals(end))
				break;
			open.remove(current);
			close.add(current);
			for (Node node : getAroundNodes(current)) {
				node.g = Math.abs(start.x - node.x) + Math.abs(start.y - node.y);
				node.h = Math.abs(end.x - node.x) + Math.abs(end.y - node.y);
				node.f = node.g + node.h;
				node.parent = current;
				if(isBarrier(node))
					continue;
				if(close.contains(node))
					continue;
				if(open.contains(node)) {
					int newG = 0;
					if(node.x == current.x || node.y == current.y)
						newG = current.g + 10;
					else
						newG = current.g + 14;
					if(newG < node.g) {
						node.parent = current;
						node.g = newG;
						node.h = Math.abs(end.x - node.x) + Math.abs(end.y - node.y);
						node.f = node.g + node.f;
						add(open, node);
					}
				} else {
					add(open, node);
				}
			}
		}
		if(end.parent != null) {
			List<Node> result = new ArrayList<PathFinding.Node>();
			Node current = end.parent;
			while(current != null) {
				result.add(0, current);
				current = current.parent;
			}
			result.add(end);
			return result;
		}
		return null;
	}
	
	private void add(List<Node> list, Node n) {
		if(list.isEmpty()) {
			list.add(n);
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			if(node.f >= n.f) {
				list.add(i, n);
				return;
			}
		}
	}
	
	private boolean isBarrier(Node n) {
		return false;
	}
	
	private List<Node> getAroundNodes(Node n) {
		List<Node> result = new ArrayList<PathFinding.Node>();
		for(int i = n.x - 1; i <= n.x + 1; i++) {
			if(i < 0)
				continue;
			for(int j = n.y - 1; j <= n.y + 1; j++) {
				if(j < 0)
					continue;
				if(i == n.x && j == n.y)
					continue;
				result.add(new Node(i, j));
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		PathFinding finding = new PathFinding();
		Node start = new Node(0, 0);
		Node end = new Node(10, 10);
		System.out.println(finding.find(start, end));
	}
	
	
}
