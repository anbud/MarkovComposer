/*
 *  Markov Composer 0.1.1
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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package rs.zx.ml;

public class Score {
	private static double scoreMatrix[][] = new double[128*128][128];

	public static int nextNote(int n1, int n2) {
		double rnd = Math.random();
		double sum = 0.0;

		for(int i = 0; i < 128; i++) {
			sum += scoreMatrix[n1*127+n2][i];

			if(sum >= rnd)
				return i;
		}

		return (int) (rnd*127);	
	}

	public static int sumAll(int pos) {
		int sum = 0;

		for(int i = 0; i < 128; sum+=scoreMatrix[pos][i++]);

		return sum;
	}

	public static void normalizeMatrix() {
		for(int i = 0; i < 128*128; i++) {
			int sum = sumAll(i);
			if(sum != 0)
				for(int j = 0; j < 128; j++) 
					scoreMatrix[i][j] /= sum;					
		}
	}

	public static void updateWeight(int n1, int n2, int n3) {
		scoreMatrix[n1*127+n2][n3]++;
	}
}
