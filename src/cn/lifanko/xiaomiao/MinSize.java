package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MinSize implements ActionListener {
	public static void main(String[] args) {
		create();
	}

	public static TrayIcon trayIcon;

	public static void create() {
		if (SystemTray.isSupported()) {// 判断是否支持系统托盘

			URL url = MinSize.class.getResource("kitty.png");// 获取图片所在的URL

			ImageIcon icon = new ImageIcon(url);// 实例化图像对象

			Image image = icon.getImage();// 获得Image对象

			trayIcon = new TrayIcon(image);// 创建托盘图标

			trayIcon.addMouseListener(new MouseAdapter() {// 为托盘添加鼠标适配器

				public void mouseClicked(MouseEvent e) {// 鼠标事件
					if (e.getClickCount() == 2) {// 判断是否双击了鼠标
						MouseMove.f.setVisible(true);
						MouseMove.f.setExtendedState(Frame.NORMAL);
					}
				}
			});

			trayIcon.setToolTip("状态：" + Network.status + "\r\n姓名：" + Network.name + "\r\n学号：" + Network.stuId);// 添加工具提示文本

			// 创建弹出菜单
			PopupMenu popupMenu = new PopupMenu();

			MenuItem main = new MenuItem("显示鼠标轨迹");
			MenuItem login = new MenuItem("我的在线时长");
			MenuItem IDP = new MenuItem("考勤系统主页");
			MenuItem list = new MenuItem("考勤排名");
			MenuItem exit = new MenuItem("退出");
			popupMenu.add(main);
			popupMenu.add(login);
			popupMenu.addSeparator();
			popupMenu.add(list);
			popupMenu.add(IDP);
			popupMenu.addSeparator();
			popupMenu.add(exit);

			trayIcon.setPopupMenu(popupMenu);// 为托盘图标加弹出菜弹

			SystemTray systemTray = SystemTray.getSystemTray();// 获得系统托盘对象
			try {
				systemTray.add(trayIcon);// 为系统托盘加托盘图标
			} catch (Exception e) {
				// e.printStackTrace();
			}
			main.addActionListener(e -> {
				MouseMove.f.setVisible(true);
				MouseMove.f.setExtendedState(Frame.NORMAL);
			});
			login.addActionListener(e -> JOptionPane.showMessageDialog(null, "<html>" + Network.timeOnline + "<html>",
					"我的在线时长", JOptionPane.INFORMATION_MESSAGE));
			list.addActionListener(e -> Login.openURL("http://ali.lifanko.cn/idp/miao/"));
			IDP.addActionListener(e -> Login.openURL("http://ali.lifanko.cn/idp/admin/person.php"));
			exit.addActionListener(e -> System.exit(0));
		} else {
			JOptionPane.showMessageDialog(null, "无法创建托盘菜单，请使用“任务管理器”关闭程序！");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
