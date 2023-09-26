package edu.csc.fooddelivery_app.Model;

import java.util.ArrayList;

import edu.csc.fooddelivery_app.R;

public class Hamburgers {
    private String name, price;
    private int pic;

    public Hamburgers(String name, String price, int pic) {
        this.name = name;
        this.price = price;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public static ArrayList<Hamburgers> init() {
        String[] names = {"Hamburger Bò", "Hamburger Bò Sốt Tiêu", "Hamburger Gà", "Hamburger Hải Sản", "Hamburger Tôm Hùm", "Hamburger Trứng"};
        String[] prices = {"47.000đ", "59.000đ", "49.000đ", "60.000đ", "70.000đ", "45.000đ"};
        int[] pics = {
                R.drawable.hamburger_bocrop,
                R.drawable.hamburger_bosotieuden,
                R.drawable.hamburger_gacrop,
                R.drawable.hamburger_haisan,
                R.drawable.hamburger_tomhumcrop,
                R.drawable.hamburger_trungcrop,
        };
        ArrayList<Hamburgers> arrayList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Hamburgers hamburger = new Hamburgers(names[i], prices[i], pics[i]);
            arrayList.add(hamburger);
        }
        return arrayList;
    }
}
