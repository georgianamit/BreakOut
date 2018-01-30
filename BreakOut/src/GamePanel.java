import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
	int width=1220;
	
	ScoreBoard s;
	
	
	public GamePanel(){
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		s = new ScoreBoard();
		MainPanel p = new MainPanel();
		this.add(s);
		this.add(p);
		
	}
	
	
	
	public class ScoreBoard extends JPanel{
		int score=0;
		int life =3;
		
		public ScoreBoard(){
			this.setPreferredSize(new Dimension(width,180));
			this.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
			this.setBackground(Color.blue);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Times new roman",Font.PLAIN,100));
			g.drawString("BreakOut", 430, 120);
			
			g.setFont(new Font("Times new roman",Font.PLAIN,50));
			g.drawString("Score: "+score, 1000, 140);
			g.drawString("Life: "+life, 20, 140);
		}
		
		void refresh(){
			repaint();
		}
	}
	
	public class MainPanel extends JPanel implements ActionListener,KeyListener{
		
		Timer t;
		
		int width=1220,height=780;
		
		//bat
		int batxpos=500,batypos=740;
		int batxvel=0;
		int batwidth=130,batheight=15;
		
		//ball

		int balldia=30;
		int ballxpos= (batxpos+(int)(batwidth/2) - (balldia/2)),ballypos=batypos-balldia-3;
		int ballxvel=0,ballyvel=0;
		
		
		//bricks
		int rows=4,cols=8;
		Brick b[][] =  new Brick[rows][cols];
		int brickvalue[][] = new int[rows][cols];
		int bricksleft=rows*cols;
		
		//game variables
		boolean gameover=false;
		boolean startgame=false;
		boolean winner=false;
		
		boolean batcollide=false;
		
		
		public MainPanel(){
			this.setPreferredSize(new Dimension(width,height));
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
			this.setBackground(Color.cyan);
			t = new Timer(1,this);
			setValue();
			addKeyListener(this);
			setFocusable(true);
			this.setFocusTraversalKeysEnabled(false);
			
			t.start();
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			drawBrickMap(g);
			
			g.setColor(Color.gray);
			g.fill3DRect(batxpos, batypos, batwidth, batheight, true);
			g.setColor(Color.BLACK);
			g.drawRect(batxpos, batypos, batwidth, batheight);
			
			
			
			g.setColor(Color.BLUE);
			g.fillOval(ballxpos, ballypos, balldia, balldia);
			
			
			if(!startgame && !gameover){
				drawStartGame(g);
			}
			
			if(winner){
				drawOptions(g);
				win(g);
				t.stop();
			}
			
			if(gameover){
			drawOptions(g);
			showGameover(g);
			}
			
			
		}
		
		public void drawBrickMap(Graphics g){
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(brickvalue[i][j] == 1){
					switch((int)i%2){
					case 0: g.setColor(Color.orange);
					break;
					case 1: g.setColor(Color.GREEN);
					break;
					}
					b[i][j].drawBrick(g);
					}
				}
			}
		}
		
		public void setValue(){
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					brickvalue[i][j]=1;
					b[i][j] = new Brick(i,j);
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ballxpos += ballxvel;
			ballypos += ballyvel;
			
			batxpos+=batxvel;
			
			if(batxpos < 0 || batxpos+batwidth > width){
				batxvel =0;
			}
			
			if(ballxpos < 0 || ballxpos+balldia >= width){
				ballxvel = -ballxvel;
			}
			if(ballypos < 0){
				ballyvel = -ballyvel;
			}
			if(ballypos >=  height){
			
				s.life--;
				if(s.life ==0){
				gameover=true;
				startgame=false;
				t.stop();
				}else{
					batxpos=500;	batypos=740;
					ballxpos= (batxpos+(int)(batwidth/2) - (balldia/2));
					ballypos=batypos-balldia-3;
					ballxvel=0;ballyvel=0;
					startgame=false;
				}
				
				
			}
			
			Ellipse2D.Double ball = new Ellipse2D.Double(ballxpos, ballypos, balldia, balldia);
			Rectangle2D.Double bat=new Rectangle2D.Double(batxpos,batypos,batwidth,batheight);

			
			if(!batcollide){
			if(((ballypos+balldia) == batypos) &&
					(ballxpos+(balldia/2)) >= batxpos &&
					((ballxpos+(balldia/2)) <= (batxpos+batwidth))){
					System.out.println("bat verticel");
					ballyvel = -ballyvel;
					batcollide = true;
				}else{
					if(ball.intersects(bat)){
						if(ballxpos < (batxpos+batwidth/2) && ballypos < (batypos+batheight)){
							
							if(ballxvel < 0 && ballyvel > 0){
								ballyvel = -ballyvel;
								System.out.println("bat vertical");
								
							}else
							if(ballxvel > 0 && ballyvel > 0){
								ballyvel = -ballyvel;
								ballxvel = -ballxvel;
								System.out.println("bat diagonal");
								
							}
						}else
						if(ballxpos > (batxpos+batwidth/2) && ballypos < (batypos+batheight)){
							if(ballxvel < 0 && ballyvel > 0){
								ballyvel = -ballyvel;
								ballxvel = -ballxvel;
								System.out.println("bat diagonal");
								
							}else
							if(ballxvel > 0 && ballyvel > 0){
								ballyvel = -ballyvel;
								System.out.println("bat vertical");
								
							}
						}
					batcollide = true;
					}
				}
			}
			
			if(batcollide){
				if(!ball.intersects(bat)){
					batcollide=false;
				}
			}
					
			
	A:		for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(brickvalue[i][j] == 1){
				Rectangle2D.Double br = new Rectangle2D.Double(b[i][j].brickxpos, b[i][j].brickypos, b[i][j].brickwidth, b[i][j].brickheight);
				
					if(((ballypos+balldia) == b[i][j].brickypos) &&
							(ballxpos+(balldia/2)) >= b[i][j].brickxpos &&
							((ballxpos+(balldia/2)) <= (b[i][j].brickxpos+b[i][j].brickwidth))){
							System.out.println("pure vertical");
							ballyvel = -ballyvel;
							update(i,j);
							break A;
							
						}else
						if((((ballypos) == (b[i][j].brickypos+b[i][j].brickheight)) &&
							(ballxpos+(balldia/2)) >= b[i][j].brickxpos) &&
							((ballxpos+(balldia/2)) <= (b[i][j].brickxpos+b[i][j].brickwidth))){
							System.out.println("pure vertical");
							ballyvel = -ballyvel;
							update(i,j);
							break A;
						}else
						if(((ballxpos+balldia) == b[i][j].brickxpos) && 
							((ballypos+(balldia/2)) >= b[i][j].brickypos) &&
							((ballypos+(balldia/2)) <= (b[i][j].brickypos+b[i][j].brickheight))){
							System.out.println("pure horizontal");
							ballxvel = -ballxvel;
							update(i,j);
							break A;
						}else
						if(((ballxpos) == (b[i][j].brickxpos+b[i][j].brickwidth))&&
							((ballypos+(balldia/2)) >= b[i][j].brickypos) &&
							((ballypos+(balldia/2)) <= (b[i][j].brickypos+b[i][j].brickheight))){
							System.out.println("pure horizontal");
							ballxvel = -ballxvel;
							update(i,j);
							break A;
						}else
							if(ball.intersects(br)){
							if(ballxpos < (b[i][j].brickxpos+b[i][j].brickwidth/2) && ballypos < (b[i][j].brickypos+b[i][j].brickheight/2)){
								if(ballxvel > 0 && ballyvel < 0){
									System.out.println("horizontal");
									ballxvel = -ballxvel;
									update(i,j);
									break A;
								}else
								if(ballxvel < 0 && ballyvel > 0){
									ballyvel = -ballyvel;
									System.out.println("vertical");
									update(i,j);
									break A;
								}else
								if(ballxvel > 0 && ballyvel > 0){
									ballyvel = -ballyvel;
									ballxvel = -ballxvel;
									System.out.println("diagonal");
									update(i,j);
									break A;
								}
							}else
							if(ballxpos > (b[i][j].brickxpos+b[i][j].brickwidth/2) && ballypos < (b[i][j].brickypos+b[i][j].brickheight/2)){
								if(ballxvel < 0 && ballyvel > 0){
									ballyvel = -ballyvel;
									ballxvel = -ballxvel;
									System.out.println("diagonal");
									update(i,j);
									break A;
								}else
								if(ballxvel > 0 && ballyvel > 0){
									ballyvel = -ballyvel;
									System.out.println("vertical");
									update(i,j);
									break A;
								}else
								if(ballxvel < 0 && ballyvel < 0){
									System.out.println("horizontal");
									ballxvel = -ballxvel;
									update(i,j);
									break A;
								}
							}else
							if(ballxpos > (b[i][j].brickxpos+b[i][j].brickwidth/2) && ballypos > (b[i][j].brickypos+b[i][j].brickheight/2)){
								if(ballxvel > 0 && ballyvel < 0){
									System.out.println("vertical");
									ballyvel = -ballyvel;
									update(i,j);
									break A;
								}else
								if(ballxvel < 0 && ballyvel > 0){
									ballxvel = -ballxvel;
									System.out.println("horizontal");
									update(i,j);
									break A;
								}else
								if(ballxvel < 0 && ballyvel < 0){
									ballyvel = -ballyvel;
									ballxvel = -ballxvel;
									System.out.println("diagonal");
									update(i,j);
									break A;
								}
							}else
							if(ballxpos < (b[i][j].brickxpos+b[i][j].brickwidth/2) && ballypos > (b[i][j].brickypos+b[i][j].brickheight/2)){
								if(ballxvel > 0 && ballyvel < 0){
									ballyvel = -ballyvel;
									ballxvel = -ballxvel;
									System.out.println("diagonal");
									update(i,j);
									break A;
								}else
								if(ballxvel > 0 && ballyvel > 0){
									ballxvel = -ballxvel;
									System.out.println("horizontal");
									update(i,j);
									break A;
								}else
								if(ballxvel < 0 && ballyvel < 0){
									System.out.println("vertical");
									ballyvel = -ballyvel;
									update(i,j);
									break A;
								}
							}
						}
				}
			}
			
				if(bricksleft <= 0){
					winner=true;
					t.stop();
				}
				
			repaint();
			s.refresh();
			
		}
	}
		
		void update(int i, int j){
			brickvalue[i][j]=0;
			s.score += 5;
			bricksleft--;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key=e.getKeyCode();
			if(!startgame && !gameover){
			if(key == KeyEvent.VK_SPACE){
				startgame=true;
				ballxvel=1;ballyvel=1;
				t.start();
			}
			}
			if(!startgame){
			if(key==KeyEvent.VK_LEFT){
				if(batxpos >= 0 ){
				batxvel=-1;
				ballxvel=-1;
				}else{
					batxvel=0;
					ballxvel=0;
				}
			}
			if(key == KeyEvent.VK_RIGHT){
				if(batxpos+batwidth <= width){
				batxvel=1;
				ballxvel = 1;
				}else{
					batxvel=0;
					ballxvel=0;
				}
			}
			}
			
			if(startgame){
				if(key==KeyEvent.VK_LEFT){
					if(batxpos >= 0 ){
					batxvel=-1;
					
					}else
						batxvel=0;
				}
				if(key == KeyEvent.VK_RIGHT){
					if(batxpos+batwidth <= width){
					batxvel=1;
					
					}else
						batxvel=0;
				}
			}
			
			if(key == KeyEvent.VK_ESCAPE){
				System.exit(0);
			}
			
			if(key == KeyEvent.VK_R){
			
				batxpos=500;	batypos=740;
			
				
				
				ballxpos= (batxpos+(int)(batwidth/2) - (balldia/2));
				ballypos=batypos-balldia-3;
				ballxvel=0;		ballyvel=0;
				
				bricksleft=rows*cols;
				
				gameover=false;
				startgame=false;
				winner=false;
				setValue();
				s.life=3;
				s.score=0;
				s.refresh();
				t.start();
			}
			
			repaint();
			
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if(!startgame){
				batxvel=0;
				ballxvel=0;
				}else
					batxvel=0;
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}
		
		void showGameover(Graphics g){
			
			g.setColor(Color.orange);
			g.setFont(new Font("Times new roman",Font.PLAIN,48));
			g.drawString("GAME OVER", width/2-170, height/2 + 10);
			g.setColor(Color.GREEN);
			g.setFont(new Font("Times new roman",Font.BOLD,48));
			g.drawString("SCORE : "+ s.score, width/2-150, height/2 + 60);
			
		}
		
		void win(Graphics g){
			
				g.setColor(Color.ORANGE);
				g.setFont(new Font("Times new roman",Font.ITALIC,40));
				g.drawString("You Have Cleared The Game.", width/2-260, height/2 - 120);
				g.setColor(Color.GREEN);
				g.setFont(new Font("Times new roman",Font.BOLD,48));
				g.drawString("SCORE : "+ s.score, width/2-150, height/2 - 70);
			
		}
		
		public void drawOptions(Graphics g){
			g.setColor(Color.orange);
			g.setFont(new Font("Times new roman",Font.ITALIC,42));
			g.drawString("Press [ESC] to exit the game", width/2-260, height/2 + 120);
			g.drawString("Press [R] to restart the game", width/2-260, height/2 + 170);
		}
		
		public void drawStartGame(Graphics g){
			g.setColor(Color.blue);
			g.setFont(new Font("Times new roman",Font.ITALIC,42));
			g.drawString("Press [SPACE] to start the game", width/2-280, height/2 + 30);
		}
	}
}
