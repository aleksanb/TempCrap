package com.burkow.sykler;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FetchStationsTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "Sykler";
    private final ArrayList<Stations> stations;
    private final Sykler context;

    public FetchStationsTask(Sykler a, ArrayList<Stations> stations) {
        this.stations = stations;
        this.context = a;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String stationUrl = "http://www.bysykler.no/trondheim/kart-over-sykkelstativer";
        //Log.d(TAG, "Got here :D");
        try {
            Document doc = Jsoup.connect(stationUrl).get();
            Elements unparsedStations = doc.select(".mapMarker");
            //Log.d(TAG, String.valueOf(unparsedStations.size()));

            for (int i = 0; i < unparsedStations.size(); i++) {
                Element temp = unparsedStations.get(i);
                Document tempDoc = Jsoup.parse(temp.attr("data-content"));
                Elements horrorParse = tempDoc.select("p");

                String name=null;
                Integer availableBikes=null;
                Integer availableSlots=null;
                Boolean operational=null;
                Boolean online=null;

                //Log.d(TAG, String.valueOf(horrorParse.size()));
                for (Element e: horrorParse) {
                    //Log.d(TAG, e.toString());

                    if (e.toString().contains("-")) {
                        //Log.d(TAG, "fant tittel" + e.toString());
                        String tmpN = e.toString();
                        name = tmpN.substring(tmpN.indexOf("-") + 1, tmpN.indexOf("</"));

                    } else if (e.toString().contains("Ikke operativt")) {
                        //Log.d(TAG, "denne stasjonen er ikke online " + e.toString());
                        availableBikes = 0;
                        availableSlots = 0;
                        operational = false;
                        online = false;
                        break;

                    } else if (e.toString().contains("sykler")) {
                        //Log.d(TAG, "fant ledige sykler " + e.toString());
                        String tmpFree = e.toString();
                        availableBikes = Integer.valueOf(tmpFree.substring(tmpFree.length()-6, tmpFree.length()-4).trim());
                        //Log.d(TAG, "free is now =" + availableBikes);

                    } else if (e.toString().contains("Ledige l")) {
                        //Log.d(TAG, "fant ledige låser " + e.toString());
                        String tmpAv = e.toString();
                        availableSlots = Integer.valueOf(tmpAv.substring(tmpAv.length()-6, tmpAv.length()-4).trim());
                        //Log.d(TAG, "availableSlots are now = " + availableSlots);

                    } else if (e.toString().contains("pen?")) {
                        //Log.d(TAG, "fant åpningsstatus " + e.toString());
                        operational = e.toString().contains("Ja");
                        //Log.d(TAG, "open status is after parse" + operational);

                    } else if (e.toString().contains("line?")) {
                        //Log.d(TAG, "fant online status " + e.toString());
                        online =  e.toString().contains("Ja");
                        //Log.d(TAG, "online status is after parse " + online);

                    }
                }

                int id = Integer.parseInt(temp.attr("data-id"));
                Float lat = Float.parseFloat(temp.attr("data-poslat"));
                Float lng = Float.parseFloat(temp.attr("data-poslng"));

                Stations station = new Stations(id, name, availableBikes, availableSlots, operational, online, lat, lng);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        context.onStationsLoaded();
    }

}
