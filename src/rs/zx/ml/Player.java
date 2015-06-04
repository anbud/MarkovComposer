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

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Player extends Thread {
	private int n1, n2;
	private JLabel note, oct;
	private JTextArea prev;

	private final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public Player(int n1, int n2, JLabel note, JTextArea prev, JLabel oct) {
		super("Thread Player");

		this.n1 = n1;
		this.n2 = n2;
		this.note = note;
		this.prev = prev;
		this.oct = oct;
	}
	public void run() {
		try {				
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();

			final MidiChannel[] channels = synth.getChannels();

			int fn, sn, nn;

			fn = n1;
			sn = n2;

			while (!this.isInterrupted()) {
				Edge n = Chain.nextEdge(fn*127+sn);
				
				nn = n.getDest();

				int octave = (nn/12)-1;
				String noteName = NOTE_NAMES[nn%12];

				note.setText("<html><p style='font-size: 64px; width: 200px; text-align: center;'>" + noteName + "</p></html>");
				oct.setText("Octave: " + octave);
				prev.insert(noteName + octave + "\n", 0);

				channels[0].noteOn(nn, n.getVelocity());
				Thread.sleep(n.getPause());
				channels[0].noteOff(nn);

				fn = sn;
				sn = nn;
			}
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
}
