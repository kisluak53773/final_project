package com.example.webproject.tag;

import jakarta.servlet.http.HttpSession;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.webproject.controller.RequestAttribute.LOCALE;

public final class TagUtil {
    private static final String BUNDLE_BASENAME = "prop.pagecontent";
    private static final String LOCALE_SPLITTER = "_";
    private static final int LANG_KEY = 0;
    private static final int COUNTRY_KEY = 1;

    private TagUtil() {
    }

    public static ResourceBundle findBundle(HttpSession session) {
        String locale = (String) session.getAttribute(LOCALE);
        String[] tags = locale.split(LOCALE_SPLITTER);
        Locale currentLocale = new Locale(tags[LANG_KEY], tags[COUNTRY_KEY]);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_BASENAME, currentLocale);
        return resourceBundle;
    }

}
