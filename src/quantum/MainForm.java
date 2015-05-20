package quantum;

/**
 * @author Anoos
 * @since 29:10:2014
 * @version 1.0
 * sub program to make compression
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;

import java.io.*;
import java.util.PriorityQueue;
import javax.swing.JTextField;
public class MainForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm frame = new MainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	JButton button,button_1;
	JTextPane textPane_1;
	private JTextField textField;
	private JTextField textField_1;
	public MainForm() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 0, 414, 308);
		contentPane.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Compress File", null, panel_1, null);
		panel_1.setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(textPane_1.getText().toString()!="")
				{
					button.setEnabled(true);
					button_1.setEnabled(true);
				}
				if(textPane.getText()=="")
				{
					button.setEnabled(false);
					button_1.setEnabled(false);
				}
			}
		});
		textPane.setBackground(new Color(255, 248, 220));
		textPane.setBounds(10, 65, 261, 32);
		panel_1.add(textPane);
		JButton btnChooseFileTo = new JButton("Choose File");
		btnChooseFileTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc=new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter=new FileNameExtensionFilter("txt files only","txt"); 
				fc.setFileFilter(filter);
				int response=fc.showOpenDialog(MainForm.this);
				if(response==JFileChooser.APPROVE_OPTION)
					textPane.setText(fc.getSelectedFile().toString());
					
			}
		});
		btnChooseFileTo.setBounds(281, 65, 118, 32);
		panel_1.add(btnChooseFileTo);
		
		JLabel lblFileToCompress = new JLabel("File To Compress :");
		lblFileToCompress.setBounds(10, 31, 157, 23);
		panel_1.add(lblFileToCompress);
		
		textPane_1 = new JTextPane();
		textPane_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(textPane.getText()!="")
				{
					button.setEnabled(true);
					button_1.setEnabled(true);
				}
			}
		});
		textPane_1.setBackground(new Color(255, 248, 220));
		textPane_1.setBounds(10, 171, 261, 32);
		panel_1.add(textPane_1);
		
		JButton btnChooseDirectory = new JButton("Choose Directory");
		btnChooseDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc=new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int response=fc.showOpenDialog(MainForm.this);
				if(response==JFileChooser.APPROVE_OPTION)
					textPane_1.setText(fc.getSelectedFile().toString());
				}
		});
		btnChooseDirectory.setBounds(281, 171, 118, 32);
		panel_1.add(btnChooseDirectory);
		
		JLabel lblPathToSave = new JLabel("Path To Save :");
		lblPathToSave.setBounds(10, 137, 157, 23);
		panel_1.add(lblPathToSave);
		
		button = new JButton("Compress");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//if((new File(textPane.getText()).isFile())&&(new File(textPane_1.getText()).isFile()))
				{
					int [][]pixels=QunFuns.compressUQ(new File("C:\\Users\\Anoos\\Desktop\\anas.jpg"),new File("C:\\Users\\Anoos\\Desktop\\anas.txt"),Integer.parseInt(textField.getText()));
					int [][]pixelsCompressed=QunFuns.deCompress(new File("C:\\Users\\Anoos\\Desktop\\anas.txt"),"C:\\Users\\Anoos\\Desktop\\anas2.jpg");
					int MSE=QunFuns.MSE(pixels, pixelsCompressed);
					String f="";
					f+=MSE;
					textField_1.setText(f);
					JOptionPane.showMessageDialog(null,"File has been Compressed");
				}
				//else
					//JOptionPane.showMessageDialog(null,textPane.getText()+" it's not a file");
				
			}
		});
		button.setBounds(10, 246, 94, 23);
		panel_1.add(button);
		
		button_1 = new JButton("Decompress");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(new File(textPane.getText()).isFile())
				{
					// LZWFunctions.deCompressFile(textPane.getText(),textPane_1.getText());
					 JOptionPane.showMessageDialog(null,"File has been DeCompressed");
				}
				else
					JOptionPane.showMessageDialog(null,textPane.getText()+" it's not a file");
			}
		});
		button_1.setBounds(114, 246, 119, 23);
		panel_1.add(button_1);
		
		JLabel lblLevels = new JLabel("Levels");
		lblLevels.setBounds(243, 250, 46, 14);
		panel_1.add(lblLevels);
		
		textField = new JTextField();
		textField.setText("3");
		textField.setBounds(299, 247, 86, 20);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblMse = new JLabel("MSE");
		lblMse.setBounds(20, 214, 46, 14);
		panel_1.add(lblMse);
		
		textField_1 = new JTextField();
		textField_1.setBounds(111, 214, 86, 20);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
	}
}
