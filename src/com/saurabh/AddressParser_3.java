package com.saurabh;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class AddressParser_3 {
    public static void main(String[] args) {
        String address = "123 Main St Apt 4 Anytown CA 12345";

        // Set up Stanford CoreNLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Create an empty Annotation with the address text
        Annotation document = new Annotation(address);

        // Run all the selected pipeline on this text
        pipeline.annotate(document);

        // Retrieve the sentences from the annotated document
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        // Extract the named entities from the first sentence (assuming single sentence address)
        CoreMap firstSentence = sentences.get(0);
        List<CoreMap> namedEntities = firstSentence.get(CoreAnnotations.MentionsAnnotation.class);

        // Initialize variables to store address components
        String extractedAddress = "";
        String city = "";
        String state = "";
        String zip = "";

        // Loop through the named entities and extract the relevant components
        for (CoreMap entity : namedEntities) {
            String namedEntityType = entity.get(CoreAnnotations.NamedEntityTagAnnotation.class);

            if (namedEntityType.equals("ADDRESS")) {
                extractedAddress = entity.get(CoreAnnotations.TextAnnotation.class);
            } else if (namedEntityType.equals("CITY")) {
                city = entity.get(CoreAnnotations.TextAnnotation.class);
            } else if (namedEntityType.equals("STATE")) {
                state = entity.get(CoreAnnotations.TextAnnotation.class);
            } else if (namedEntityType.equals("ZIP")) {
                zip = entity.get(CoreAnnotations.TextAnnotation.class);
            }
        }

        // Print the extracted information
        System.out.println("Address: " + extractedAddress);
        System.out.println("City: " + city);
        System.out.println("State: " + state);
        System.out.println("ZIP Code: " + zip);
    }
}
