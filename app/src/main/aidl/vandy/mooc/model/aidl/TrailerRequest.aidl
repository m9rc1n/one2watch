package vandy.mooc.model.aidl;

import vandy.mooc.model.aidl.TrailerResults;

interface TrailerRequest {
    oneway void getCurrentTrailer(in String location, in TrailerResults results);
}
