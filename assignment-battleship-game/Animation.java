import java.awt.image.BufferedImage;
import java.text.NumberFormat;

public class Animation {
	
	protected BufferedImage[] scaledImages;
	protected ImageManager iManage;
	protected String baseName,extension;
	protected int frames,width,height;
	
	public Animation(String baseName, String extension,int frames,ImageManager iManage,int width,int height) {
			this.iManage = iManage;
			this.frames = frames;
			this.baseName = baseName;
			this.extension = extension;
			this.scaledImages = new BufferedImage[frames];
			this.width = width;
			this.height = height;
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(Integer.toString(frames).length());
			String file;
			for (int i=0;i<frames;i++)  {
				file = baseName+nf.format(i)+extension;
				iManage.addImage(file);
				scaledImages[i] = iManage.getScaledImage(file,width,height,false);
			}
			
	}
	
	public Animator getAnimator(int x, int y){
		return new Animator(scaledImages,frames,x,y);
	}
	
	public class Animator {
		protected BufferedImage[] images;
		protected int frames,counter;
		protected int x,y;
		
		public Animator(BufferedImage[] images,int frames, int x, int y) {
			this.counter = 0;
			this.images = images;
			this.frames = frames;
			this.x = x;
			this.y = y;
		}
		
		public BufferedImage nextImage() {
			if (counter == frames)
				return null;
				
			BufferedImage img = images[counter];
			counter++;
			return img;
		}
	}
}
	
	


