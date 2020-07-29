import java.awt.event.*;
import java.net.URI;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private boolean play = false;
	private int score = 0;
	private int totalBricks;
	private int v;
	private int h;
	private Timer timer;
	private int delay = 8;
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private MapGenerator map;

	public Gameplay(int v, int h) {

		map = new MapGenerator(v, h);
		totalBricks = v * h;
		this.v = v;
		this.h = h;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {

		// Background
		g.setColor(Color.white);
		g.fillRect(1, 1, 692, 592);
		Image img = new ImageIcon("man_digital_hub.png").getImage();
		g.drawImage(img, 80, 50, 540, 145, null);

		// Draw the map (more specifically the bricks, with their number according to
		// config.ini file)
		map.draw((Graphics2D) g);

		// Borders (Left, Right and Top of the game region)
		g.setColor(Color.red);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 698, 3);
		g.fillRect(698, 0, 3, 592);

		// Scoreboard
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);

		// Paddle (The player)
		g.setColor(Color.black);
		g.fillRect(playerX, 550, 200, 8);

		// Ball
		Image vw = new ImageIcon("vw.png").getImage();
		g.drawImage(vw, ballposX, ballposY, 30, 30, null);

		// In case of Winning the game
		if (totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.black);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Welcome to the world of", 150, 300);
			g.drawString("MAN |", 180, 330);
			g.setColor(Color.red);
			g.drawString("Digital Hub", 280, 330);

			// Direct to MAN | Digital Hub main web page via default browser
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://vwds.pt/man.html"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// In case of losing the game
		if (ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);

			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press (Enter) to Restart", 230, 350);
		}

		g.dispose();
	}

	// Key events to control the game
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 500) {
				playerX = 500;
			} else {
				moveRight();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 10) {
				playerX = 0;
			} else {
				moveLeft();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = v * h;
				map = new MapGenerator(v, h);
				repaint();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void moveRight() {
		play = true;
		playerX += 40;
	}

	public void moveLeft() {
		play = true;
		playerX -= 40;
	}

	// Check collision between ball and paddle
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (play) {
			if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(playerX, 550, 60, 8))) {
				ballYdir = -ballYdir;
				ballXdir = -2;
			} else if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(playerX + 140, 550, 60, 8))) {
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			} else if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(playerX + 60, 550, 80, 8))) {
				ballYdir = -ballYdir;
			}

			// Check collision between ball and map borders
			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 30, 30);
						Rectangle brickRect = rect;

						if (ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							score += 5;
							totalBricks--;

							// Collision with left or right side of the brick
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}
							// Collision with top or bottom side of the brick
							else {
								ballYdir = -ballYdir;
							}

							break A;
						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			if (ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {
				ballXdir = -ballXdir;
			}

			repaint();
		}
	}
}