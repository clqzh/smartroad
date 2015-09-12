package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CrackCheck {
	private boolean isThreshold;

	public List<File> getResultFromANN(List<File> images, boolean threshold) {
		this.isThreshold = threshold;
		List<File> crackList = new ArrayList<File>();
		PredictNeuralNetwork nn = new PredictNeuralNetwork();
		for(File image : images){
			if(nn.predict(image)){
				System.out.println("Crack image : "+image.getName());
				crackList.add(image);
			}
		}
		return crackList;
	}

	public List<File> getResultsFromDBN(List<File> images, boolean threshold) {
		this.isThreshold = threshold;
		List<File> crackList = new ArrayList<File>();
		PredictDBN dbn = new PredictDBN();
		for(File image : images){
			if(dbn.predict(image)){
				crackList.add(image);
			}
		}
		return crackList;
	}

}
