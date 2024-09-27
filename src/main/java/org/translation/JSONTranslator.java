package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private final Map<String, Map<String, String>> mapOfCountriesMap = new HashMap<>();
    private final String alpha3 = "alpha3";

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                // save each country's info
                JSONObject eachCountryInJSONArray = jsonArray.getJSONObject(i);

                // transfer each country's info (JSONObject) to a map
                Map<String, String> tempCountryInfoMap = new HashMap<>();
                Iterator<String> eachKey = eachCountryInJSONArray.keys();
                while (eachKey.hasNext()) {
                    String currentKey = eachKey.next();
                    Object tempValue = eachCountryInJSONArray.get(currentKey);
                    if (tempValue instanceof Integer) {
                        tempCountryInfoMap.put(currentKey, eachCountryInJSONArray.get(currentKey).toString());
                    }
                    else {
                        tempCountryInfoMap.put(currentKey, (String) eachCountryInJSONArray.get(currentKey));
                    }
                }

                // Save each country and its info in a map of maps
                String associatedCountry = eachCountryInJSONArray.getString(alpha3);
                mapOfCountriesMap.put(associatedCountry, tempCountryInfoMap);

            }
            // jsonArray is a list of dictionaries, each dict being a single country and their info unsorted

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        /**
         * Returns list of all keys, unordered is fine
         * Program reruns unknown number of times, so can return list every time rather than define class variable
         * @param country 3 char code of country
         */
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        Map<String, String> countryInfo = mapOfCountriesMap.get(country.toLowerCase());
        ArrayList<String> justLanguages = new ArrayList<>();
        for (String eachKey : countryInfo.keySet()) {
            if ("id".equals(eachKey) || "alpha2".equals(eachKey) || alpha3.equals(eachKey)) {
                continue;
            }
            justLanguages.add(eachKey);
        }
        return justLanguages;
    }

    @Override
    public List<String> getCountries() {
        /**
         * Return all countries in sample.json
         * Program reruns unknown number of times, so can return list every time rather than define class variable
         */
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(mapOfCountriesMap.keySet());
    }

    @Override
    public String translate(String country, String language) {
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        boolean converted = false;
        String languageAsCode = null;

        // if language looking for is greater than 2, change it to the 2 letter code
        if (language.length() > 2) {
            languageAsCode = languageCodeConverter.fromLanguage(language);
            converted = true;
        }
        String useThis = language.toLowerCase();
        if (converted) {
            useThis = languageAsCode;
        }

        System.out.println(country);
        for (String eachKey : mapOfCountriesMap.get(countryCodeConverter.fromCountry(country).toLowerCase()).keySet()) {
            if ("id".equals(eachKey) || "alpha2".equals(eachKey) || alpha3.equals(eachKey)) {
                continue;
            }
            if (eachKey.equals(useThis)) {
                return mapOfCountriesMap.get(countryCodeConverter.fromCountry(country).toLowerCase()).get(useThis);
            }
        }
        return null;

    }
}
