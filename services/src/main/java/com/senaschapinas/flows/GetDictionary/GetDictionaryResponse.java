package com.senaschapinas.flows.GetDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

public class GetDictionaryResponse {

    @JsonProperty("words")
    private List<DictionaryWord> words;

    public List<String> getPalabras() {
        return words.stream()
                .map(DictionaryWord::getId_word)
                .collect(Collectors.toList());
    }


    public void setPalabras(List<DictionaryWord> words) {
        this.words = words;
    }
}
