package com.tacademy.sampledata;

import android.provider.BaseColumns;

/**
 * Created by Tacademy on 2016-08-11.
 */
public class AddressContract {
    public interface Address extends BaseColumns {
        public static final String TABLE = "persontable";
        public static final String COLUMN_NAME = "personname";
        public static final String COLUMN_AGE = "personage";
        public static final String COLUMN_PHONE = "personphone";
        public static final String COLUMN_ADDRESS = "personaddress";
    }
}
