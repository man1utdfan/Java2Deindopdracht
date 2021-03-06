import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ParticleSystem {
	
	public static void main(String s[])
	{
		JFrame frame = new JFrame("Staart van Particles");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new ParticleSystemPanel();
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}

class ParticleSystemPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = -8271945508104460591L;
	private int x=0,y=0;
	private CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<Particle>();
	private boolean drukMuis; 
	private int particleCount;
	private FPScounter fpsCounter;
	private boolean kleur;
	private boolean normaal;
	

	/* Constructor */
	public ParticleSystemPanel()
	{
		fpsCounter = new FPScounter();
		setBackground(Color.black);
		setPreferredSize( new Dimension(900,700));
		
		Timer timer = new Timer(1000/30, this);
		timer.start();
		addMouseListener(this);
		addMouseMotionListener(this);
		gameDo();
		normaal = true;
	}

	private void gameDo() {

		new Timer(1000/200, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(drukMuis) {
				particles.add(new Particle(x-15,y-12,kleur, normaal));
				particles.add(new Particle(x-15,y-12,kleur, normaal));
				particles.add(new Particle(x-15,y-12,kleur, normaal));
				particles.add(new Particle(x-15,y-12,kleur, normaal));
				particleCount+=4;
				}
				
			}
		}).start();
		
	}

	public void actionPerformed(ActionEvent arg0)
	{
		synchronized (particles) {
		ArrayList<Particle> tempArrayList = new ArrayList<Particle>();	
		
		for( Iterator<Particle> itr = particles.iterator(); itr.hasNext(); )
		{
			Particle pete = itr.next();

			if(pete.dead() || pete.getX()+35<0 || pete.getY()+35<0)
			{
				tempArrayList.add(pete);
//				particleCount-=1;
//				System.out.println("Kill!");
			}
			else
			{
				pete.update();
			}
			if(tempArrayList.size() >= 100) {
				particleCount-=tempArrayList.size();
				particles.removeAll(tempArrayList);
				tempArrayList.clear();
			}
		}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		synchronized (particles) {
		for(Particle p : particles)
		{
			p.draw(g);
		}	
		}
		
		g.setColor(Color.green);
		g.drawString(fpsCounter.tellFPS(), 1, 10);
		g.drawString(fpsCounter.maxFPS(),1, 24);
		g.drawString("Het aantal paricles is: "+particleCount,1, 38);
		
		fpsCounter.tick();
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getButton()==2&&normaal)normaal=false;
		else normaal=true;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==1)kleur=true;
		if(e.getButton()==3)kleur=false;
		drukMuis=true;
		 x = e.getX();
		 y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==1)drukMuis = false;
		if(e.getButton()==2)drukMuis=false;
		if(e.getButton()==3)drukMuis = false;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		 x = arg0.getX();
		 y = arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	   
}