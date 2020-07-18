package com.example.tovisit_srinathsuddala_c0741052.googleplaces;

import java.util.List;

public interface PlacesListener {
    void onPlacesFailure(PlacesException e);

    void onPlacesStart();

    void onPlacesSuccess(List<Place> places);

    void onPlacesFinished();
}