package application;

import java.io.File;
import java.io.IOException;

import org.canova.api.records.reader.RecordReader;
import org.canova.api.split.FileSplit;
import org.canova.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.DataSet;

public class VectorizeImageData {
	
	public DataSet getImageDataSet(File image) {

		// Create a record reader, and initialize it with a FileSplit of the
		// data
		RecordReader reader = new ImageRecordReader(80,60);
		try {
			reader.initialize(new FileSplit(image.getAbsoluteFile()));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Convert record reader to a dataset iterator.
		DataSetIterator iter = new CrackRecordReaderDataSetIterator(reader, 10);

		// Select next dataset, and scale the data.
		DataSet next = iter.next();
		//Return the Image dataset.
		next.scale();
		return next;
	}
}