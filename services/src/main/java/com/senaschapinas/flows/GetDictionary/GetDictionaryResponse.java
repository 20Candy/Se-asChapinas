package com.senaschapinas.flows.GetDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetDictionaryResponse {

    @JsonProperty("words")
    private List<String> words;

    public List<String> getPalabras() {
        return words;
    }

    public void setPalabras(List<String> words) {
        this.words = words;
    }
}
