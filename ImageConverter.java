import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
 
public class ImageConverter {
 
    /**
     * Converts an image to another format
     *
     * @param inputImagePath Path of the source image
     * @param outputImagePath Path of the destination image
     * @param formatName the format to be converted to, one of: jpeg, png,
     * bmp, wbmp, and gif
     * @return true if successful, false otherwise
     * @throws IOException if errors occur during writing
     */
	
	public ImageConverter(){
		
	}
	
    public boolean convertFormat(String inputImagePath, String outputImagePath, String formatName) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputImagePath);
        FileOutputStream outputStream = new FileOutputStream(outputImagePath);
         
        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);
        // create a blank, RGB, same width and height, and a white background
        BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);
        
        // writes to the output image in specified format
        boolean result = ImageIO.write(newBufferedImage, formatName, outputStream);
        
        // needs to close the streams
        outputStream.close();
        inputStream.close();
         
        return result;
    }
}
