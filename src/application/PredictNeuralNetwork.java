package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;

public class PredictNeuralNetwork {
	
	public boolean predict(File image){
		// load trained neural network saved with Neuroph Studio (specify some
		// existing neural network file here)
		NeuralNetwork nnet = NeuralNetwork.load("net1.nnet");
		// load trained neural network saved with Neuroph Studio
		// get the image recognition plugin from neural network
		ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin) nnet
				.getPlugin(ImageRecognitionPlugin.class);

			try {
				HashMap<String, Double> output = imageRecognition.recognizeImage(new File(image.getAbsolutePath()));
				if(evaluate(output.values())){
					return true;
				}
				
					
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		return false;
		
	}
	
	private boolean evaluate(Collection<Double> collection) {
		Iterator itr = collection.iterator();
		List<Double> values = new ArrayList<Double>();
		while(itr.hasNext()){
			values.add((Double) itr.next());
		}
		double numCrack = values.get(0);
		double numNonCrack = values.get(1);
		//System.out.println("Crack : "+numCrack+"   Non Crack : "+numNonCrack);
		if (numCrack > numNonCrack) {
			return true;
		}
		return false;
	}

}
