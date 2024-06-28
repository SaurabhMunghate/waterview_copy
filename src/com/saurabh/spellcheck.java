/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saurabh;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.StringUtils;

/**
 *
 * @author karan
 */
public class spellcheck {
    static Map<String, String> slangMap;
    static List<String> entityList;
    static HashMap<String, Double> hm1;
    static HashMap<String, Double> hm2;
    static HashMap<String, Double> senti_map;
    static double score = 0.0;
    static int count = 0;

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        slangMap = new HashMap<String, String>();
        BufferedReader slangRead = new BufferedReader(new FileReader("extras/out.txt"));// loading slang dictionary with key as slang and value as its full form
        String line = "";
        while ((line = slangRead.readLine()) != null) {
            String parts[] = line.split("\t");
            slangMap.put(parts[0], parts[1]);
        }
        slangRead.close();

        BufferedReader htm_in = new BufferedReader(new FileReader("extras/html_ent.txt"));
        entityList = new ArrayList<String>();
        while ((line = htm_in.readLine()) != null) {
            entityList.add(line);
        }

        FileInputStream fos1 = new FileInputStream(new File("extras/hash1.dat")); // loading emoticon dictionary, with key as emoticon and value as its sentiment score
        ObjectInputStream out1 = new ObjectInputStream(fos1);
        hm1 = (HashMap<String, Double>) out1.readObject();
        //System.out.println(hm1);

        FileInputStream fos2 = new FileInputStream(new File("extras/hash2.dat")); // loading emoticon dictionary, with key as emoticon and value as its sentiment score
        ObjectInputStream out2 = new ObjectInputStream(fos2);
        hm2 = (HashMap<String, Double>) out2.readObject();
        //System.out.println(hm2);

        String serializedClassifier = "english.all.3class.distsim.crf.ser.gz"; //NER configuration
        AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();

        //loading senti-wordnet
        FileReader fr2 = new FileReader("extras/SentiWordNet_scores_final.txt");
        BufferedReader br2 = new BufferedReader(fr2);
        String str2;
        senti_map = new HashMap<String, Double>();
        while ((str2 = br2.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(str2, "^");
            senti_map.put(st.nextToken(), Double.parseDouble(st.nextToken()));
        }
        //System.out.println(senti_map);

        String a = "&amp; :P :'( wrng https://mail.google.com/mail/u/0/#search/kireet RT @amitgupta75:  @TrafflineDEL  Karan Mangla witnessing hevy traffic on NH-8 from Hero Honda Chowk to Exit 16. Traffic moving very slowly. Avoid if possible.";

        //URL Removal
        a = removeUrl(a);

        //slang removal
        String[] acro = slangRemoval(a);

        //entity removal
        String[] finaldata = entityRemoval(acro);

        //System.out.println(acro);
        a = "";
        for (String word : finaldata) {

            a += word + " ";
        }
        System.out.println(a);

        //Ner Taggging
        String XmlData = classifier.classifyWithInlineXML(a);
        a = XmlData;

        //removing user mentions
        a = mentions(a);

        //handling words to the spell_checked
        String[] data = a.split("<");
        String val = "";
        for (String word : data) {
            if (word.startsWith("PER") || word.startsWith("LOC")) {
                word = word.replaceAll("PERSON>", "");
                word = word.replaceAll("/PERSON>", "");
                word = word.replaceAll("LOCATION>", "");
                word = word.replaceAll("/LOCATION>", "");
                //insert word into database here 
            } else {
                word = word.replaceAll("/PERSON>", "");
                word = word.replaceAll("/LOCATION>", "");
                //System.out.println(word);
                val += word;
            }
        }
        //System.out.println("see" + val);
        val = val.replaceAll("\\s+", " ");
        String[] temp = val.split(" ");
        String match = "";
        //spell_check
        for (String word : temp) {
            //System.out.println(word);
            if (hm2.containsKey(word)) {
                score += hm2.get(word);
                count++;
                a = a.replace(word, "");
            }
            Process p = Runtime.getRuntime().exec("python extras/text_blob.py " + word);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //System.out.println(in.readLine());
            match = in.readLine();
            //match = spell_check(word);
            //System.out.println(match);
            if (!match.equals(word)) {
                a = a.replaceAll(word, match);
            }

        }
        emojiDetection(a);
        System.out.println(score);

        //handle NerTags
        a = af_spellcheck(a);
        System.out.println(a);

        //removing irrelevant chars         
        a = removechars(a);
        System.out.println(a);

        //POS- TAGGING
        a = postagging(a);

        // removing prepositions and nouns
        a = removeprepn(a);
        System.out.println(a);

        sentiscores(a);
        System.out.println(score);
        System.out.println(score / count);

    }

