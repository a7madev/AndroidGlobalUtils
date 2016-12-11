package me.a7madev.androidglobalutils;

public interface AsyncResponse {
    void processFinish(String requestType, String output, String messageToDisplay, Object extraDetails);
}