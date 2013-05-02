/*
 * Blade.java
 */

import java.awt.*;

public class Blade 
{
	private Graphics2D blade;
	private double x;
	private double y;
	private double value;
	private final double bladeWidth = 60;
	
	public Blade(Graphics2D b, double x, double y, double v)
	{
		this.blade = b;
		this.x = x;
		this.y = y;
		this.value = v;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void setValue(double v)
	{
		this.value = v;
	}
	
	public void draw()
	{
		Double w = this.bladeWidth;
		Double v = this.value;
		Double x = this.x;
		Double y = this.y;
		blade.fillRect(x.intValue(), y.intValue(), w.intValue(), v.intValue());
	}
}
