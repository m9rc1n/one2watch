package vandy.mooc;

import vandy.mooc.common.ContextView;
import vandy.mooc.common.ModelOps;
import vandy.mooc.common.PresenterOps;
import vandy.mooc.model.aidl.TrailerData;

public interface MVP {

    interface RequiredViewOps extends ContextView {
        void displayResults(TrailerData trailerData, String errorReason);
    }

    interface ProvidedPresenterOps extends PresenterOps<MVP.RequiredViewOps> {
        boolean getWeatherAsync(String location);
        boolean getWeatherSync(String location);
    }

    interface RequiredPresenterOps extends ContextView {
        void displayResults(TrailerData trailerData, String errorMessage);
    }

    interface ProvidedModelOps extends ModelOps<MVP.RequiredPresenterOps> {
        boolean getWeatherAsync(String location);
        TrailerData getWeatherSync(String location);
    }
}
