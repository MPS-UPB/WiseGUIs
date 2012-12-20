/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;

/**
 *
 * @author Jim
 */
public class ImageViewer  {

  public static Image load(byte[] data) throws Exception{
    Image image = null;
    SeekableStream stream = new ByteArraySeekableStream(data);
    String[] names = ImageCodec.getDecoderNames(stream);
    ImageDecoder dec = 
      ImageCodec.createImageDecoder(names[0], stream, null);
    RenderedImage im = dec.decodeAsRenderedImage();
    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
    return image;
  }
  
  public static Image resize(Image image,int width , int height){
   
      Image image_scaled = image.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
      return image_scaled;
  }
  
  public static Image buildImage(String pathImage){
      
      FileInputStream in = null;
      Image image = null;
      try{
            in = new FileInputStream(pathImage);
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            image = ImageViewer.load(buffer.array());
      }catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      return image;
      
  }
}
