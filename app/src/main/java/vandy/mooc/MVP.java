package vandy.mooc;

import java.util.List;

import vandy.mooc.common.ContextView;
import vandy.mooc.common.ModelOps;
import vandy.mooc.common.PresenterOps;
import vandy.mooc.model.aidl.TrailerData;

public interface MVP {

    interface RequiredViewOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorReason);
    }

    interface ProvidedPresenterOps extends PresenterOps<MVP.RequiredViewOps> {
        boolean getWeatherAsync(String location);
        boolean getWeatherSync(String location);
    }

    interface RequiredPresenterOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorMessage);
    }

    interface ProvidedModelOps extends ModelOps<MVP.RequiredPresenterOps> {
        boolean getWeatherAsync(String location);

        List<TrailerData> getWeatherSync(String location);
    }
}
