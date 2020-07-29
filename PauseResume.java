package threadtestingpauseLock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

public class PauseResume extends JFrame{

    private JButton button = new JButton("Start");

    private JPanel box = new JPanel();
    
    private Object lock = new Object();
    private volatile boolean paused = true;
    
	public static final int BALL_DIAMETER = 200;
	public static final int X_BALL = 100;
	public static final int Y_BALL = 100;
    
	
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PauseResume();
            }
        });
    }
    
    public PauseResume() {
        super("Rotating Ball Demo");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.white);
		setLayout(new BorderLayout());
		box = new JPanel();
		add(box, BorderLayout.CENTER);
        
        button.addActionListener(pauseResume);
        counter.start();

        add(button, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private Thread counter = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                work();
            }
        }
    });
    
    private void work() {
    	int i = 0;
        while(true) {
            allowPause();
            etch(i);
            i++;
            sleep();
        }
    }
    
    private void allowPause() {
        synchronized(lock) {
            while(paused) {
                try {
                    lock.wait();
                } catch(InterruptedException e) {
                    // nothing
                }
            }
        }
    }
    
    private java.awt.event.ActionListener pauseResume =
        new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                    paused = !paused;
                    button.setText(paused?"Resume":"Pause");
                synchronized(lock) {
                    lock.notifyAll();
                }
            }
        };
    
    private void sleep() {
        try {
            Thread.sleep(150);
        } catch(InterruptedException e) {
            // nothing
        }
    }

	public void etch(int i) {
		Graphics g = box.getGraphics();
		
				int adjust = 30 * i;
				int bluepoint = -90 + adjust;
				int greenpoint = 0 + adjust;
				int orangepoint = 90 + adjust;

				g.setColor(Color.RED);
				g.fillOval(X_BALL, Y_BALL, BALL_DIAMETER, BALL_DIAMETER);
				g.setColor(Color.ORANGE);
				g.fillArc(100, 100, 200, 200, orangepoint, -90);
				g.setColor(Color.GREEN);
				g.fillArc(100, 100, 200, 200, greenpoint, -90);
				g.setColor(Color.BLUE);
				g.fillArc(100, 100, 200, 200, bluepoint, -90);
		      }
	}

