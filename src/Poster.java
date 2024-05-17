import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;


public class Poster {
    String chosenWord = "spring";
    ArrayList<Integer> imageX = new ArrayList<Integer>();
    ArrayList<String> tagOptions = new ArrayList<String>();
    HashSet<String> tagOptionsHash = new HashSet<String>();
    public JSONArray jsonArray;
    public String chosenLink;
    JFrame mainFrame = new JFrame("Button Grid Example");
    public boolean linkpage=false;
    JPanel buttonPanel;

    public static void main(String[] args) {
        Poster poster = new Poster();
    }

    public Poster() {
        linkpage=false;
        mainFrame.setSize(700, 850);
        try {
            URL url = new URL("https://pixabay.com/api/?key=43557578-57d8c196d378af42f9843db80&image_type=photo&per_page=200&page=1");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            jsonArray = (JSONArray) jsonObject.get("hits");

            tagAdd();
            buttonCreater();
            mainFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //when a button is pressed it finds the tag and then sends that value to the link grabber
    //opens the link to the page immediately when the button is pressed
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            linkpage=true;
            System.out.println(linkpage);
            for (int x = 0; x < tagOptions.size(); x++) {
                if (command.equals(tagOptions.get(x))) {
                    chosenWord = tagOptions.get(x);
                    linkGrabber();
                }
            }
            URL temp = null;
            try {
                temp = new URL(chosenLink);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
            openWebpage(temp);
        }
    }

    //the following two methods are adapted from the web to fit my project.
    // They are not my work but are adapted to help my work
    //Immediately opens the webpage when the button is pressed
    public boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    //grabs the link related to a given tag when the button is pressed
    public String linkGrabber(){
        System.out.println("chosen word: " + chosenWord);
        for (int x = 0; x < jsonArray.size(); x++) {
            JSONObject image = (JSONObject) jsonArray.get(x);
            String tags2 = (String)image.get("tags");
            String[] miniArray2 = tags2.split(", ", 5);

            for (int y=0;y<miniArray2.length;y++) {
                if (miniArray2[y].equals( chosenWord )) {
                    chosenLink = (String) image.get("largeImageURL");
                }
            }
        }
        System.out.println(chosenLink);
        return chosenLink;
    }

    //creates all the buttons that are on the panel
    public void buttonCreater(){
        int rows = 13;
        int cols = 7;
        buttonPanel = new JPanel(new GridLayout(rows, cols));
        ArrayList<JButton> buttonArrayList = new ArrayList<JButton>();
        int curr = 0;

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                buttonArrayList.add(new JButton(String.valueOf(tagOptions.get((int) (Math.random() * tagOptions.size())))));
                buttonPanel.add(buttonArrayList.get(curr));
                curr++;
            }
        }
        for (int x = 0; x < buttonArrayList.size(); x++) {
            buttonArrayList.get(x).addActionListener(new ButtonClickListener());
        }
        mainFrame.add(buttonPanel);

    }

    //creates an array of not repeating tags for images whose names are small enough to fit on each button
    public void tagAdd(){
        int n = jsonArray.size();
        boolean inArray = false;
        for (int x = 0; x < n; x++) {

            //makes an array to split the tags from one string to a string for each tag
            JSONObject image = (JSONObject) jsonArray.get(x);
            String tags = (String) image.get("tags");
            String[] miniArray = tags.split(", ", 5);

            //puts all the strings from the mini array into a hashset so no words are repeated
            for(int z=0;z<miniArray.length;z++){
                tagOptionsHash.add(miniArray[z]);

            }

            //makes an array from the hash and puts them into an array list so I can use the .get command
            String[] tagOptionsArray = tagOptionsHash.toArray(new String[0]);
            for (int y = 0; y < tagOptionsArray.length; y++) {
                if (tagOptionsArray[y].length()<13) {
                    tagOptions.add(tagOptionsArray[y]);
                }
            }
        }
        System.out.println(tagOptionsHash);
    }
}