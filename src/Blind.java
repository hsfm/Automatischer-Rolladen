/*
 * Blind.java
 */


import java.awt.*; // for Graphics2D

public class Blind
{	
	private final Double xStart = 394.0;
	private final Double yStart = 378.0;
	private final Double bladeWidth = 60.0;
	private final Double bladeHeight = 15.0;
	private final Double maxBlindHeight = 13.0;
	private final Double minBlindHeight = 1.0;
	private final Double maxBladeThickness = 15.0;
	private final Double minBladeThickness = 1.0;
	private final Double amountBlades = 4.0;
	private final Double whitespace = 1.0;	
	
	/* takes care of height, thickness and drawing the blind/blades */
	public void drawBlinds(Graphics2D g2d, Double  height, Double thickness)
	{		
		Double x = this.xStart;
		Double y = this.yStart;
		Double seperators = amountBlades - 1;
		
		// check blindheight
		if(height > this.maxBlindHeight)
		{
			height = this.maxBlindHeight;
		}
		if(height < this.minBlindHeight)
		{
			height = this.minBlindHeight;
		}
		
		// check bladethickness
		if(thickness > this.maxBladeThickness)
		{
			thickness = this.maxBladeThickness;
		}
		if(thickness < this.minBladeThickness)
		{
			thickness = this.minBladeThickness;
		}				
		
		// draw blades
		for(int i = 0; i < height; i++)
		{			
			for(int j = 0; j < amountBlades; j++)
			{						
				// set blade color
				g2d.setColor(Color.lightGray);
				g2d.fillRect(x.intValue(), y.intValue(), this.bladeWidth.intValue(), thickness.intValue());
				x += this.bladeWidth;
				x += this.whitespace;
				
				// draw seperator (old code)
				if(seperators > 0)
				{
					g2d.setColor(Color.DARK_GRAY);
					g2d.drawLine(x.intValue() - 1, y.intValue(), 
							x.intValue() - 1, y.intValue() + (bladeHeight.intValue() * height.intValue()) + (height.intValue() * whitespace.intValue()) - 1);
					g2d.drawLine(x.intValue() - 1, y.intValue(), x.intValue() - 1, y.intValue() + bladeHeight.intValue());					
					seperators--;
				}				
			}										
			x = this.xStart;
			y += this.bladeHeight;
			y += this.whitespace;
		}	
	}
}
