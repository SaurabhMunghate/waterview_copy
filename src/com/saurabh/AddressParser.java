package com.saurabh;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class AddressParser {
   
    public static void main(String[] args) {
        String address = "123 Main St Apt 4 Anytown CA 12345";
       
        // Load the NER model
//        String serializedClassifier = "/path/to/your/file/english.all.3class.distsim.crf.ser.gz";

        String serializedClassifier = "/home/shatam-100/Downloads/stanford-ner-4.2.0/stanford-ner-2020-11-17/classifiers/english.all.3class.distsim.crf.ser";
//        String serializedClassifier = "/home/shatam-100/Downloads/stanford-ner-4.2.0/stanford-ner-2020-11-17/classifiers/english.all.3class.distsim.crf.ser.gz";
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
        	System.out.println(sentence);
            for (CoreLabel token : sentence) {
            	
            	System.out.println(token);
            	
                String word = token.word();
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
               
                System.out.println("word :: "+word);
                System.out.println("ne   :: "+ne);
//                if(!(ne==null)) {
//                	 if (ne.equals("ADDRESS")) {
//                         if (addressLine1.isEmpty()) {
//                             addressLine1 = word;
//                         } else {
//                             addressLine2 += " " + word;
//                         }
//                     } else if (ne.equals("CITY")) {
//                         city = word;
//                     } else if (ne.equals("STATE")) {
//                         state = word;
//                     } else if (ne.equals("ZIP")) {
//                         zip = word;
//                     }
//                }
            }
        }
       
        System.out.println("Address Line 1: " + addressLine1);
        System.out.println("Address Line 2: " + addressLine2.trim());
        System.out.println("City: " + city);
        System.out.println("State: " + state);
        System.out.println("Zip: " + zip);
    }
}