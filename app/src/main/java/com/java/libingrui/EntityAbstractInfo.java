package com.java.libingrui;

import androidx.room.Embedded;
import androidx.room.Entity;

@Entity
public class EntityAbstractInfo {
    @Embedded
    public EntityCovidInfo COVID;
    public String baidu;
    public String enwiki;
    public String zhwiki;
}
