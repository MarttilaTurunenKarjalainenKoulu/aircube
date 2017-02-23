package com.kantelesoftware.aircube;

/**
 * Created by Teemu on 22.11.2016.
 */

class Card {
    int holder;
    String tv_data;
    int photoId;

    Card(int holder, String tv_data, int photoId) {
        this.holder = holder;
        this.tv_data = tv_data;
        this.photoId = photoId;
    }
}
