import java.io.*; 
import javax.swing.*; 
import java.awt.event.*; 
import javax.swing.filechooser.*;

public class Image2Image extends JFrame implements ActionListener {
	
	// Label to show the files user selects 
    static JLabel label;
    static JLabel label2;
    static JList file_list;
    static JTextField text_field;
    static JComboBox<String> file_extension_list;
    static File[] selected_files;
    static JPanel panel;

    // a default constructor 
    Image2Image(){ 
    } 
	
	public static void main(String[] args) {
		
		// frame to contains GUI elements 
		JFrame frame = new JFrame("file chooser to select directories"); 
		
		// set the size of the frame 
		frame.setSize(400, 400);
		
		// set the frame's visibility
		frame.setVisible(true);
		
		// Close when exit button is pressed.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		//Button to open open dialog
		JButton buttonOpen = new JButton("open");
		
		//Button to convert the jpeg file to the png file.
		JButton buttonConvert = new JButton("convert");
		
		//make an object of the class JpegToPng
		Image2Image m2m = new Image2Image();
		
		//Add an action listener to each button to capture user response.  
		buttonOpen.addActionListener(m2m);
		buttonConvert.addActionListener(m2m);
		
		//Make a text field to specify the file extension of the converted image.
		text_field = new JTextField("Type in file extension e.g png, jpeg",26);
		
		//Make a drop-down menu using a ComboBox.
		//List output file extensions.
		String[] file_extensions = {"png", "jpeg", "bmp", "gif", "tiff"};
		file_extension_list = new JComboBox<String>(file_extensions);
		file_extension_list.setSelectedIndex(0);
		file_extension_list.addActionListener(m2m);
		
		//make a panel to add the buttons and labels
		panel = new JPanel();
		
		//add the button to the frame.
		panel.add(buttonOpen);
		panel.add(buttonConvert);
		panel.add(file_extension_list);
		
		//Add the text field to the panel.
		panel.add(text_field);
		
		//Set the label to its initial value
		label = new JLabel("no file selected");
		label2 = new JLabel();
		file_list = new JList();
		
		//add panel to the frame
		panel.add(label);
		panel.add(label2);
		frame.add(panel);
		
		frame.setVisible(true);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//If the user presses the save button, show the save dialog.
		String com = e.getActionCommand();
		
		if(com.equals("open")) {
			handleOpenCommand();
		}else if(com.equals("convert")) {
			handleConvertCommand();
		}
	}
	
	public void handleOpenCommand() {
		//create an object of JFileChooser class
		JFileChooser file_chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		//set the selection mode to directories and files.
		file_chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		//Enable file_chooser to select multiple files.
		file_chooser.setMultiSelectionEnabled(true);
		
		//Set filter to JFileChooser object so the dialog only shows jpg.
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG", "jpg", "jpeg"));
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "png"));
		
		//invoke the showsOpenDialog function to show the save dialog
		int r = file_chooser.showOpenDialog(null);
		
		if(r == JFileChooser.APPROVE_OPTION){
			
			//Set the names of the selected files to a list shown in GUI.
			selected_files = file_chooser.getSelectedFiles(); //selected_files is File[] array.
			file_list = new JList<>(selected_files); //JList containing a list of files.
			//file_list.setCellRenderer(new FileRenderer(true));
			file_list.setVisibleRowCount(10);
			
			// Create the model for the JList
		    DefaultListModel model = new DefaultListModel();

		    // Add all the elements of the array "selected_files" in the model
		    for (int i=0 ; i<selected_files.length ; i++){
		        model.addElement(selected_files[i].getName());
		    }
			
		    // Add the JList to the JScrollPane
		    file_list.setModel(model);
			
		    panel.add(new JScrollPane(file_list));
		    panel.revalidate();
		    panel.repaint();
		    
			//set the label to the path of the selected directory
			label.setText("The user has selected the files.");
		}else {
			//if the user cancelled the operation
			label.setText("The user cancelled the operation.");
		}
	}
	
	public void handleConvertCommand() {
		ImageConverter image_converter = new ImageConverter();
		
		if(label.getText() != "no file selected" && label.getText() != "The user cancelled the operation.") {
			
			String inputImagePath = label.getText();
			
			int dot = 0;
			for(int i = inputImagePath.length()-1; i > 0; i--) {
				if(inputImagePath.charAt(i) == '.') {
					dot = i;
				}
			}
			StringBuilder sb = new StringBuilder(inputImagePath.substring(0,dot));
			String file_ext = (String) file_extension_list.getSelectedItem();
			sb.append(".");
			sb.append(file_ext);
			String outputImagePath = sb.toString();
			
			try {
				boolean result = image_converter.convertFormat(inputImagePath, outputImagePath, file_ext);
				if(result) {
					label2.setText("Image Converted Successfully!");
				}else {
					label2.setText("Could not convert the image.");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error during converting image.");
				e1.printStackTrace();
			}
		}
	}
	
}
