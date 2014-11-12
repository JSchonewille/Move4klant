package library;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Leo on 06-11-14.
 */
public class Category {
    private int ID;
    private String name;
    private int liked;

    public Category(int ID, String name, int liked){
        this.ID = ID;
        this.name = name;
        this.liked = liked;
    }

    public Category(JSONObject object){

        try {
            this.ID = object.getInt("id");
            this.name = object.getString("name");
            this.liked = object.getInt("liked");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLiked() {
        return liked;
    }
    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ArrayList<Category> fromJSON(JSONArray array){
        ArrayList<Category> henk = new ArrayList<Category>();
        for(int i = 0; i < array.length(); i++) {
            try{
                henk.add(new Category(array.getJSONObject(i)));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return henk;

    }
}
