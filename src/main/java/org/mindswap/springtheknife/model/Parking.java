package org.mindswap.springtheknife.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Parking {

    @JsonProperty("result")
    private Result result;

    @Getter
    public static class Result {
        private List<Record> records;
    }

    @Getter
    public static class Record {

        private String active;
        private String address;
        private double latitude;
        private double longitude;

    }

}

