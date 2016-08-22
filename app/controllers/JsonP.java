package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import it.sad.sii.network.RestClient;
import it.sad.sii.transit.sdk.model.Arrival;
import it.sad.sii.transit.sdk.model.ArrivalResponse;
import it.sad.sii.transit.sdk.model.Departure;
import it.sad.sii.transit.sdk.model.DepartureResponse;
import org.apache.commons.lang3.StringUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Jsonp.jsonp;

/**
 * Created by cmair on 2/2/16.
 */
public class JsonP extends Controller {

    public Result getDepartures(String callback, Integer waypoint) {


        Config conf = ConfigFactory.load();
        String transitServiceBaseURL =
                conf.getString("transit_service.base_url");
        String transitServiceUsername =
                conf.getString("transit_service.username");
        String transitServicePassword =
                conf.getString("transit_service.password");

        String nextDeparturesAction = "/departures/" + waypoint + "/until";
        String response = "";

        try {
            RestClient client =
                    new RestClient(transitServiceBaseURL, transitServiceUsername,
                            transitServicePassword, 60, null);

            response = client.get(transitServiceBaseURL + nextDeparturesAction);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return internalServerError("Backend not URI not valid: " +
                    e.getMessage());

        } catch (IOException i)  {
            i.printStackTrace();
            return internalServerError("Backend IO Exception: " +
                    i.getMessage());
        }


        List<Map<String, Object>> records = new ArrayList<>();

        Integer elements = 0;

        ObjectMapper mapper = new ObjectMapper();
        DepartureResponse departure_response = null;
        try {
            departure_response =
                    mapper.readValue(response, DepartureResponse.class);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (departure_response != null) {
            for (Departure departure: departure_response.getDepartures()) {
                Map<String, Object> record = new HashMap<>();
                record.put("sDate", departure.getScheduledTime().toString("yyyy-MM-dd"));
                record.put("sTime", departure.getScheduledTime().toString("HH:mm"));
                record.put("sDeparture", decompose(departure.getDestinationDE()));

                Integer delayStatus = 3;
                if (departure.getDelayInSeconds() > 60) {
                    delayStatus = 4;
                }

                record.put("nStatus", delayStatus);
                record.put("nTrack", departure.getLaneIT());
                if (elements == 0) {
                    record.put("bLight", true);
                } else {
                    record.put("bLight", false);
                }

                records.add(record);

                elements++;
                if (elements >= 8) {
                    break;
                }
            }
        }

        JsonNode responseNode = Json.toJson(records);
        return ok(jsonp(callback, responseNode));
    }


    public Result getArrivals(String callback, Integer waypoint) {


        Config conf = ConfigFactory.load();
        String transitServiceBaseURL =
                conf.getString("transit_service.base_url");
        String transitServiceUsername =
                conf.getString("transit_service.username");
        String transitServicePassword =
                conf.getString("transit_service.password");

        String nextDeparturesAction = "/arrivals/" + waypoint + "/until";
        String response = "";

        try {
            RestClient client =
                    new RestClient(transitServiceBaseURL, transitServiceUsername,
                            transitServicePassword, 60, null);

            response = client.get(transitServiceBaseURL + nextDeparturesAction);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return internalServerError("Backend not URI not valid: " +
                    e.getMessage());

        } catch (IOException i)  {
            i.printStackTrace();
            return internalServerError("Backend IO Exception: " +
                    i.getMessage());
        }


        List<Map<String, Object>> records = new ArrayList<>();

        Integer elements = 0;

        ObjectMapper mapper = new ObjectMapper();
        ArrivalResponse arrival_response = null;
        try {
            arrival_response =
                    mapper.readValue(response, ArrivalResponse.class);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (arrival_response != null) {
            for (Arrival arrival: arrival_response.getArrivals()) {
                Map<String, Object> record = new HashMap<>();
                record.put("sDate", arrival.getScheduledTime().toString("yyyy-MM-dd"));
                record.put("sTime", arrival.getScheduledTime().toString("HH:mm"));
                record.put("sDeparture", decompose(arrival.getOriginDE()));

                Integer delayStatus = 3;
                if (arrival.getDelayInSeconds() > 60) {
                    delayStatus = 4;
                }

                record.put("nStatus", delayStatus);
                record.put("nTrack", arrival.getLaneIT());
                if (elements == 0) {
                    record.put("bLight", true);
                } else {
                    record.put("bLight", false);
                }

                records.add(record);

                elements++;
                if (elements >= 8) {
                    break;
                }
            }
        }

        JsonNode responseNode = Json.toJson(records);
        return ok(jsonp(callback, responseNode));
    }

    public static String decompose(String s) {

        String[] searchList = { "Ä", "ä", "Ö", "ö", "Ü", "ü", "ß" };
        String[] replaceList = { "Ae", "ae", "Oe", "oe", "Ue", "ue", "sz" };

        String n = StringUtils.replaceEachRepeatedly(s, searchList, replaceList);
        return java.text.Normalizer.normalize(n, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","");
    }
}
