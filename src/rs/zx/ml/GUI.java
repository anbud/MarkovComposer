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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame {
	private static final long serialVersionUID = -6511180782914328417L;

	private JPanel pan, val, ctrl, main, nota, info;
	private JButton play, stop, pause, rand;
	private JTextField n1, n2;	
	private JLabel note, st, oct, imp;
	private JTextArea prev;
	private JScrollPane scr;

	private Thread player;

	private boolean state = false;

	@SuppressWarnings("deprecation")
	public GUI() {
		setTitle(Info.NAME + " " + Info.VERSION);
		setSize(new Dimension(550, 300));

		Random rnd = new Random();

		pan = new JPanel();

		pan.setLayout(new GridLayout(1, 2));

		val = new JPanel();
		ctrl = new JPanel();

		n1 = new JTextField();
		n2 = new JTextField();
		st = new JLabel("Starting notes: ");
		n1.setPreferredSize(new Dimension(40, 20));
		n2.setPreferredSize(new Dimension(40, 20));

		n1.setText(rnd.nextInt(127)+"");
		n2.setText(rnd.nextInt(127)+"");

		rand = new JButton("Randomize");
		rand.addActionListener(e -> {
			n1.setText(rnd.nextInt(127)+"");
			n2.setText(rnd.nextInt(127)+"");
		});

		val.add(st);
		val.add(n1);
		val.add(n2);
		val.add(rand);

		play = new JButton("Play");
		play.addActionListener(e -> {
			player = new Player(Integer.parseInt(n1.getText()), Integer.parseInt(n2.getText()), note, prev, oct);

			player.start();		

			state = true;

			pause.setText("Pause");
			pause.setEnabled(true);

			stop.setEnabled(true);

			rand.setEnabled(false);
			n1.setEditable(false);
			n2.setEditable(false);
		});
		stop = new JButton("Stop");
		stop.addActionListener(e -> {
			if(!state) 
				player.resume();

			player.interrupt();		
			try {
				player.join();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			state = false;

			pause.setText("Pause");
			pause.setEnabled(false);

			stop.setEnabled(false);

			rand.setEnabled(true);
			n1.setEditable(true);
			n2.setEditable(true);
		});
		pause = new JButton("Pause");
		pause.addActionListener(e -> {
			if(state) {
				player.suspend();		
				pause.setText("Resume");
				state = false;
			} else {
				player.resume();
				pause.setText("Pause");
				state = true;
			}				
		});

		pause.setEnabled(false);
		stop.setEnabled(false);
		play.setEnabled(false);

		ctrl.add(play);
		ctrl.add(pause);
		ctrl.add(stop);

		pan.add(val);
		pan.add(ctrl);

		note = new JLabel();

		prev = new JTextArea();
		prev.setEditable(false);

		main = new JPanel();

		main.setLayout(new GridLayout(1, 2, 10, 10));

		scr = new JScrollPane(prev);

		main.add(scr);

		nota = new JPanel();
		nota.setLayout(new BorderLayout());

		info = new JPanel();
		info.setLayout(new GridLayout(1, 2));

		oct = new JLabel("Octave: ");
		imp = new JLabel("Learning...");

		info.add(oct);
		info.add(imp);

		nota.add(note);
		nota.add(info, BorderLayout.SOUTH);

		main.add(nota);

		add(main);
		add(pan, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
        
	    JFileChooser dir = new JFileChooser(); 
	    dir.setCurrentDirectory(new java.io.File("."));
	    dir.setDialogTitle(Info.NAME + " - Directory chooser");
	    dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	    dir.setAcceptAllFileFilterUsed(false);
	    
	    File learningDir = null;
	    
	    if(dir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
	      learningDir = dir.getSelectedFile();
	    else { 
	    	JOptionPane.showMessageDialog(this, "You need to choose a learning directory!", "Learning error!", JOptionPane.ERROR_MESSAGE);
	    	dispose();
	    }
	    
	    int cnt = 0;
	    
		File[] files = learningDir.listFiles();		    
		for (File file : files) {
			cnt = 0;
			if (!file.isDirectory()) {
				try {
					if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("mid")) {
						new Learn(file.getCanonicalPath());
						cnt++;
					}
				} catch (IOException e1) {}
			}
		}
		
		if(cnt == 0) {
			JOptionPane.showMessageDialog(this, "No midi files found in the learning directory!", "Learning error!", JOptionPane.ERROR_MESSAGE);
	    	dispose();
		}

		Chain.normalizeChain();

		imp.setText("Learned compositions: " + Learn.getCount());
		play.setEnabled(true);
	}

}
