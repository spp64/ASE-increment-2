package spp.app.asehackathon.places;

import java.io.IOException;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;

@SuppressWarnings("deprecation")
public class GooglePlaces {
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	//Google Places Urls
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    
    private static final String API_KEY = "AIzaSyBHoB-XcMlm5JjEAqeN6T7CX7ceA0Ixjd8";
    
    private double mLatitude;
    private double mLongitude;
    private double mradius;
    
    public PlacesList getPlaces(double latitude, double longitude, double radius, String types) throws IOException{
    	mLatitude = latitude;
    	mLongitude = longitude;
    	mradius = radius;
    	
    	try {
    		 
            HttpRequestFactory factory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest httpRequest = factory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            httpRequest.getUrl().put("key", API_KEY);
            httpRequest.getUrl().put("location", mLatitude + "," + mLongitude);
            httpRequest.getUrl().put("radius", mradius); // in meters
            httpRequest.getUrl().put("sensor", "false");
            if(types != null)
                httpRequest.getUrl().put("types", types);
 
            PlacesList list = httpRequest.execute().parseAs(PlacesList.class);
            // Check log cat for places response status
            Log.d("Places Status", "" + list.status);
            return list;
 
        } catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
            return null;
        }
    	
    }
    
    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                GoogleHeaders headers = new GoogleHeaders();
                headers.setApplicationName("AseHackathon");
                request.setHeaders(headers);
                JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
                request.addParser(parser);
            }
        });
    }
    
    public PlaceDetails getPlaceDetails(String reference) throws Exception {
        try {
 
            HttpRequestFactory factory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest httpRequest = factory
                    .buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
            httpRequest.getUrl().put("key", API_KEY);
            httpRequest.getUrl().put("reference", reference);
            httpRequest.getUrl().put("sensor", "false");
            PlaceDetails place = httpRequest.execute().parseAs(PlaceDetails.class);
             
            return place;
 
        } catch (HttpResponseException e) {
            Log.e("Error in Perform Details", e.getMessage());
            throw e;
        }
    }
	
}
