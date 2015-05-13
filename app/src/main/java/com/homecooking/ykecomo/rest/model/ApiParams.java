package com.homecooking.ykecomo.rest.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by andres on 13/03/15.
 */
@Parcel
public class ApiParams {

    public ArrayList<Hashtable<String, String>> getCreateMember() {
        return createMember;
    }

    public void setCreateMember(ArrayList<Hashtable<String, String>> createMember) {
        this.createMember = createMember;
    }

    protected ArrayList<Hashtable<String, String>> createMember;
}
