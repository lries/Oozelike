package oozelike.roguelike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pathfinder {
	/**********************************************************************************
	 * Find an A* path. 
	 * @param d the dungeon to pathfind in
	 * @param sx start x
	 * @param sy start y
	 * @param gx goal x
	 * @param gy goal y
	 * @param walker the walking pattern to check
	 * @param checkActors whether to check for actors obstructing movement
	 * @param ignoreGoalActors whether to check if an actor is obstructing the goal
	 **********************************************************************************/
	public static List<int[]> findPathAStar(Dungeon d, int sx, int sy, int gx, int gy, WalkType walker, boolean checkActors, boolean ignoreGoalActors){
		List<Node> frontier = new ArrayList<Node>();
		Node start = new Node(sx, sy);
		Node end = new Node(gx, gy);
		start.value = start.heuristic(end);
		start.cost = 0;
		frontier.add(start);
		List<Node> visited = new ArrayList<Node>();
		int n = 0; //tracks how many nodes have been opened
		Node node;

		while (!frontier.isEmpty()){
			//grabs first in queue
			node = frontier.remove(0);
			n++;
			visited.add(node);
			if (node.equals(end)){
				break;
				}
			boolean check = checkActors && (node.x != gx || node.y != gy || !ignoreGoalActors); 
			List<Node> l2 = node.neighbors(d, walker, check, end); //gets neighbors
			for (Node l : l2){
				l.Par = node;
				if (l.x==node.x | l.y==node.y) l.cost = node.cost + 1;
				else l.cost = node.cost + Math.sqrt(2);
				l.value = l.heuristic(end);
				boolean go = true;
				for (Node nu : visited){
					if (nu.x == l.x && nu.y == l.y) { 
						go = false;
					}
				}
				if (go && !frontier.contains(l)){
					//places the node in the correct place in the queue
					boolean added = false;
					for (int i=0; i<frontier.size(); i++){
						if (l.cost+l.value < frontier.get(i).cost+frontier.get(i).value){
							frontier.add(i, l);
							added = true;
							break;
						}
					}
					if (!added) {
						frontier.add(l);
					}
				}
			}
			if (n > 50000){
				System.out.println("Node limit reached"); 
				return null;
				}
		}
		System.out.println(start.toString()+" "+end.toString());
		return Backwards(visited.get(visited.size()-1), start);

	}

	/***************************************************
	 * Trace back through visited nodes to find a path.
	 * @param visits the visit list from e.g. A*
	 * @param start the start of the path.
	 * @return a List<int[]> path
	 ***************************************************/
	public static List<int[]> Backwards(Node end, Node start){
		//returns path from Start to Goal as a List.  simply traces back through the visited nodes and finds the ideal path.
		List<int[]> bw = new ArrayList<int[]>();
		Node c = end; 
		while (c!= null && c.Par!=null && (c.x!=start.x || c.y!=start.y)){
			int[] point = {c.x, c.y}; 
			bw.add(point);
			c = c.Par;
		}
		Collections.reverse(bw);
		for (int[] element: bw){
			System.out.println(element.length+" "+element[0]+" "+element[1]);
		}
		return bw;
	}

	/*********************************************
	 * Node: a private class used for pathfinding
	 *********************************************/
	private static class Node{
		//Private class used for pathfinding. A node is just a coordinate, but with pathfinding cost, value, and a parent. 
		public int x;
		public int y; 
		public double cost;
		public double value;
		public Node Par;
		public Node(int x, int y){
			this.x = x; 
			this.y = y; 
			this.cost = 0;
			this.value = 0; //heuristic value of a node - kept because recalculating it all the time is expensive! :V
			this.Par=null;
		}

		/******************************************************
		 * Find all walkable neighbors
		 * @param floorMap the map to find on
		 * @param w the WalkType of the mover
		 * @param checkActors whether to check for occupancy
		 * @return a list of neighbors 
		 ******************************************************/
		public List<Node> neighbors(Dungeon floorMap, WalkType w, boolean checkActors, Node goal) {
			//Neighbor finder! Needs to start factoring in for creatures :V
			List<Node> ret = new ArrayList<Node>();
			for (int ix=Math.max(0,x-1); ix<=Math.min(floorMap.getWidth()-1, x+1); ix++){
				for (int iy=Math.max(0, y-1); iy<=Math.min(floorMap.getHeight()-1, y+1); iy++){
					if (ix == x && iy == y) continue; 
					if (floorMap.getActor(ix, iy) != null && checkActors && !this.equals(goal)) {
						continue; 
					}
					if (floorMap.getTile(ix,iy).canWalk(w)) ret.add(0, new Node(ix, iy));
				}
			}
			return ret;
		}

		/***************************************************************
		 * Distance heuristic
		 * @param goal the goal node
		 * @return the heuristic estimated length between these points
		 ***************************************************************/
		public int heuristic(Node goal){
			return (int) (Math.sqrt((goal.x-x)*(goal.x-x) + (goal.y-y)*(goal.y-y)));
		}

		@Override
		/***************************************
		 * Make the node pretty to print
		 * @return a string of the form (x, y)
		 ***************************************/
		public String toString(){
			//method for node-to-string conversion
			return "("+x+","+y+")";
		}

		@Override
		/***********************************************
		 * Check equality
		 * Returns true if the x and y values are equal
		 * Does NOT consider parentage, etc. 
		 * @param o the other node to check
		 * @return whether they are equal
		 ***********************************************/
		public boolean equals(Object o) { 
			//returns true if the x and y values are equal
			if (!(o instanceof Node)) {
				return false;
			}
			Node n = (Node) o;
			return (this.x == n.x && this.y == n.y);
		}

	}

}
