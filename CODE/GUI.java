import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.TextArea;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.TextField;
import javax.swing.JEditorPane;

public class GUI {

	private JFrame frame;
	private static JTextField textField;
	private JTextField textField_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 782, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.setBounds(17, 439, 131, 31);
		frame.getContentPane().add(btnAdd);
		
		textField = new JTextField();
		textField.setBounds(17, 391, 431, 29);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnFind = new JButton("FIND");
		btnFind.setBounds(169, 439, 131, 31);
		frame.getContentPane().add(btnFind);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setBounds(317, 439, 131, 31);
		frame.getContentPane().add(btnDelete);
		
		JButton btnEdit = new JButton("EDIT");
		btnEdit.setBounds(465, 439, 131, 31);
		frame.getContentPane().add(btnEdit);
		
		textField_1 = new JTextField();
		textField_1.setBounds(38, 39, 669, 330);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
	}
	
	public static void PrintText(String Text){
		textField.setText(Text);
	}
}
