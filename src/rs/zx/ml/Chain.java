/*
 *  Markov Composer 0.2.4
 * 
 *  Copyright (C) 2015 - Andrej Budinčević
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package rs.zx.ml;

import java.util.HashMap;

public class Chain {
	private static HashMap<Integer, Vertex> chain;
	
	static {
		chain = new HashMap<Integer, Vertex>();
	}
	
	public static void addVertex(int key) {
		chain.put(key, new Vertex(key));
	}
	
	public static void addEdge(int key, Edge e) {
		chain.get(key).getEdges().put(e.getDest(), e);
	}
	
	public static Vertex getVertex(int key) {
		return chain.get(key);
	}
	
	public static void updateWeight(int key, Edge e) {
		if(!chain.containsKey(key)) {
			addVertex(key);			
			addEdge(key, e);
		} else if(!chain.get(key).getEdges().containsKey(e.getDest())) {
			addEdge(key, e);
		} else {
			Edge ex = chain.get(key).getEdges().get(e.getDest());
			ex.incChance();
			
			ex.updateVelocity(e.getVelocity());
			ex.addPause(e.getPause());
		}
	}
	
	public static int sumEdgeWeight(HashMap<Integer, Edge> edges) {
		int sum = 0;
		
		for(Edge e : edges.values()) {
			sum += e.getChance();
		}
		
		return sum;
	}
	
	public static void normalizeChain() {
		for(Vertex v : chain.values()) {
			int sum = sumEdgeWeight(chain.get(v.getKey()).getEdges());
			
			for(Edge e : chain.get(v.getKey()).getEdges().values()) {
				e.meanPause();
				
				e.normalizeChance(sum);
			}
		}
	}
	
	public static Edge nextEdge(int key) {
		if(chain.containsKey(key)) {
			double rnd = Math.random();
			double sum = 0.0;
			
			for(Edge e : chain.get(key).getEdges().values()) {
				sum += e.getChance();
				
				if(sum >= rnd)
					return e;
			}
			
			int t = (int) (rnd*127);
			
			if(chain.get(key).getEdges().containsKey(t)) 
				return chain.get(key).getEdges().get(t);
			else 
				return new Edge(0.0, Info.NOTE_VELOCITY, Info.NOTE_PAUSE, t);
		} else {
			int t = (int) (Math.random()*127);
			return new Edge(0.0, Info.NOTE_VELOCITY, Info.NOTE_PAUSE, t);
		}
	}
}