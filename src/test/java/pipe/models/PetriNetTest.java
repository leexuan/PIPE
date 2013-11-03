package pipe.models;

import org.junit.Before;
import org.junit.Test;
import pipe.common.dataLayer.StateGroup;
import pipe.models.interfaces.IObserver;
import pipe.views.viewComponents.RateParameter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PetriNetTest {
    PetriNet net;
    IObserver mockObserver;

    @Before
    public void setUp()
    {
        net = new PetriNet();
        mockObserver = mock(IObserver.class);
    }

    @Test
    public void addingPlaceNotifiesObservers() {
        net.registerObserver(mockObserver);
        Place place = new Place("", "");
        net.addPlace(place);

        verify(mockObserver).update();
    }

    @Test
    public void addingArcNotifiesObservers() {
        net.registerObserver(mockObserver);
        Arc mockArc = mock(Arc.class);
        net.addArc(mockArc);

        verify(mockObserver).update();
    }

    @Test
    public void addingTransitionNotifiesObservers() {
        net.registerObserver(mockObserver);
        Transition transition = new Transition("", "");
        net.addTransition(transition);
        verify(mockObserver).update();
    }


    @Test
    public void addingAnnotationNotifiesObservers() {
        net.registerObserver(mockObserver);
        Annotation annotation = new Annotation(10, 10, "", 10, 10, false);
        net.addAnnotaiton(annotation);
        verify(mockObserver).update();
    }

    @Test
    public void addingRateParameterNotifiesObservers() {

        net.registerObserver(mockObserver);
        RateParameter rateParameter = new RateParameter("", 0., 0, 0);
        net.addRate(rateParameter);
        verify(mockObserver).update();
    }


    @Test
    public void addingTokenNotifiesObservers() {

        net.registerObserver(mockObserver);
        Token token = new Token();
        net.addToken(token);
        verify(mockObserver).update();
    }

    @Test
    public void addingStateGroupNotifiesObservers() {

        net.registerObserver(mockObserver);
        StateGroup group = new StateGroup();
        net.addStateGroup(group);
        verify(mockObserver).update();
    }
}
