package spp.app.asehackathon.places;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

public class PlacesList implements Serializable {
	@Key
	public String status;

	@Key
	public List<PlaceObjects> results;
}
