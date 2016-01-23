package vandy.mooc.model.aidl;

import java.util.List;
import vandy.mooc.model.aidl.TrailerData;

interface TrailerCall {
    List<TrailerData> getPopularTrailers();
    List<TrailerData> getBoxOfficeTrailers();
    List<TrailerData> getComingSoonTrailers();
    List<TrailerData> getTrailers(in String query);
}
