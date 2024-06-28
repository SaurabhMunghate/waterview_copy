package com.saurabh;
import java.util.List;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class AddressParser_1 {

	//19 May 12.43
    public static void main(String[] args) {
        String address = "123 Main St, Apt 4, Anytown, CA 12345";

        // Load the NER model
        String serializedClassifier = "english.all.3class.distsim.crf.ser.gz";
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

        // Run NER on the address string
        List<List<CoreLabel>> nerTags = classifier.classify(address);

        // Extract the Address, City, State, and Zip
        String addressLine1 = "";
        String addressLine2 = "";
        String city = "";
        String state = "";
        String zip = "";
        for (List<CoreLabel> sentence : nerTags) {
            for (CoreLabel token : sentence) {
                String word = token.word();
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                if (ne.equals("ADDRESS")) {
                    if (addressLine1.isEmpty()) {
                        addressLine1 = word;
                    } else {
                        addressLine2 += " " + word;
                    }
                } else if (ne.equals("CITY")) {
                    city = word;
                } else if (ne.equals("STATE")) {
                    state = word;
                } else if (ne.equals("ZIP")) {
                    zip = word;
                }
            }
        }

        // Print the extracted address components
        System.out.println("Address Line 1: " + addressLine1);
        System.out.println("Address Line 2: " + addressLine2.trim());
        System.out.println("City: " + city);
        System.out.println("State: " + state);
        System.out.println("Zip: " + zip);
    }
}
