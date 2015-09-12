package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;

public class PredictDBN {
	public boolean predict(File image) {

		InputStream file;
		InputStream buffer = null;
		ObjectInput input = null;
		MultiLayerNetwork network = null;

		try {
			// Getting the Trained DBN Network.
			/*file = new FileInputStream(
					"DBN_network_OriImgs_1430139299264.dbnet");*/
			file = new FileInputStream(
					"/home/lahiru/Crack_Project/DBN_network_OriImgs_1433014209232.dbnet");
			buffer = new BufferedInputStream(file);
			input = new ObjectInputStream(buffer);

			network = (MultiLayerNetwork) input.readObject();

		} catch (ClassNotFoundException ex) {
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		} finally {
			try {
				input.close();
				buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Check whether the file is available.
		if (image.isFile()) {
			// Vectorizing image data.
			VectorizeImageData vectorImage = new VectorizeImageData();
			DataSet testd = vectorImage
					.getImageDataSet(image.getAbsoluteFile());
			// Getting the Prediction Values from the network.
			INDArray predict2 = network.output(testd.getFeatureMatrix());
			// Comparing DBN network probabilities values.
			return (evaluate(predict2));
		}
		return false;
	}

	private boolean evaluate(INDArray predict2) {
		String str = predict2.toString().replaceAll("[\\[\\](){}]", "");
		String predictString = str.replaceAll(" ", "");
		String[] part = predictString.split(",");
//		// Getting the values.
//		double num1 = Double.parseDouble(part[0]);
//		double num2 = Double.parseDouble(part[1]);
//		num1 = num1*10000;
//		num2 = num2*10000;
//		System.out.println("Crack Value : "+num1+"  non Crack value : "+num2);
//		// If the crack DBN output value is higher than non crack value the
//		// image is a crack image.
//		
//		if (num1 > num2) {
//			// Return true if the image is a crack.
//			return true;
//		}
		
		// testing
		// Getting the values.
        double num1 = Double.parseDouble(part[0])*100000;
        double num2 = Double.parseDouble(part[1])*100000;
        
        //System.out.println("Crack Value : "+num1+"  non Crack value : "+num2);
        double crackthresh = 403.5;
        double noncrackthresh = 99250.5;
        // If the crack DBN output value is higher than non crack value the
        // image is a crack image.
        //testing
        if(num1 > crackthresh){
            if(num2 < noncrackthresh){
            return true;
            }else{
                return false;
            }
        }
		return false;
	}
}
