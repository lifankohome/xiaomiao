package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MouseMove implements ActionListener {

	public static JFrame f;
	public static boolean icon = false, normal = false;
	Robot robot;
	JLabel label;
	GeneralPath gp;
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	MouseMove() throws AWTException {
		robot = new Robot();

		label = new JLabel();
		gp = new GeneralPath();
		Point p = MouseInfo.getPointerInfo().getLocation();
		gp.moveTo(p.x, p.y);
		drawLatestMouseMovement();
		ActionListener al = new ActionListener() {

			Point lastPoint;

			@Override
			public void actionPerformed(ActionEvent e) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				if (!p.equals(lastPoint)) {
					gp.lineTo(p.x, p.y);
					drawLatestMouseMovement();

					Network.update();

					if (normal) {
						MinSize.trayIcon.setToolTip("状态：正常考勤\r\n姓名：" + Network.name + "\r\n学号：" + Network.stuId);
//						System.out.println("正常");
						f.setTitle("小喵喵考勤系统 v" + Network.ver + " ©lifanko 2018");
					}
					if (!normal) {
//						f.setVisible(true);
//						f.setExtendedState(Frame.NORMAL);
						f.setTitle("网络连接断开，正在尝试重新连接...");

//						System.out.println("重连中");
						MinSize.trayIcon.setToolTip("状态：正在重新连接\r\n姓名：" + Network.name + "\r\n学号：" + Network.stuId);
					}
				}
				lastPoint = p;
			}
		};
		Timer timer = new Timer(60000, al);
		timer.start();
	}

	public void drawLatestMouseMovement() {
		BufferedImage biOrig = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		BufferedImage small = new BufferedImage(biOrig.getWidth() / 4, biOrig.getHeight() / 4,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = small.createGraphics();
		g.scale(.25, .25);
		g.drawImage(biOrig, 0, 0, label);

		g.setStroke(new BasicStroke(8));
		g.setColor(Color.RED);
		g.draw(gp);
		g.dispose();

		label.setIcon(new ImageIcon(small));
	}

	public JComponent getUI() {
		return label;
	}

	public static void start() {
		Runnable r = () -> {
			JPanel ui = new JPanel(new BorderLayout(2, 2));
			ui.setBorder(new EmptyBorder(4, 4, 4, 4));
			try {
				MouseMove mmos = new MouseMove();
				ui.add(mmos.getUI());
			} catch (AWTException ex) {
				// ex.printStackTrace();
			}

			f = new JFrame("小喵喵考勤系统 v" + Network.ver + " ©lifanko 2018");
			f.setIconImage(Toolkit.getDefaultToolkit().getImage(
					Login.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-fewer-details@2x.png")));
			// quick hack to end the frame and timer
			f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			f.setResizable(false);
			f.setContentPane(ui);
			f.pack();
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation((dim.width - f.getWidth()) / 2, 200);
			f.setVisible(true);

			minimize(f);

			min(); // 登陆后直接最小化

			Network.update();
		};
		SwingUtilities.invokeLater(r);
	}

	public static void main(String[] args) throws Exception {
		Runnable r = () -> {
			JPanel ui = new JPanel(new BorderLayout(2, 2));
			ui.setBorder(new EmptyBorder(4, 4, 4, 4));

			try {
				MouseMove mmos = new MouseMove();
				ui.add(mmos.getUI());
			} catch (AWTException ex) {
				// ex.printStackTrace();
			}

			f = new JFrame("正在考勤 - 1420实验室 ©2018");
			// quick hack to end the frame and timer
			f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			f.setResizable(false);
			f.setContentPane(ui);
			f.pack();
			f.setLocationByPlatform(true);
			f.setVisible(true);

			minimize(f);
		};
		SwingUtilities.invokeLater(r);
	}

	public static void min() {
		f.setExtendedState(JFrame.ICONIFIED);
	}

	public static void minimize(JFrame f) {
		f.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {// 图标化窗口时调用事件
				f.setVisible(false);
				if (!icon) {
					MinSize.create();
					icon = true;
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
