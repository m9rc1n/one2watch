package io.github.marcinn.model.aidl;

import io.github.marcinn.model.aidl.TrailerResults;

interface TrailerRequest {
    oneway void getPopularTrailers(in TrailerResults results);
    oneway void getBoxOfficeTrailers(in TrailerResults results);
    oneway void getComingSoonTrailers(in TrailerResults results);
    oneway void getTrailers(in String query, in TrailerResults results);
}
