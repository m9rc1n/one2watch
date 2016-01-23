package vandy.mooc;

import java.util.List;

import vandy.mooc.common.ContextView;
import vandy.mooc.common.ModelOps;
import vandy.mooc.common.PresenterOps;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerType;

public interface MVP {

    interface RequiredViewOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorReason, TrailerType type);
    }

    interface ProvidedPresenterOps extends PresenterOps<MVP.RequiredViewOps> {
        boolean getTrailersAsync(String query, TrailerType type);

        boolean getTrailersAsync(TrailerType type);

        boolean getTrailersSync(String query, TrailerType type);

        boolean getTrailersSync(TrailerType type);
    }

    interface RequiredPresenterOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorMessage, TrailerType type);
    }

    interface ProvidedModelOps extends ModelOps<MVP.RequiredPresenterOps> {
        boolean getTrailersAsync(String query, TrailerType type);

        boolean getTrailersAsync(TrailerType type);

        List<TrailerData> getTrailersSync(String query, TrailerType type);

        List<TrailerData> getTrailersSync(TrailerType type);
    }
}
