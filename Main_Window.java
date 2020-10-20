
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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import org.imgscalr.Scalr;


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
	private JScrollPane converted_files_list_pane;
	private JComboBox resolutions_list;
	
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
		mid_panel.setLayout(null);
		
		selected_files_list_pane = new JScrollPane();
		selected_files_list_pane.setBounds(12, 10, 249, 157);
		mid_panel.add(selected_files_list_pane);
		
		converted_files_list_pane = new JScrollPane();
		converted_files_list_pane.setBounds(273, 10, 269, 157);
		mid_panel.add(converted_files_list_pane);
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
		
		JButton buttonResize = new JButton("Resize");
		buttonResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					handleResizeCommand();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		top_panel.add(buttonResize);
		
		resolutions_list = new JComboBox();
		resolutions_list.setModel(new DefaultComboBoxModel(new String[] {"640x480","1392x1024","1600x1200","2080x1542","2580x1944","2816x2112","3264x2448","4080x3072","6464x4864"}));
		top_panel.add(resolutions_list);
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
		    selected_files_list_pane.setBounds(12, 10, 249, 157);
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
			selected_files_list_pane.setBounds(12, 10, 249, 157);
			mid_panel.revalidate();
			mid_panel.repaint();
		}
	}
	

	public void handleConvertCommand() {
		
		if(file_list.getModel().getSize() != 0) {
			
			if(converted_files_list_pane != null) {
				try {//remove() can cause NullPointerException.
					mid_panel.remove(converted_files_list_pane);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
			
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
					boolean result = convertFormat(file, outputImagePath, file_ext);
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
		    
		    //Assign the converted list to the selected files list scroll pane.
		    converted_files_list_pane = new JScrollPane(converted_file_list);
		    converted_files_list_pane.setBounds(273, 10, 269, 157);
		    // Add the scroll pane to the mid panel.
		    mid_panel.add(converted_files_list_pane);
		    
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
	
	
	
	
	public boolean convertFormat(File inputImageFile, String outputImagePath, String formatName) throws IOException {
        
		FileInputStream inputStream = new FileInputStream(inputImageFile);
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
	
	public String getFileExtension(String file_name) {
		
		//Make output file directory. 
		int dot = 0;
		//Find the index of the dot from the right end of file name.
		for(int i = file_name.length()-1; i > 0; i--) {
			if(file_name.charAt(i) == '.') {
				dot = i;
			}
		}
		//Return substring of the file name.
		return file_name.substring(dot+1);
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
	
	public String makeOutputFilename(File file, String file_ext, String resolution) {
		StringBuilder sb = new StringBuilder();
		//Get output directory.
		sb.append(output_directory_text_field.getText());
		//Add slashes
		sb.append("\\");
		//Put output filename.
		String file_name = getFileNameWithoutExtension(file.getName());
		sb.append(file_name);
		sb.append("_");
		sb.append(resolution);
		sb.append(".");
		//Put output file extension.
		sb.append(file_ext);
		//Return the absolute path of the output file. 
		return sb.toString();
	}
	
	public void handleResizeCommand() throws IOException {
		/*
		 * Just resize the image without converting to different format.
		 * 
		 */
		if(file_list.getModel().getSize() != 0) {
			//JList containing a list of files to be resized.
			JList<String> resized_file_list = new JList<>();
			
			//Make resized file list visible.
			resized_file_list.setVisibleRowCount(10);
			
			// Create the model for the resized_file_list
		    DefaultListModel<String> resized_model = new DefaultListModel<>();
		    
		    //Get the target resolution.
		    String resolution = (String) resolutions_list.getSelectedItem();
		    String[] width_height = resolution.split("x");
			int width = Integer.parseInt(width_height[0]);
			int height = Integer.parseInt(width_height[1]);
			
		    for(File file : selected_files) {
				
				String file_ext = (String) getFileExtension(file.getName());
				String outputImagePath = makeOutputFilename(file, file_ext, resolution);
				
				
				//boolean result = resizeImageWithHint(file, outputImagePath, file_ext, width, height);
				//boolean result = resizeImageUsingScaledInstance(file, outputImagePath, file_ext, width, height);
				//boolean result = resizeToWidthImgscalr(file, outputImagePath, file_ext, width, height);
				boolean result = resizeImgscalr(file, outputImagePath, file_ext, width, height);
				
				if(result) {
					resized_model.addElement(getFileNameWithoutExtension(file.getName())+ "_" + resolution + "." + file_ext + " (success)");
				}else {
					resized_model.addElement(getFileNameWithoutExtension(file.getName())+ "_" + resolution + "." + file_ext + " (failed)");
				}
				
			}
		    // Set the model to the JList
		    resized_file_list.setModel(resized_model);
		    
		    if(converted_files_list_pane != null) {
				try {//remove() can cause NullPointerException.
					mid_panel.remove(converted_files_list_pane);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
		    
		    //Assign the converted list to the selected files list scroll pane.
		    converted_files_list_pane = new JScrollPane(resized_file_list);
		    converted_files_list_pane.setBounds(273, 10, 269, 157);
		    // Add the scroll pane to the mid panel.
		    mid_panel.add(converted_files_list_pane);
		    frame.getContentPane().add(mid_panel);
		    mid_panel.revalidate();
		    mid_panel.repaint();
		}
	}

	private static boolean resizeImageWithHint(File originalFile, String outputImagePath, String formatName, int img_width, int img_height){
		
		boolean result = false;
		
		try {
			// reads input image from file
			FileInputStream inputStream = new FileInputStream(originalFile);
	        BufferedImage originalImage = ImageIO.read(inputStream);
			
	        //Get image type from originalImage object made above from file object.
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			
			//Make resized BufferedImage object from above originalImage object and arguments.
			BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, img_width, img_height, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);
			
			//Improve the quality of the image.
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			
			//Write the BufferedImage object into an output file.
			FileOutputStream outputStream = new FileOutputStream(outputImagePath);
	        result = ImageIO.write(resizedImage, formatName, outputStream);
		
			// needs to close the streams
	        outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return result;	
		
	}
	
	public boolean resizeImageUsingScaledInstance(File originalFile, String outputImagePath, String formatName, int targetWidth, int targetHeight) throws IOException {
		
		boolean result = false;
		
		try {
			// reads input image from file
			FileInputStream inputStream = new FileInputStream(originalFile);
	        BufferedImage originalImage = ImageIO.read(inputStream);
			
		    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		    
		    //Write the BufferedImage object into an output file.
			FileOutputStream outputStream = new FileOutputStream(outputImagePath);
	        result = ImageIO.write(outputImage, formatName, outputStream);
	        
	        // needs to close the streams
	        outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	
	//Resize to width
	public boolean resizeToWidthImgscalr(File originalFile, String outputImagePath, String formatName, int targetWidth, int targetHeight) {
		boolean result = false;
		
		try {
			// reads input image from file
			FileInputStream inputStream = new FileInputStream(originalFile);
	        BufferedImage originalImage = ImageIO.read(inputStream);
	        BufferedImage outputImage = Scalr.resize(originalImage, targetWidth);
	        //Write the BufferedImage object into an output file.
			FileOutputStream outputStream = new FileOutputStream(outputImagePath);
	        result = ImageIO.write(outputImage, formatName, outputStream);
	        // needs to close the streams
	        outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	//Resize to width and height. 
	public boolean resizeImgscalr(File originalFile, String outputImagePath, String formatName, int targetWidth, int targetHeight) {
		boolean result = false;
		
		try {
			// reads input image from file
			FileInputStream inputStream = new FileInputStream(originalFile);
	        BufferedImage originalImage = ImageIO.read(inputStream);
	        BufferedImage outputImage = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
	        //Write the BufferedImage object into an output file.
			FileOutputStream outputStream = new FileOutputStream(outputImagePath);
	        result = ImageIO.write(outputImage, formatName, outputStream);
	        // needs to close the streams
	        outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}