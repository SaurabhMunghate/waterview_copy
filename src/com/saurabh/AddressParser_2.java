package com.saurabh;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.IOException;

public class AddressParser_2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String serializedClassifier = "/home/shatam-100/Downloads/stanford-ner-4.2.0/stanford-ner-2020-11-17/classifiers/english.all.3class.distsim.crf.ser";
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        
        // Rest of your code
        
        System.out.println("hiiiii");
    }
}
