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

public class Edge {
	private double chance;
	private int pause;
	
	private int velocity;	
	private int dest;

	public Edge(double chance, int pause, int velocity, int dest) {
		this.chance = chance;
		this.pause = pause;
		this.velocity = velocity;
		this.dest = dest;
	}

	public double getChance() {
		return chance;
	}
	
	public int getDest() {
		return dest;
	}
	
	public void normalizeChance(int sum) {
		chance /= sum;
	}

	public int getPause() {
		return pause;
	}

	public int getVelocity() {
		return velocity;
	}
	
	public void incChance() {
		this.chance++;
	}
	
	public void updateVelocity(int velocity) {
		this.velocity = velocity > this.velocity ? velocity : this.velocity;
	}
	
	public void addPause(int pause) {
		this.pause += pause;
	}
	
	public void meanPause() {
		this.pause = (int) Math.ceil(this.pause/this.chance) % 1000;
	}
}
