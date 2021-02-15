package com.claudiodimauro.Scrape4Engineering.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "pattern")
public class Pattern {
    @Id
    String id;
    //to define
}
