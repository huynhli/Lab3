package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {
    private final Map<String, String> countryAndThreeCodeMap = new HashMap<>();
    private final Map<String, String> threeCodeAndCountryMap = new HashMap<>();
    private final int numOfCountries;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            // lines is a list where each line is an index in the array (including first "legend" line)
            int temp = -1;
            for (String eachLine : lines) {
                String[] tempLineList = eachLine.split("\t");
                countryAndThreeCodeMap.put(tempLineList[0], tempLineList[2]);
                threeCodeAndCountryMap.put(tempLineList[2], tempLineList[0]);
                temp += 1;
            }
            numOfCountries = temp;
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        // will utilize threeCodeAndCountryMap
        // Pre: country code exists in country-codes.txt
        String tempValue = this.threeCodeAndCountryMap.get(code);
        if (tempValue == null) {
            tempValue = "null";
        }
        return tempValue;
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return countryAndThreeCodeMap.get(country);
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return numOfCountries;
    }
}
