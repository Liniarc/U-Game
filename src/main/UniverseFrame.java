package main;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class UniverseFrame extends JFrame {
	
	public UniverseFrame() {
		super("Universe Clickers");
		final UniverseCanvas canvas = new UniverseCanvas();
		add(canvas);
		setResizable(false);
		pack();
		setVisible(true);
		createBufferStrategy(2);
		setLocationRelativeTo(null);
		canvas.setup();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				canvas.stop();
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		new UniverseFrame();
    }
}
