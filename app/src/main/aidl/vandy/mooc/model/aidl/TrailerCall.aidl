package vandy.mooc.model.aidl;

import java.util.List;
import vandy.mooc.model.aidl.TrailerData;

interface TrailerCall {
    List<TrailerData> getCurrentTrailer(in String location);
}
