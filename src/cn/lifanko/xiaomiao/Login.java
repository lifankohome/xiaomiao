package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private static JTextField stuId;
	private static JPasswordField password;
	private JLabel tip;

	private static boolean loginDirect = false;

	public static Login frame;
	public static String config, configPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				frame = new Login();
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation((dim.width - frame.getWidth()) / 2, 200);
				frame.setVisible(true);

				javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();

				configPath = fsv.getDefaultDirectory().toString() + "\\xiaomiao-config.ini";
				config = Config.read(configPath);

				if (config.length() > 194) {
					loginDirect = true;
					stuId.setText(config.substring(171, 183));
					EncryptUtils enUtils = new EncryptUtils();
					String pass = enUtils.decrypt(config.substring(183, config.length() - 13));
					password.setText(pass);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Login.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-fewer-details@2x.png")));
		setTitle("\u767B\u5F55");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 290);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel label_2 = new JLabel("\u8003\u52E4\u7CFB\u7EDF");
		label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		panel.add(label_2);

		JLabel label_3 = new JLabel("                                                          ");
		panel.add(label_3);

		JLabel label = new JLabel("\u5B66\u53F7");
		label.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		panel.add(label);

		stuId = new JTextField();
		stuId.setColumns(10);
		panel.add(stuId);

		JLabel label_1 = new JLabel("\u5BC6\u7801");
		label_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		panel.add(label_1);

		password = new JPasswordField();
		password.setColumns(10);
		panel.add(password);

		JButton btnLogin = new JButton("  \u767B\u5F55  ");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String result;
				result = Network.login(stuId.getText(), String.valueOf(password.getPassword()));

				tip.setText("<html>" + result + "<br/>没有账号？<br/>请访问“创新开发平台”注册。</html>");

				if (result.substring(0, 4).equals("登录成功")) {
					MouseMove.start();
					frame.setVisible(false);
					if (!loginDirect) {
						EncryptUtils encryptUtils = new EncryptUtils();
						String pass = null;
						try {
							pass = encryptUtils.encrypt(String.valueOf(password.getPassword()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						Config.save(configPath, stuId.getText() + pass + ". By lifanko.");
					}
				} else {
					Config.save(configPath, stuId.getText());
				}
			}
		});
		panel.add(btnLogin);

		tip = new JLabel(
				"<html><br/>\u6CA1\u6709\u8D26\u53F7\uFF1F<br/>\u8BF7\u8BBF\u95EE\u201C<a href=\"\">\u521B\u65B0\u5F00\u53D1\u5E73\u53F0</a>\u201D\u6CE8\u518C\u3002</html>");
		tip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openURL("http://ali.lifanko.cn/idp/login");
			}
		});
		tip.setForeground(Color.DARK_GRAY);
		tip.setFont(new Font("微软雅黑 Light", Font.PLAIN, 13));
		panel.add(tip);
	}

	public static void openURL(String url) {
		java.awt.Desktop dp = java.awt.Desktop.getDesktop();
		if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
			try {
				try {
					dp.browse(new URI(url));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
