package Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sander on 14-11-2014.
 */
public class Product {
    private int productID;
    private String name;
    private int categoryID;
    private String image;
    private String description;

    public Product(){}
    public Product(int productID, String name, int categoryID, String image, String description){
        this.productID=productID;
        this.name=name;
        this.categoryID=categoryID;
        this.image=image;
        this.description=description;
    }
    public Product(JSONObject object){
        try {
            this.productID = object.getInt("id");
            this.name=object.getString("name");
            this.categoryID=object.getInt("categoryID");
            this.image=object.getString("image");
            this.description=object.getString("description");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<Product> fromJSON(JSONArray array){
        ArrayList<Product> henk = new ArrayList<Product>();
        for(int i = 0; i < array.length(); i++) {
            try{
                henk.add(new Product(array.getJSONObject(i)));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return henk;
    }
}
