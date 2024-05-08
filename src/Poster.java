import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Poster {
    String keyword;
    ArrayList<Integer> imageX = new ArrayList<Integer>();
    ArrayList<String> tagOptions = new ArrayList<String>();


    public JLabel imageLabel;
    public JPanel imagePanel;
    public JFrame mainFrame;
    public  String ImageURL = "https://pixabay.com/api/?key=43557578-57d8c196d378af42f9843db80&id=8719633";
    //NEED TO CHANGE THIS TO A DEFAULT? IDK IT WON"T WORK EVERY DAY


    public Image theImage;
    public static void main(String[] args) {
        Poster poster = new Poster();
        JFrame frame = new JFrame("My Window");
        frame.setSize(700, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
//        Poster.addImage();
    }


    public Poster(){
        try {
            URL url = new URL("https://pixabay.com/api/?key=43557578-57d8c196d378af42f9843db80&image_type=photo&per_page=200&page=1");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            // System.out.println(response);

            JSONParser parser = new JSONParser();
            //System.out.println(str);
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("hits");
            //System.out.println(jsonArray);

            String searchTerm = "spring";
            int n=jsonArray.size();
            for (int x=0;x<n;x++){
                // remove everyhing from imageID
                JSONObject image = (JSONObject)jsonArray.get(x);
                long id = (long)image.get("id");
                String tags = (String) image.get("tags");
//MAKE A STRING TO STORE THE IMAGE URL ADN THEN TAKE THAT ONE DOWN AS THE URL
                if(tags.contains(searchTerm)){
                    System.out.println("id: "+id);
                    System.out.println("tags: "+tags);
                    System.out.println(x);
                    imageX.add(x);
                }
            }
           int imageInArray=(int)(Math.random()*imageX.size());
            int chosenImageX = imageX.get(imageInArray);
            System.out.println("chosen X: " + chosenImageX);

            JSONObject ChosenImage = (JSONObject)jsonArray.get(chosenImageX);
            String link = (String) ChosenImage.get("largeImageURL");

             ImageURL = link;
            System.out.println(ImageURL);
//            theImage = fetchImage(ImageURL);
        //    fetchImage(ImageURL);

            boolean isInArrayList;
            for (int x=0;x<n;x++) {
                JSONObject image = (JSONObject) jsonArray.get(x);
                String tags = (String) image.get("tags");
//                System.out.println(tags);

                String[] miniArray = tags.split(", ", 5);


                isInArrayList = false;
                for (int y = 0; y < miniArray.length; y++) {
                    if (tagOptions.contains(miniArray[y]) || tagOptions.contains(" " + miniArray[y])) {
                        isInArrayList = true;
                    }
                    if (isInArrayList == false) {
                        tagOptions.add(miniArray[y]);
                    }
                }
            }
                System.out.println(tagOptions.size());
                //ISSUE OF THE MOMENT: the size is only 3? im confused. also trying to add array of buttons to match the tags

               // adding an array of buttons to the mainframe
                int rows=tagOptions.size()/5;
                int cols=tagOptions.size()/5;

                JPanel buttonPanel=new JPanel(new GridLayout(rows, cols));
                for(int i=1;i<=rows;i++)
                {
                    for(int j=1;j<=cols;j++)
                    {
                        JButton btn=new JButton(String.valueOf(i));
                        buttonPanel.add(btn);
                    }
                }
                mainFrame.add(buttonPanel);



            System.out.println(tagOptions);






        }
        catch (Exception e) {
            e.printStackTrace();
        }




    }

    //the fetchImage method came from ChatGPT because I didn't know how to get an image from a URL
    private void fetchImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            inputStream.close();

//            return bufferedImage;

            ImageIcon inputImage;
            if (bufferedImage != null) {
                inputImage = new ImageIcon(bufferedImage.getScaledInstance(800, 700, Image.SCALE_SMOOTH));
                if (bufferedImage != null) {
                    imageLabel = new JLabel(inputImage);
                } else {
                    System.out.println("oh no");
                }
                imagePanel.removeAll();
                imagePanel.repaint();
                imagePanel.add(imageLabel);
                mainFrame.add(imagePanel, BorderLayout.CENTER);

            }
            else{
                System.out.println("buffered image is null?");
            }

        } catch (IOException e) {
            e.printStackTrace();
//            return null;
        }
    }



}
//86e3fe1c1624c38b08f960ce8da64982

//jlabel, image goes into that label and that's how it displays, constructor calls prepareGUI(), which is a method, image panel
//swingcontroldemo.addimage
//mothod add image take
//URL url = new URL(path)
//maybe have a default image
//buffered image into image icon into label onto panel
//butto click listener fopr later
