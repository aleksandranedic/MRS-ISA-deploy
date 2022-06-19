package com.project.team9.model.buissness;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;


@Entity
public class UserCategory {
    @Id
    @SequenceGenerator(
            name = "user_category_sequence",
            sequenceName = "user_category_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_category_sequence"
    )
    private Long id;
    private String name;
    private int minimumPoints;
    private int maximumPoints;
    private int discount;
    private String color;
    private boolean isVendorCategory;
    private boolean isClientCategory;
    private String type;
    private boolean deleted;

    public static HashMap<String, String> colors = new HashMap<String, String>() {{
        put("pink-blue", "linear-gradient( 64.5deg,  rgba(245,116,185,1) 14.7%, rgba(89,97,223,1) 88.7% )");
        put("green-blue", "linear-gradient( 109.6deg,  rgba(61,245,167,1) 11.2%, rgba(9,111,224,1) 91.1% )");
        put("pink-purple", "radial-gradient( circle farthest-corner at 10.2% 55.8%,  rgba(252,37,103,1) 0%, rgba(250,38,151,1) 46.2%, rgba(186,8,181,1) 90.1% )");
        put("green", "radial-gradient( circle farthest-corner at 10% 20%,  rgba(14,174,87,1) 0%, rgba(12,116,117,1) 90% )");
        put("blue-purple", "linear-gradient( 83.2deg,  rgba(150,93,233,1) 10.8%, rgba(99,88,238,1) 94.3% )");
        put("purple-orange", "linear-gradient( 358.4deg,  rgba(249,151,119,1) -2.1%, rgba(98,58,162,1) 90% )");
        put("blue", "linear-gradient( 109.6deg,  rgba(62,161,219,1) 11.2%, rgba(93,52,236,1) 100.2% )");
    }};

    public UserCategory() {
    }

    public UserCategory(String name, int minimumPoints, int maximumPoints, int discount, String color, boolean isClientCategory, boolean isVendorCategory) {
        this.name = name;
        this.minimumPoints = minimumPoints;
        this.maximumPoints = maximumPoints;
        this.discount = discount;
        this.color = color;
        this.isClientCategory = isClientCategory;
        this.isVendorCategory = isVendorCategory;
        this.deleted = false;
    }

    public UserCategory(Long id, String name, int minimumPoints, int maximumPoints, int discount, String color, String type) {
        this.id = id;
        this.name = name;
        this.minimumPoints = minimumPoints;
        this.maximumPoints = maximumPoints;
        this.discount = discount;
        this.color = color;
        this.type = type;
        if (Objects.equals(type, "CLIENT")) {
            this.isClientCategory = true;
            this.isVendorCategory = false;
        }
        else if (Objects.equals(type, "VENDOR")) {
            this.isClientCategory = false;
            this.isVendorCategory = true;
        }
        this.deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinimumPoints() {
        return minimumPoints;
    }

    public void setMinimumPoints(int minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    public int getMaximumPoints() {
        return maximumPoints;
    }

    public void setMaximumPoints(int maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isVendorCategory() {
        return isVendorCategory;
    }

    public void setVendorCategory(boolean vendorCategory) {
        isVendorCategory = vendorCategory;
    }

    public boolean isClientCategory() {
        return isClientCategory;
    }

    public void setClientCategory(boolean clientCategory) {
        isClientCategory = clientCategory;
    }
}
