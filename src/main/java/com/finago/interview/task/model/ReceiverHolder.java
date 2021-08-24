package com.finago.interview.task.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "receivers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiverHolder {

    @XmlElement(name = "receiver")
    private List<Receiver> receiver = new ArrayList<>();

    @XmlTransient
    public List<Receiver> getReceiver() {
        return receiver;
    }
}
