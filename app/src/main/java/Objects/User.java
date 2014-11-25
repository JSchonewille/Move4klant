package Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jeff.move4klant.R;

/**
 * Created by Leo on 12-11-14.
 */
public class User {

    private int userID;
    private String name;
    private String lastName;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String email;
    private Bitmap image;
    private Byte[] byteArray;
    private String filePath;

    public User(){}

    public User(Context c, int id, String name, String lastName, String street, String houseNumber,
                              String postalCode, String city, String email){
        this.setUserID(id);
        this.setName(name);
        this.setLastName(lastName);
        this.setStreet(street);
        this.setHouseNumber(houseNumber);
        this.setPostalCode(postalCode);
        this.setCity(city);
        this.setEmail(email);
        Bitmap bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.emptyprofile);
        this.image = bmp;
        this.filePath = "";
    }

    public void setImage(Bitmap bitmap){
        this.image = bitmap;
    }

    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setUserID (int user_ID){
        this.userID = user_ID;
    }

    public int getUserID() {
        return userID;
    }
}
