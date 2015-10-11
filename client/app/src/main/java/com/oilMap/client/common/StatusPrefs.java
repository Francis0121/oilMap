package com.oilMap.client.common;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by SungGeun on 2015-10-11.
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface StatusPrefs {

    @DefaultString("0")
    String status();

    @DefaultString("1")
    String imageType();

}
