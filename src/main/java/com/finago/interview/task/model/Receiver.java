/*
 * Copyright (c) 2021. batch-processor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or batch-processor(at your option) any later version.
 *
 * batch-processor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with batch-processor; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
