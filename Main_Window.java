import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.List;
import javax.swing.JLabel;

public class Main_Window {

	private JFrame frame;
	private JTextField output_directory_text_field;
	private JLabel label;
	private File[] selected_files;
	private JList file_list;
	private JPanel top_panel;
	private JPanel mid_panel;
	private JComboBox file_extension_list;
	private JScrollPane selected_files_list_pane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Window window = new Main_Window();
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
	public Main_Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 594, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		top_panel = new JPanel();
		mid_panel = new JPanel();
		
		label = new JLabel("New label");
		selected_files = new File[0];
		file_list = new JList();
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(top_panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(mid_panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(top_panel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mid_panel, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
					.addContainerGap())
		);
		top_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton buttonOpen = new JButton("Open");
		buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleOpenCommand();
			}
		});
		top_panel.add(buttonOpen);
		
		JButton buttonConvert = new JButton("Convert");
		buttonConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleConvertCommand();
			}
		});
		top_panel.add(buttonConvert);
		
		file_extension_list = new JComboBox();
		file_extension_list.setModel(new DefaultComboBoxModel(new String[] {"png", "jpeg", "bmp", "gif", "tiff"}));
		top_panel.add(file_extension_list);
		
		output_directory_text_field = new JTextField();
		output_directory_text_field.setText("This is your output directory");
		top_panel.add(output_directory_text_field);
		output_directory_text_field.setColumns(16);
		
		JButton buttonSetDirectory = new JButton("Set output directory");
		buttonSetDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSetDirectoryCommand();
			}
		});
		top_panel.add(buttonSetDirectory);
		
		top_panel.add(label);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public void handleOpenCommand() {
		
		if(selected_files_list_pane != null) {
			try {//remove() can cause NullPointerException.
				mid_panel.remove(selected_files_list_pane);
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
		selected_files = new File[0];
		file_list = new JList<File>();
		selected_files_list_pane = new JScrollPane();
		mid_panel.revalidate();
		mid_panel.repaint();
		
		//create an object of JFileChooser class
		JFileChooser file_chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		//set the selection mode to directories and files.
		file_chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		//Enable file_chooser to select multiple files.
		file_chooser.setMultiSelectionEnabled(true);
		
		//Set filter to JFileChooser object so the dialog only shows one type of image.
		//Add filters for "jpeg", ""png, "bmp", "gif", "tiff"
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG", "jpg", "jpeg"));
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "png"));
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF", "gif"));
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("TIFF", "tiff"));
		
		//invoke the showsOpenDialog function to show the save dialog
		int r = file_chooser.showOpenDialog(null);
		
		 
		
		selected_files_list_pane = new JScrollPane();
		if(r == JFileChooser.APPROVE_OPTION){
			
			//Set the names of the selected files to a list shown in GUI.
			selected_files = file_chooser.getSelectedFiles(); //selected_files is File[] array.
			file_list = new JList<>(); //JList containing a list of files.
			
			//file_list.setCellRenderer(new FileRenderer(true));
			file_list.setVisibleRowCount(10);
			
			// Create the model for the JList
		    DefaultListModel model = new DefaultListModel();

		    // Add all the elements of the array "selected_files" in the model
		    for (int i=0 ; i<selected_files.length ; i++){
		    	model.addElement(selected_files[i].getName());
		    }
			
		    // Set the model to the JList
		    file_list.setModel(model);
			
		    // Make the scroll pane containing the JList.
		    selected_files_list_pane = new JScrollPane(file_list);
		    
		    // Add the scroll pane to the mid panel.
		    mid_panel.add(selected_files_list_pane);
		    mid_panel.revalidate();
		    mid_panel.repaint();
		    frame.getContentPane().add(mid_panel);
		    
			//set the label to the path of the selected directory
			label.setText("The user has selected the files.");
		}else {
			//If the user cancelled the operation
			label.setText("The user cancelled the operation.");
			
			//Handle NullPointerException when user clicks cancel in JFileChooser.
			if(selected_files_list_pane != null) {
				try {//remove() can cause NullPointerException.
					mid_panel.remove(selected_files_list_pane);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
			selected_files = new File[0];
			file_list = new JList<File>();
			selected_files_list_pane = new JScrollPane();
			mid_panel.revalidate();
			mid_panel.repaint();
		}
	}
	

	public void handleConvertCommand() {
		
		if(file_list.getModel().getSize() != 0) {
			//JList containing a list of files to be converted.
			JList<String> converted_file_list = new JList<>();
			
			//Make converted file list visible.
			converted_file_list.setVisibleRowCount(10);
			
			// Create the model for the coverted_file_list
		    DefaultListModel<String> converted_model = new DefaultListModel<>();

		    for(File file : selected_files) {
				String file_ext = (String) file_extension_list.getSelectedItem();
				String outputImagePath = makeOutputFilename(file, file_ext);
				
				try {
					boolean result = convertFormat(file.getAbsolutePath(), outputImagePath, file_ext);
					if(result) {
						converted_model.addElement(getFileNameWithoutExtension(file.getName())+ "." + file_ext + " (success)");
					}else {
						converted_model.addElement(getFileNameWithoutExtension(file.getName())+ "." + file_ext + " (failed)");
					}
				} catch (IOException e1) {
					System.out.println("Error during converting image.");
					e1.printStackTrace();
				}
			}
		    // Set the model to the JList
		    converted_file_list.setModel(converted_model);
		    
		    //Remove the selected files list from the scroll pane.
		    mid_panel.remove(selected_files_list_pane);
		    
		    //Assign the converted list to the selected files list scroll pane.
		    selected_files_list_pane = new JScrollPane(converted_file_list);
		    
		    // Add the scroll pane to the mid panel.
		    mid_panel.add(selected_files_list_pane);
		    frame.getContentPane().add(mid_panel);
		    mid_panel.revalidate();
		    mid_panel.repaint();
		}
	}
	
	public String makeOutputFilename(File file, String file_ext) {
		StringBuilder sb = new StringBuilder();
		//Get output directory.
		sb.append(output_directory_text_field.getText());
		//Add slashes
		sb.append("\\");
		//Put output filename.
		String file_name = getFileNameWithoutExtension(file.getName());
		sb.append(file_name);
		sb.append(".");
		//Put output file extension.
		sb.append(file_ext);
		//Return the absolute path of the output file. 
		return sb.toString();
	}
	
	
	
	
	public boolean convertFormat(String inputImagePath, String outputImagePath, String formatName) throws IOException {
        
		FileInputStream inputStream = new FileInputStream(inputImagePath);
        FileOutputStream outputStream = new FileOutputStream(outputImagePath);
         
        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);
	    
        // create a blank, RGB, same width and height, and a white background
	  	boolean result = false;
        try {
	  		BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	  		newBufferedImage.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);
	  		result = ImageIO.write(newBufferedImage, formatName, outputStream);
	  	}catch(NullPointerException e) {
	  		// needs to close the streams
	        outputStream.close();
	        inputStream.close();
	  		e.printStackTrace();
	  		return false;
	  	}
	  	
        // needs to close the streams
        outputStream.close();
        inputStream.close();
         
        return result;
    }
	
	
	public String getFileNameWithoutExtension(String file_name) {
		
		//Make output file directory. 
		int dot = 0;
		//Find the index of the dot from the right end of file name.
		for(int i = file_name.length()-1; i > 0; i--) {
			if(file_name.charAt(i) == '.') {
				dot = i;
			}
		}
		//Return substring of the file name.
		return file_name.substring(0,dot);
	}
	
	
	public void handleSetDirectoryCommand() {
		//create an object of JFileChooser class
		JFileChooser directory_chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		//set the selection mode to directories and files.
		directory_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		//Enable file_chooser to select multiple files.
		directory_chooser.setMultiSelectionEnabled(true);
		
		//invoke the showsOpenDialog function to show the save dialog
		int chosen_option = directory_chooser.showOpenDialog(null);
		
		if(chosen_option == JFileChooser.APPROVE_OPTION) {
			String output_directory = directory_chooser.getSelectedFile().getAbsolutePath();
			output_directory_text_field.setText(output_directory);
		}else {
			output_directory_text_field.setText("The user cancelled the operation.");
		}
		
	}
	
	
}