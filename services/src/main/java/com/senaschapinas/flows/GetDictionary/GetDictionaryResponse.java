package com.senaschapinas.flows.GetDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetDictionaryResponse {

    @JsonProperty("palabras")
    private List<String> palabras;

    public List<String> getPalabras() {
        return palabras;
    }

    public void setPalabras(List<String> palabras) {
        this.palabras = palabras;
    }
}
