package model;

import java.awt.Point;

public enum Direction {   // configured for cave origin in top left
	UP(0,-1), 
	DOWN(0,1), 
	LEFT(-1,0), 
	RIGHT(1,0);
	
	private final int dx;
	private final int dy;
	
	private Direction(int dx, int dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public Point applyMoveTo (Point start){
		//if (start==null) return null;
		return new Point(start.x+dx, start.y+dy);
	}
	
	public Direction opposite(){
		switch (this){
			case UP: return DOWN;
			case DOWN: return UP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
		}
		return null;
	}
}
