package com.lunatech.example.sietse.StackSearch;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import com.lunatech.example.sietse.StackSearch.model.StackSite;
import com.lunatech.example.sietse.StackSearch.model.User;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class UserParser {
    private static final String NS = null;
    private final StackSite site;
    private final UserHelper helper;

    public UserParser(StackSite site, UserHelper helper) {
        this.site = site;

        this.helper = helper;
    }

    public int parse(InputStream in) throws IOException, XmlPullParserException {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Integer readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        final Set<User> entries = new LinkedHashSet<User>();

        parser.require(START_TAG, NS, "users");
        int count = 0;

        while (parser.next() != END_TAG) {
            if(parser.getEventType() != START_TAG) {
                continue;
            }

            if(parser.getName().equals("row")) {
                entries.add(readUser(parser));
            } else {
                skipElement(parser);
            }

            count++;

            if(entries.size() == 500 ){
                helper.saveUsers(entries);
                entries.clear();
            }
        }
        helper.saveUsers(entries);

        Log.i("UserParser", String.format("Parsed a total of %s users for site %s", entries.size(), site));
        return count;
    }

    private User readUser(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(START_TAG, NS, "row");

        final Long id;
        final String displayName, about;
        final Integer reputation;

        id = parseLong(parser.getAttributeValue(NS, "Id"));
        displayName = parser.getAttributeValue(NS, "DisplayName");
        about = parser.getAttributeValue(NS, "AboutMe");
        reputation = parseInt(parser.getAttributeValue(NS, "Reputation"));

        final User user = new User(site, id, displayName, about, reputation);

        skipElement(parser); // Advance parser to end of element

        return user;
    }

    private static void skipElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() != START_TAG) throw new IllegalStateException();

        int depth = 1;
        while (depth != 0) {
            int i = parser.next();
            if (i == END_TAG) {
                depth--;
            } else if (i == START_TAG) {
                depth++;
            }
        }
    }


}
