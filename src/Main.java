import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {

		// File reader
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("config.ini"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VerticalSquares = Integer.parseInt(p.getProperty("VerticalSquares"));
		int HorizontalSquares = Integer.parseInt(p.getProperty("HorizontalSquares"));

		JFrame frame = new JFrame();
		Gameplay gamePlay = new Gameplay(VerticalSquares, HorizontalSquares);

		frame.setBounds(10, 10, 700, 600);
		frame.setTitle("Volkswagen Brick Breaker");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(gamePlay);
		frame.setVisible(true);
	}
}