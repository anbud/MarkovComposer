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

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Learn {
	private final int NOTE_ON = 0x90;
	private static int cnt;

	static {
		cnt = 0;
	}

	public Learn(String midiName) {
		try {
			long oldTick = 0;
			
			Sequence sequence = MidiSystem.getSequence(new File(midiName));
			int tick = 60000/(120*sequence.getResolution());

			int id[] = {0, 0, 0};
			Edge nArr[][] = new Edge[2][2];

			for(Track track : sequence.getTracks()) {
				for(int i = 0; i < track.size(); i++) { 				
					MidiEvent event = track.get(i);
					MidiMessage message = event.getMessage();
					if(message instanceof ShortMessage) {
						ShortMessage sm = (ShortMessage) message;

						if(sm.getCommand() == NOTE_ON) {
							int key = sm.getData1();
							int velocity = sm.getData2();
							int pause = (int) Math.abs(event.getTick()*tick-oldTick);

							oldTick = event.getTick()*tick;
							
							if(pause == 0)
								pause = Info.NOTE_PAUSE;

							for(int j = 0; j < 2; j++) {
								if(id[j] == 2) {
									id[j] = 0;
									Chain.updateWeight(nArr[j][0].getDest()*127+nArr[j][1].getDest(), new Edge(1.0, (nArr[j][0].getVelocity()+nArr[j][1].getVelocity())/2, (nArr[j][0].getPause()+nArr[j][1].getPause())/2, key));
								} else {
									nArr[j][id[j]++] = new Edge(1.0, velocity, pause, key);
								}
							}
						}						
					}
				}
			}

			cnt++;
		} catch(InvalidMidiDataException|IOException e) {
			e.printStackTrace();
		}
	}

	public static int getCount() {
		return cnt;
	}
}
