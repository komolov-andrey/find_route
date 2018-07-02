package dataBean;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import json.JsonReader;
import org.json.JSONException;

/**
 *
 * @author Андрюха
 */
@ManagedBean(name = "route")
@SessionScoped
public class RouteData implements Serializable {

    JSONObject json;

    ArrayList coor = new ArrayList();

    private double distance;
    private double duration;
    private double durationHour;

    private String coor1;
    private String coor2;
    private String coor3;
    private String coor4;
    private String departure;
    private String appointment;

    private String coor1Adr;
    private String coor2Adr;
    private String coor3Adr;
    private String coor4Adr;
    private String departureAdr;
    private String appointmentAdr;

    public ArrayList getCoor() {
        return coor;
    }

    public String getCoor1Adr() {
        return coor1Adr;
    }

    public void setCoor1Adr(String coor1Adr) {
        this.coor1Adr = coor1Adr;
    }

    public String getCoor2Adr() {
        return coor2Adr;
    }

    public void setCoor2Adr(String coor2Adr) {
        this.coor2Adr = coor2Adr;
    }

    public String getCoor3Adr() {
        return coor3Adr;
    }

    public void setCoor3Adr(String coor3Adr) {
        this.coor3Adr = coor3Adr;
    }

    public String getCoor4Adr() {
        return coor4Adr;
    }

    public void setCoor4Adr(String coor4Adr) {
        this.coor4Adr = coor4Adr;
    }

    public String getDepartureAdr() {
        return departureAdr;
    }

    public void setDepartureAdr(String departureAdr) {
        this.departureAdr = departureAdr;
    }

    public String getAppointmentAdr() {
        return appointmentAdr;
    }

    public void setAppointmentAdr(String appointmentAdr) {
        this.appointmentAdr = appointmentAdr;
    }

    public String getCoor1() {
        return coor1;
    }

    public void setCoor1(String coor1) {
        this.coor1 = coor1;
    }

    public String getCoor2() {
        return coor2;
    }

    public void setCoor2(String coor2) {
        this.coor2 = coor2;
    }

    public String getCoor3() {
        return coor3;
    }

    public void setCoor3(String coor3) {
        this.coor3 = coor3;
    }

    public String getCoor4() {
        return coor4;
    }

    public void setCoor4(String coor4) {
        this.coor4 = coor4;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getDistance() {
        return String.format("%.2f", distance/1000);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return String.format("%.2f", duration/60);
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDurationHour() {
        return String.format("%.2f", duration/3600);
    }
    
    public void findRoute(ActionEvent actionEvent) {
        //addMessage(getDeparture());

        // парсим адрес
        String posLoc = "";
        posLoc = getPos(getDepartureAdr());
        if (posLoc.equals("")) {
            setDeparture(posLoc);
        } else {
            setDeparture(posLoc + ";");
        }
        
        posLoc = getPos(getAppointmentAdr());
        if (posLoc.equals("")) {
            setAppointment(posLoc);
        } else {
            setAppointment(posLoc + ";");
        }
        
        posLoc = getPos(getCoor1Adr());
        if (posLoc.equals("")) {
            setCoor1(posLoc);
        } else {
            setCoor1(posLoc + ";");
        }
        
        posLoc = getPos(getCoor2Adr());
        if (posLoc.equals("")) {
            setCoor2(posLoc);
        } else {
            setCoor2(posLoc + ";");
        }
        
        posLoc = getPos(getCoor3Adr());
        if (posLoc.equals("")) {
            setCoor3(posLoc);
        } else {
            setCoor3(posLoc + ";");
        }
        
        posLoc = getPos(getCoor4Adr());
        if (posLoc.equals("")) {
            setCoor4(posLoc);
        } else {
            setCoor4(posLoc + ";");
        }
        
        // парсим координаты
        getRoute();
    }

    public String getPos(String add) {

        String[] name = new String[2];

        try {

            json = JsonReader.readJsonFromUrl("https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + add + "&results=1");

            // парсим
            JSONObject json1 = (JSONObject) json.get("response");
            JSONObject json2 = (JSONObject) json1.get("GeoObjectCollection");
            JSONArray json3 = (JSONArray) json2.get("featureMember");
            JSONObject json4 = (JSONObject) json3.get(0);
            JSONObject json5 = (JSONObject) json4.get("GeoObject");
            JSONObject json6 = (JSONObject) json5.get("Point");

            name = json6.getString("pos").split(" ");

        } catch (Exception e) {

        }
        if (name[1] != null) {
            return name[0] + "," + name[1];
        } else {
            return "";
        }
    }

    public ArrayList getRoute() {

        coor = new ArrayList();

        // получаем инфу с osrm
        try {
            String test = "http://router.project-osrm.org/route/v1/driving/" + getDeparture() + ""
                    + getAppointment() + ""
                    + getCoor1() + ""
                    + getCoor2() + ""
                    + getCoor3() + ""
                    + getCoor4();
            test = test.substring(0, test.length() - 1) + "?geometries=geojson";
            
            json = JsonReader.readJsonFromUrl(test);

            // парсим
            JSONArray json1 = (JSONArray) json.get("routes");
            JSONObject json2 = (JSONObject) json1.get(0);

            setDuration(json2.getDouble("duration"));
            setDistance(json2.getDouble("distance"));

            JSONObject json3 = (JSONObject) json2.get("geometry");
            JSONArray json4 = (JSONArray) json3.get("coordinates");

            for (int i = 0; i < json4.length(); i++) {
                // меняем местами координаты
                String s = json4.get(i) + "";
                // удалить скобки
                s = s.substring(1,s.length() - 1);
                String [] sMas = s.split(",");
                s = "[" + sMas[1] + "," + sMas[0] + "]";
                coor.add(s);
            }

        } catch (IOException | JSONException e) {
            addMessage("Too Many Requests");
            setDistance(0);
            setDuration(0);
        }

        return coor;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
