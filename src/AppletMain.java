
import net.sourceforge.jFuzzyLogic.FIS;

import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
//import java.awt.image.RescaleOp;
//import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AppletMain extends JApplet
{
	private static final long serialVersionUID = 1;
	private Container content;
	private JPanel controls;
	private JPanel drawspace;
	private BufferedImage house;
	private BufferedImage sunimg;
	private BufferedImage calm;
	private BufferedImage windy;
	private BufferedImage stormy;
	private BufferedImage storm;
	private FIS fis;
	private double sun;
	private double daytime;
	private double weather;
	private Double blindh;
	private Double bladev;
	private JSlider sliderSun;
	private JSlider sliderDaytime;
	private JSlider sliderWeather;
	private JSlider sliderH;
	private JSlider sliderV;
	
	public void init()
	{
		String fileName = "rollo.fcl";
        fis = FIS.load(fileName,true);
        if( fis == null ) 
        { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        //fis.chart();
        fis.setVariable("Sonnenstaerke", 1);
        fis.setVariable("Tageszeit", 1);
        fis.setVariable("Wetterlage", 0);
        fis.evaluate();
        blindh = fis.getVariable("Jalousienhoehe").getLatestDefuzzifiedValue();
        bladev = fis.getVariable("Lamellenstellung").getLatestDefuzzifiedValue();        
        
        // applet init part
        
		content = getContentPane();
		content.setBackground(Color.black);
		content.setLayout(new BorderLayout());
		setSize(1210, 700);
		
		calm = loadImage("calm.png");
		windy = loadImage("windy.png");
		stormy = loadImage("stormy.png");
		storm = loadImage("storm.png");
		house = loadImage("house.png");
		sunimg = loadImage("sun.png");
	
		drawspace = new JPanel();
		drawspace.setBackground(new Color(84, 165, 231));
		content.add(drawspace, BorderLayout.CENTER);
		
		controls = new JPanel();	
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));		
		createControls();		
		content.add(controls, BorderLayout.LINE_END);
	}
		
	public void paint(Graphics g)
	{		
		drawspace.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(getWeather(), 0, 0, this);
		g2d.drawImage(house, 0, 100, this);
		setOpacity((float)sun, g2d);		
        g2d.drawImage(sunimg, 600, 50, this);                
        setOpacity(4f, g2d);
		Blind b = new Blind();		
		b.drawBlinds(g2d, blindh, bladev);		
		controls.repaint();	
	}
	
	private void createControls()
	{
		// eingabevariablen
		JPanel panelInput = new JPanel();
		panelInput.setBorder(BorderFactory.createTitledBorder("Eingabevariablen"));		
		panelInput.setLayout(new GridLayout(0, 2));	
		
		sliderSun = new JSlider(JSlider.HORIZONTAL, 0, 4, 0);
		JLabel labelSun = new JLabel("Sonnenstärke");
		sliderSun.setMajorTickSpacing(1);
		sliderSun.setMinorTickSpacing(1);
		sliderSun.setPaintTicks(true);
		sliderSun.setPaintLabels(true);
		labelSun.setLabelFor(sliderSun);
		sliderSun.addChangeListener(new MyChangeAction());
		panelInput.add(labelSun);		
		panelInput.add(sliderSun);
		
		sliderDaytime = new JSlider(JSlider.HORIZONTAL, 0, 24, 0);
		JLabel labelDaytime = new JLabel("Tageszeit");
		sliderDaytime.setMajorTickSpacing(3);
		sliderDaytime.setMinorTickSpacing(1);
		sliderDaytime.setPaintTicks(true);
		sliderDaytime.setPaintLabels(true);
		labelDaytime.setLabelFor(sliderDaytime);		
		sliderDaytime.addChangeListener(new MyChangeAction());
		panelInput.add(labelDaytime);
		panelInput.add(sliderDaytime);
		
		sliderWeather = new JSlider(JSlider.HORIZONTAL, 0, 3, 0);
		JLabel labelWeather = new JLabel("Wetterlage");
		sliderWeather.setMajorTickSpacing(1);
		sliderWeather.setMinorTickSpacing(1);
		sliderWeather.setPaintTicks(true);
		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("Ruhig") );
		labelTable.put( new Integer( 1 ), new JLabel("Bewölkt") );
		labelTable.put( new Integer( 2 ), new JLabel("Stürmisch") );
		labelTable.put( new Integer( 3 ), new JLabel("Orkan") );
		sliderWeather.setLabelTable( labelTable );
		sliderWeather.setPaintLabels(true);
		labelWeather.setLabelFor(sliderWeather);
		sliderWeather.addChangeListener(new MyChangeAction());
		panelInput.add(labelWeather);
		panelInput.add(sliderWeather);
						
		controls.add(panelInput);
		
		// ausgabevariablen
		JPanel panelOutput = new JPanel();
		panelOutput.setBorder(BorderFactory.createTitledBorder("Ausgabevariablen"));		
		panelOutput.setLayout(new GridLayout(0, 2));
		
		sliderH = new JSlider(JSlider.HORIZONTAL, 0, 21, 0);
		JLabel labelH = new JLabel("Jalousienhöhe");
		sliderH.setMajorTickSpacing(3);
		sliderH.setMinorTickSpacing(1);
		sliderH.setPaintTicks(true);
		sliderH.setPaintLabels(true);
		labelH.setLabelFor(sliderH);
		panelOutput.add(labelH);
		panelOutput.add(sliderH);
		
		sliderV = new JSlider(JSlider.HORIZONTAL, 0, 14, 0);
		JLabel labelV = new JLabel("Lamellenstellung");
		sliderV.setMajorTickSpacing(2);
		sliderV.setMinorTickSpacing(1);
		sliderV.setPaintTicks(true);
		sliderV.setPaintLabels(true);
		labelH.setLabelFor(sliderV);
		panelOutput.add(labelV);
		panelOutput.add(sliderV);
		
		controls.add(panelOutput);
				
	}
	
	private BufferedImage loadImage(String filename)
	{
		BufferedImage img = null;
		try 
		{
			URL url = new URL(getCodeBase(), filename);
			img = ImageIO.read(url);
		} catch (IOException e) { System.out.println(filename); }		
		return img;
	}
	
	private void setOpacity(float opacity, Graphics2D g) 
	{			
		opacity *= 10;
		opacity /= 40;		
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		g.setComposite(composite); // Set current alpha
	}
	
	private void update()
	{
		sun = sliderSun.getValue();
		daytime = sliderDaytime.getValue();
		weather = sliderWeather.getValue();		
		
		if(daytime < 6 | daytime > 20)
		{
			sun = 1;
		}
		if(daytime < 4 | daytime > 22)
		{
			sun = 0;
		}	
		
		fis.setVariable("Sonnenstaerke", sun);
        fis.setVariable("Tageszeit", daytime);
        fis.setVariable("Wetterlage", weather);
        fis.evaluate();
        blindh = fis.getVariable("Jalousienhoehe").getLatestDefuzzifiedValue();
        bladev = fis.getVariable("Lamellenstellung").getLatestDefuzzifiedValue();		        
        sliderH.setValue(blindh.intValue());
        sliderV.setValue(bladev.intValue());
        repaint();
	}
	
	private BufferedImage getWeather()
	{
		if(weather == 0)
		{
			return calm;
		}
		else if(weather == 1)
		{
			return windy;
		}
		else if(weather == 2)
		{
			sun = 0;
			return stormy;
		}
		else if(weather == 3)
		{
			sun = 0;
			return storm;
		}
		else
		{
			return calm;
		}
	}
	
	public class MyChangeAction implements ChangeListener
	{
		public void stateChanged(ChangeEvent ce)
		{
			update();
		}
	}
}
