import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

//holds a list of images + a prescaled version 
public class ImageManager {
	
	private BufferedImage errorImage;
	private String basePath;
	private HashMap<String,BufferedImage> images;
	
	public ImageManager (String basePath) {
		this.basePath = basePath;
		this.images = new HashMap<String,BufferedImage>();
		setErrorImage(100,100);
	}
	public void setErrorImage(int width,int height) {
		this.errorImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
		Graphics2D g = errorImage.createGraphics();
		g.setColor(Color.red);
		g.drawRect(0,0,width,height);
		g.drawLine(0,width,height,0);
		g.drawLine(0,0,width,height);
		
	}

//	load an image 
	private BufferedImage loadImage(String fileName) {
		try { BufferedImage img =  ImageIO.read(new File(basePath+fileName)); 
		return img;}
		catch (IOException e) { return null; }
	}
	
	private BufferedImage rotateImage(BufferedImage source) {
		BufferedImage rotated = new BufferedImage(source.getHeight(),source.getWidth(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotated.createGraphics();
		g.rotate(-Math.PI/2.0);
		g.drawImage(source,-source.getWidth(),0,null);
		return rotated;
	}
	
	public void setBasePath(String basePath) {
		if (!basePath.endsWith("/"))
			this.basePath = basePath+"/";
	}
	
	

	public void addImage(String fileName) {
		BufferedImage img = loadImage(fileName);
		if (img == null)
			img = errorImage;
		images.put(fileName,img);
	}
	
	public BufferedImage getImage(String fileName) {
		if(!images.containsKey(fileName))
			addImage(fileName);
		return (BufferedImage)images.get(fileName);
	}
	
	public BufferedImage getScaledImage(String fileName, int width, int height,boolean rotate) {
		BufferedImage source = getImage(fileName);
		BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = AffineTransform.getScaleInstance((double)width/source.getWidth(),(double)height/source.getHeight());
		
		Graphics2D g = scaled.createGraphics();
		g.drawRenderedImage(source,at);
		if (rotate == true)
			return rotateImage(scaled);
		return scaled;
	}
}
	
	
