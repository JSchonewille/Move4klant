package library;

/**
 * Created by Sander on 11-11-2014.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sander on 7-11-2014.
 */
public class Offer {
    private int ID;
    private String categoryID;
    private String description;
    private String image;

    public Offer(int ID, String categoryID, String description, String image){
        this.ID = ID;
        this.categoryID=categoryID;
        this.description=description;
        this.image=image;
    }

    public Offer(JSONObject object){

        try {
            this.ID = object.getInt("id");
            this.categoryID=object.getString("category");
            this.description=object.getString("description");
            this.image=object.getString("image");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Offer> fromJSON(JSONArray array){
        ArrayList<Offer> henk = new ArrayList<Offer>();
        for(int i = 0; i < array.length(); i++) {
            try{
                henk.add(new Offer(array.getJSONObject(i)));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return henk;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
