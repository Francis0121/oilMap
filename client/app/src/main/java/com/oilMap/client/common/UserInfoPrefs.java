package com.oilMap.client.common;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by SungGeun on 2015-10-01.
 */
@SharedPref
public interface UserInfoPrefs {

    @DefaultString("")
    String id();
}
