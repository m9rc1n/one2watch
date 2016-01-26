package io.github.marcinn.model.aidl;

import java.util.List;
import io.github.marcinn.model.aidl.TrailerData;

interface TrailerCall {
    List<TrailerData> getPopularTrailers();
    List<TrailerData> getBoxOfficeTrailers();
    List<TrailerData> getComingSoonTrailers();
    List<TrailerData> getTrailers(in String query);
}
