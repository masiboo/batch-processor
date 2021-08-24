package com.finago.interview.task.model;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "receiver")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "receiver_id",
        "first_name",
        "last_name",
        "file",
        "file_md5"
})
@Getter
public class Receiver {

    private String receiver_id;
    private String first_name;
    private String last_name;
    private String file;
    private String file_md5;

    public Receiver(){
    }


}
