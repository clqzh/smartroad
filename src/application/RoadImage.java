package application;

import javafx.beans.property.SimpleStringProperty;


public class RoadImage {
    private final SimpleStringProperty firstName;
   
 
    RoadImage(String fName) {
        this.firstName = new SimpleStringProperty(fName);
      
    }
 
    public String getFirstName() {
        return firstName.get();
    }
    public void setFirstName(String fName) {
    	firstName.set(fName);
    }
}
        
   
   
