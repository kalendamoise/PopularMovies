package com.focusandcode.popularmovies.Entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Moise2022 on 12/3/15.
 */
public class MovieVideo {
    private String id;
    @SerializedName("iso_639_1")
    private String iso6391;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MovieVideo{" +
                "id='" + id + '\'' +
                ", iso6391='" + iso6391 + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
}