    public static void sentiscores(String message) {

        message = message.replaceAll("_NNS", "_n");
        message = message.replaceAll("_NN", "_n");
        message = message.replaceAll("_RBR", "_r");
        message = message.replaceAll("_RBS", "_r");
        message = message.replaceAll("_RB", "_r");
        message = message.replaceAll("_JJR", "_a");
        message = message.replaceAll("_JJS", "_a");
        message = message.replaceAll("_JJ", "_a");
        message = message.replaceAll("_VBD", "_v");
        message = message.replaceAll("_VBG", "_v");
        message = message.replaceAll("_VBN", "_v");
        message = message.replaceAll("_VBP", "_v");
        message = message.replaceAll("_VBZ", "_vs");
        message = message.replaceAll("_VB", "_v");

        message = message.replaceAll("\\s+", " ");
        // System.out.println(message);
        String[] senti_token = message.split(" ");

        for (String word : senti_token) {
            word = word.toLowerCase();
            System.out.println(word);
            if (senti_map.containsKey(word)) {

                score += senti_map.get(word);
                System.out.println(score);
                count++;
            }

        }

    }

    public static void emojiDetection(String message) {
        Pattern emo = Pattern.compile("[\\uD83D\\uDE01-\\uD83D\\uDE4F]");
        Matcher m_emo = emo.matcher(message);
        while (m_emo.find()) {
            if (hm1.containsKey(m_emo.group()))
                //System.out.println("llalala");
                score += hm1.get(m_emo.group());
            count++;
        }
    }

    public static String removeUrl(String message) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(message);
        int i = 0;
        while (m.find()) {
            message = message.replaceAll(m.group(i), "").trim();
            i++;
        }
        return message;
    }

    public static String af_spellcheck(String message) {

        message = message.replaceAll("<PERSON>", "");
        message = message.replaceAll("</PERSON>", "");
        message = message.replaceAll("<LOCATION>", "");
        message = message.replaceAll("</LOCATION>", "");
        return message;
    }

    public static String[] entityRemoval(String[] message) {
        List<String> finalList = new ArrayList<String>();
        for (String word : message) {
            if (!entityList.contains(word.trim())) {
                finalList.add(word);
            }
        }
        String[] myArray = new String[finalList.size()];
        finalList.toArray(myArray);
        return myArray;
    }

    public static String[] slangRemoval(String message) {
        ArrayList<String> slangRemovalList = new ArrayList<String>();
        String[] words = message.split(" ");
        for (String single : words) {
            if (slangMap.containsKey(single.toUpperCase()))
                slangRemovalList.add(slangMap.get(single.toUpperCase()));
            else
                slangRemovalList.add(single);
        }
        String[] myArray = new String[slangRemovalList.size()];
        slangRemovalList.toArray(myArray);
        return myArray;
    }

    public static String spell_check(String message) {
        try {
            String url = "https://languagetool.org:8081/?language=en-US&text=" + message;

            URI uri = new URI(url);
            URL page = new URL(uri.toString());
            StringBuffer text = new StringBuffer();
            HttpURLConnection conn = (HttpURLConnection) page.openConnection();
            conn.connect();
            InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
            BufferedReader buff = new BufferedReader(in);
            String st1 = buff.readLine();
            while (st1 != null) {
                text.append(st1);
                st1 = buff.readLine();
            }
            String data = text.toString();
            //System.out.println(data);
            if (data.contains("MORFOLOGIK_RULE_EN_US")) {
                Pattern p = Pattern.compile("(replacements=\"([^\"]*)\")");
                Matcher m = p.matcher(data);

                if (!Character.isUpperCase(message.charAt(0))) {
                    m.find();
                    m.find();
                } else {
                    m.find();
                }

                //System.out.println(m.group());
                if (m.group().contains("#")) {
                    Pattern p1 = Pattern.compile("\"(.*?)#");
                    Matcher m1 = p1.matcher(m.group());

                    m1.find();
                    String ans = m1.group().substring(1, m1.group().length() - 1);
                    return ans;
                } else {
                    Pattern p1 = Pattern.compile("\"(.*?)\"");
                    Matcher m1 = p1.matcher(m.group());

                    m1.find();
                    String ans = m1.group().substring(1, m1.group().length() - 1);
                    //System.out.println(ans);
                    //System.out.println("bababa");
                    return ans;
                }
            } else {
                return message;

            }
        } catch (URISyntaxException | IOException e) {
            System.out.println(e);
        }
        return message;
    }

    public static String mentions(String message) {
        Pattern p = Pattern.compile("\\@\\w+");
        Matcher m = p.matcher(message);
        while (m.find()) {
            //System.out.println(m.group());
            message = message.replaceAll(m.group(), "");
        }
        return message;

    }

    public static String removechars(String message) {

        message = message.replaceAll("\\.", "");
        message = message.replaceAll("\\!", "");
        message = message.replaceAll("\\$", "");
        message = message.replaceAll("\\%", "");
        message = message.replaceAll("\\^", "");
        message = message.replaceAll("\\|", "");
        message = message.replaceAll("\\+", "");
        message = message.replaceAll("\\:", "");
        message = message.replaceAll("\\(", "");
        message = message.replaceAll("\\)", "");
        message = message.replaceAll("\\*", "");
        message = message.replaceAll("\\{", "");
        return message;
    }

    public static String postagging(String message) throws Exception {
        MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
        String tagged = tagger.tagString(message);

        return tagged;
    }

    public static String removeprepn(String message) {
        String delims = " ";
        String[] tokens = message.split(delims);
        for (String word : tokens) {
            if (word.endsWith("_IN") || word.endsWith("_NNP") || word.endsWith("_NNPS")) {
                message = message.replace(word, "");
            }

        }
        return message;
    }

}