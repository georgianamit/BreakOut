import java.awt.Color;
import java.awt.Graphics;

public class Brick {
	int brickxpos,brickypos;
	int brickwidth=100,brickheight=50;
	
	int xoffset=140;
	int yoffset=50;
	
	int xgap=20;
	int ygap=20;
	
	public Brick(int i,int j){
		brickxpos=xoffset+brickwidth*j+xgap*j;
		brickypos=yoffset+brickheight*i+ygap*i;
	}
	
	public void drawBrick(Graphics g){
		
		
		g.fill3DRect(brickxpos, brickypos, brickwidth, brickheight, true);
		g.setColor(Color.white);
		g.drawRect(brickxpos, brickypos, brickwidth, brickheight);
		
		}

}

