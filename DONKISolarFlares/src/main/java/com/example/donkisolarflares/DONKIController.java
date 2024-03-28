package com.example.donkisolarflares;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class DONKIController {
    private static final String API_KEY = "DEMO_KEY";
    @FXML
    private TextField startDate;
    @FXML
    private Label solarFlare;
    @FXML
    private Label solarDate;
    @FXML
    private Label beginTime;
    @FXML
    private Label peakTime;
    @FXML
    private Label endTime;
    @FXML
    private ImageView backgroundImage;
    Image myImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/auroraborealis.jpeg")));

    @FXML
    protected String SolarFlareCalculator() throws Exception {
        backgroundImage.setImage(myImage);
        String userStartDate = startDate.getText();

        if (validDate(userStartDate)) {
            try {
                HttpURLConnection conn = getHttpURLConnection(userStartDate);
                int apiNum = conn.getResponseCode();
                if (apiNum != 200) {
                    getAlert("The API connection is not 200! It is: " + apiNum);
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuilder resp = new StringBuilder();
                    ArrayList<String> respList = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        resp.append(line);
                        respList.add(line);
                    }

                    br.close();

                    String apiResponse = respList.toString();

                    solarFlare.setText(apiResponse);
                    if (apiResponse.equals("[[]]")) {
                        startDate.clear();
                        solarFlare.setText("There is no data for this day. Sorry.");
                        throw new RuntimeException("Invalid Input");

                    }

                    String date = getString(respList);
                    solarDate.setText("The date you have chosen is: " + date);
                    String flareStart = getStringBegin(respList);
                    beginTime.setText("The starting time of your solar flare is: " + flareStart);
                    String flarePeak = getStringPeak(respList);
                    peakTime.setText("The peak time of your solar flare is: " + flarePeak);
                    String flareEnd = getStringEnd(respList);
                    endTime.setText("The end of your solar flare is: " + flareEnd);
                }
            } catch (IOException e) {
                getAlert("There is an IOException");
                startDate.clear();
                throw new IOException();

            } catch (StringIndexOutOfBoundsException e) {
                solarDate.setText(" ");
                beginTime.setText(" ");
                peakTime.setText(" ");
                endTime.setText(" ");
                startDate.clear();
                getAlert("Your date is invalid and/or out of bounds. Or the date does not have any data for that day. If it does not have data it will say '[[]]' in the right-hand box. Please input another date.");
                throw new StringIndexOutOfBoundsException();


            } catch (RuntimeException e) {
                startDate.clear();
                getAlert("There is no data for this day.");

            } catch (Exception e) {
                startDate.clear();
                solarDate.setText(" ");
                beginTime.setText(" ");
                peakTime.setText(" ");
                endTime.setText(" ");
                throw new Exception();
            }

        } else {
            getAlert("The date is not valid. Please Try again");
        }

        return null;
    }

    public static String getString(ArrayList<String> respList) {
        String usersDate = "flrID";
        String userFlareDate = respList.toString();
        int startIndex = userFlareDate.indexOf(usersDate) + usersDate.length() + 3;
        int endIndex = userFlareDate.indexOf(",", startIndex);

        return userFlareDate.substring(startIndex, endIndex);
    }


    public static String getStringBegin(ArrayList<String> respList) {
        String usersBegin = "beginTime";
        String userFlareBegin = respList.toString();
        int startIndex = userFlareBegin.indexOf(usersBegin) + usersBegin.length() + 14;
        int endIndex = userFlareBegin.indexOf(",", startIndex);

        return userFlareBegin.substring(startIndex, endIndex);
    }

    public static String getStringPeak(ArrayList<String> respList) {
        String usersPeak = "peakTime";
        String userPeakTime = respList.toString();
        int startIndex = userPeakTime.indexOf(usersPeak) + usersPeak.length() + 14;
        int endIndex = userPeakTime.indexOf(",", startIndex);
        return userPeakTime.substring(startIndex, endIndex);
    }

    public static String getStringEnd(ArrayList<String> respList) {
        String endTime = "endTime";
        String userEndTime = respList.toString();
        int startIndex = userEndTime.indexOf(endTime) + endTime.length() + 14;
        int endIndex = userEndTime.indexOf(",", startIndex);

        return userEndTime.substring(startIndex, endIndex);
    }


    public HttpURLConnection getHttpURLConnection(String startDate) throws IOException {
        String DONKI = "https://api.nasa.gov/DONKI/FLR?startDate=" + startDate + "&endDate=" + startDate + "&api_key=" + API_KEY;
        URL url = new URL(DONKI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }


    public static boolean validDate(String userDates) {
        if (!userDates.matches("\\d{4}-\\d{2}-\\d{2}"))
            return false;
        SimpleDateFormat valid = new SimpleDateFormat("yyyy-MM-dd");
        valid.setLenient(false);
        try {
            valid.parse(userDates);
            return true;
        } catch (ParseException e) {

            return false;
        }
    }

    public void getAlert(String msg) {
        Alert newAlert = new Alert(Alert.AlertType.ERROR);
        newAlert.setContentText(msg);
        newAlert.show();
    }

}



