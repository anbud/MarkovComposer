/*
 *  Markov Composer 0.1.9
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

public class Score {
	private static Data scoreMatrix[][] = new Data[128*128][128];

	public static Note nextNote(int n1, int n2) {
		double rnd = Math.random();
		double sum = 0.0;
		
		int ind = n1*127+n2;

		for(int i = 0; i < 128; i++) {
			if(scoreMatrix[ind][i] != null)
				sum += scoreMatrix[ind][i].getChance();

			if(sum >= rnd)
				return new Note(i, scoreMatrix[ind][i].getVelocity(), scoreMatrix[ind][i].getPause());
		}

		int t = (int) (rnd*127);
		if(scoreMatrix[ind][t] != null)
			return new Note(t, scoreMatrix[ind][t].getVelocity(), scoreMatrix[ind][t].getPause());	
		else
			return new Note(t, Info.NOTE_VELOCITY, Info.NOTE_PAUSE);
	}

	public static int sumAll(int pos) {
		int sum = 0;

		for(int i = 0; i < 128; i++) {
			if(scoreMatrix[pos][i] != null) 				
				sum+=scoreMatrix[pos][i++].getChance();
		}

		return sum;
	}

	public static void normalizeMatrix() {
		for(int i = 0; i < 128*128; i++) {
			int sum = sumAll(i);
			if(sum != 0)
				for(int j = 0; j < 128; j++) {
					if(scoreMatrix[i][j] != null) {
						scoreMatrix[i][j].meanPause();
						
						scoreMatrix[i][j].setChance(scoreMatrix[i][j].getChance()/sum);		
					}
				}
		}
	}

	public static void updateWeight(int n1, int n2, Note n3) {
		int ind = n1*127+n2;
		
		if(scoreMatrix[ind][n3.getNoteId()] != null) {
			scoreMatrix[ind][n3.getNoteId()].incChance();
			
			scoreMatrix[ind][n3.getNoteId()].updateVelocity(n3.getVelocity());
			scoreMatrix[ind][n3.getNoteId()].addPause(n3.getPause());
		} else
			scoreMatrix[ind][n3.getNoteId()] = new Data(1.0, n3.getVelocity(), n3.getPause());
	}
}
