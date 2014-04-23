package pipe.reachability;

import pipe.animation.Animator;
import pipe.animation.PetriNetAnimator;
import pipe.models.component.arc.Arc;
import pipe.models.component.place.Place;
import pipe.models.component.token.Token;
import pipe.models.component.transition.Transition;
import pipe.models.petrinet.IncidenceMatrix;
import pipe.models.petrinet.PetriNet;
import pipe.parsers.FunctionalResults;
import pipe.parsers.PetriNetWeightParser;
import pipe.visitor.ClonePetriNet;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * This class performs state space exploration to determine the reachability of each state
 * <p/>
 * It performs on the fly vanishing state elimination, producing the reachability graph for tangible states.
 * A tangible state is one in which:
 * a) Has no enabled transitions
 * b) Has entirely timed transitions leaving it
 *
 * A vanishing state is therefore one where there are immediate enabled transitions out of it. It can be eliminated
 * because no amount time is spent in this state (since there is an immediate transition out of it). This optimisation
 * reduces the memory needed to store the state space.
 *
 */
public class Reachability {

    /**
     * Value used to eliminate a vanishing state. We do not explore a state if the rate into it is
     * less than this value
     */
    private static final double EPSILON = 0.0000001;

    /**
     * The number of times that vanishing states
     */
    private static final int ALLOWED_ITERATIONS = 100000;

    /**
     * PetriNet to generate reachability graph for
     */
    private final PetriNet petriNet;

    /**
     * Token to perform reachability graph on
     */
    private final Token token;

    /**
     * Form in which to write transitions out to a Writer
     */
    private final WriterFormatter formatter;

    /**
     * Responsible for firing transitions in the Petri net. It is used to determine what transitions
     * are enabled for a given state
     */
    private final Animator animator;

    private Set<State> explored = new HashSet<>();


    public Reachability(PetriNet petriNet, Token token, WriterFormatter formatter) {
        this.token = token;
        this.formatter = formatter;
        this.petriNet = ClonePetriNet.clone(petriNet);
        animator = new PetriNetAnimator(this.petriNet);
    }


    /**
     * Performs state space exploration writing the results to the Writer stream.
     * That is it writes the transitions from each state to the writer.
     *
     * @param writer writer in which to write the output to
     */
    public void generate(Writer writer) {
        State initialState = createState();
        Deque<State> tangibleStack = new ArrayDeque<>();
        exploreInitialState(initialState, tangibleStack);
        stateSpaceExploration(tangibleStack, writer);
    }

    /**
     * Performs state space exploration of the tangibleStack
     * popping a state off the stack and exploring all its successors.
     * <p/>
     * It records the reachability graph into the writer
     *
     * @param tangibleStack stack containing tangible states to explore
     * @param writer        in which to record the reachability graph
     */
    private void stateSpaceExploration(Deque<State> tangibleStack, Writer writer) {
        while (!tangibleStack.isEmpty()) {
            State state = tangibleStack.pop();
            for (State successor : getSuccessors(state)) {
                if (isTangible(successor)) {
                    transition(state, successor, rate(state, successor), writer);
                    if (!explored.contains(successor)) {
                        tangibleStack.push(successor);
                        markAsExplored(successor);
                    }
                } else {
                    exploreVanishingStates(successor, tangibleStack, true, null);
                }
            }
        }
    }

    /**
     * Calculates the rate of a  transition from a tangible state to the successor state.
     * It does this by calculating the transitions that are enabled at the given state,
     * the transitions that can be reached from that state and performs the intersection of the two.
     * <p/>
     * It then sums the firing rates of this interesection and divides by the sum of the firing rates
     * of the enabeld transition
     */
    private double rate(State state, State successor) {
        Collection<Transition> transitionsToSuccessor = getTransitions(state, successor);
        return getWeightOfTransitions(transitionsToSuccessor);
    }

    /**
     * Populates tangibleStack with all starting tangible states.
     * <p/>
     * In the case that initialState is tangible then this is just
     * added to the queue.
     * <p/>
     * Otherwise it must sort through vanishing states
     *
     * @param initialState  starting state of the algorithm
     * @param tangibleStack queue to populate with tangible states
     */
    private void exploreInitialState(State initialState, Deque<State> tangibleStack) {
        if (isTangible(initialState)) {
            tangibleStack.push(initialState);
            markAsExplored(initialState);
        } else {
            exploreVanishingStates(initialState, tangibleStack, false, null);
        }

    }

    /**
     * Explores vanishing states in the tree. Adds any tangible states it finds
     * to the tangible exploration queue
     * <p/>
     * Cycles of vanishing states can be a problem since there is no explored table
     * for vanishing states. In order to ensure there is no infinite loop of exploration
     * states whose propogated effective entry rate falls below a certain threshold (EPSILON)
     * is dropped.
     * <p/>
     * Finally strongly connected components of vanishing states are known as timeless traps
     * They correspond to functional errors in the Markov Chain. In order to avoid this
     * an elimination timeout is applied which will expire after a number of attempts have
     * been made to eliminate a cluster of states/
     *
     * @param state              state to explore from
     * @param tangibleStack      queue of states to add found tangible states to
     * @param registerTransition if this is set to true, state transitions are registered
     * @param writer
     */
    //TODO: TIMELESS TRAP ELIMINATION
    private void exploreVanishingStates(State state, Deque<State> tangibleStack, boolean registerTransition,
                                        Writer writer) {
        Deque<VanishingRecord> vanishingStack = new ArrayDeque<>();
        vanishingStack.push(new VanishingRecord(state, 1.0));
        int iterations = 0;
        while (!vanishingStack.isEmpty() && iterations < ALLOWED_ITERATIONS) {
            VanishingRecord record = vanishingStack.pop();
            for (State successor : getSuccessors(record.getState())) {
                double successorRate = record.getRate() * probability(record.getState(), successor);
                if (isTangible(successor)) {
                    if (!explored.contains(successor)) {
                        tangibleStack.push(successor);
                        markAsExplored(successor);
                    }
                    if (registerTransition) {
                        transition(state, successor, successorRate, writer);
                    }
                } else {
                    if (successorRate > EPSILON) {
                        vanishingStack.push(new VanishingRecord(successor, successorRate));
                    }

                }
            }
            iterations++;
        }
        if (iterations == ALLOWED_ITERATIONS) {
            // TODO: Throw timeless trap
        }
    }

    private void transition(State state, State successor, double successorRate, Writer writer) {
        try {
            writer.write(formatter.format(state, successor, successorRate));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Works out what transitions would lead you to the successor state then divides the sum
     * of their rates by the total rates of all enabled transitions
     *
     * @param state     initial state
     * @param successor next state
     * @return the probability of transitioning to the successor state from state
     */
    //TODO: CHECK WITH WILL?
    private double probability(State state, State successor) {
        Collection<Transition> marked = getTransitions(state, successor);
        if (marked.isEmpty()) {
            return 0;
        }
        double toSuccessorWeight = getWeightOfTransitions(marked);
        double totalWeight = getWeightOfTransitions(animator.getEnabledTransitions());
        return toSuccessorWeight / totalWeight;
    }

    private double getWeightOfTransitions(Iterable<Transition> transitions) {
        double weight = 0;
        PetriNetWeightParser parser = new PetriNetWeightParser(petriNet);
        for (Transition transition : transitions) {
            FunctionalResults<Double> results = parser.evaluateExpression(transition.getRateExpr());
            if (!results.hasErrors()) {
                weight += results.getResult();
            } else {
                //TODO:
            }
        }
        return weight;
    }

    /**
     *
     * Calculates the set of transitions that will take you from one state to the successor.
     *
     * To perform this calculation the petri net is adjusted to match that of the given state.
     * All enabled transitions are then explored in the following manner:
     * 1) Assume the transition is part of the result
     * 2) Explore all outgoing arcs from, looking at the place they connect to.
     * 3) If at any point the place does not contain the same number of tokens as the successor state
     *    then remove it from the result
     * 4) Only the transitions that lead to the successor state will remain in the result
     *
     * This could benefit from memoization.
     *
     *
     * @param state initial state
     * @param successor successor state, must be directly reachable from the state
     * @return enabled transitions that take you from state to successor, if it is not directly reachable then
     *         an empty Collection will be returned
     */
    private Collection<Transition> getTransitions(State state, State successor) {
        setState(state);

        IncidenceMatrix forwardsIncidenceMatrix =
                petriNet.getForwardsIncidenceMatrix(token);
        IncidenceMatrix backwardsIncidenceMatrix = petriNet.getBackwardsIncidenceMatrix(token);

        Collection<Transition> stateEnabledTransitions = animator.getEnabledTransitions();
        Collection<Transition> result = new HashSet<>();

        //TODO: Memoization
        for (Transition transition : stateEnabledTransitions) {
            result.add(transition);
            Collection<Arc<Transition, Place>> outboundArcs = petriNet.outboundArcs(transition);
            for (Arc<Transition, Place> arc : outboundArcs) {
                Place place = arc.getTarget();
                String id = place.getId();
                int newTokenCount = state.getTokens(id) + forwardsIncidenceMatrix.get(place, transition)
                        - backwardsIncidenceMatrix.get(place, transition);
                if (newTokenCount != successor.getTokens(id)) {
                    result.remove(transition);
                    break;
                }
            }
        }
        return result;
    }


    /**
     *
     * Calculates successor states of the given state
     *
     * @param state current state
     * @return Set of successor states
     */
    private Iterable<State> getSuccessors(State state) {
        setState(state);
        Collection<Transition> enabled = animator.getEnabledTransitions();
        Collection<State> successors = new HashSet<>();
        for (Transition transition : enabled) {
            setState(state);
            animator.fireTransition(transition);
            State successor = createState();
            successors.add(successor);
        }
        return successors;
    }


    /**
     * Adds a compressed version of a tangible state to exploredStates
     *
     * @param state
     */
    //TODO: IMPLEMENT COMPRESSED VERSION
    private void markAsExplored(State state) {
        explored.add(state);
    }

    /**
     * Creates a new state containing the token counts for the
     * current Petri net.
     *
     * @return current state of the Petri net
     */
    private State createState() {
        Map<String, Integer> tokenCounts = new HashMap<>();
        for (Place place : petriNet.getPlaces()) {
            tokenCounts.put(place.getId(), place.getTokenCount(token));
        }
        return new HashedState(tokenCounts);
    }

    /**
     * Sets the Petri net to this state
     *
     * @param state contains the token counts to set the places to
     */
    private void setState(State state) {
        for (Place place : petriNet.getPlaces()) {
            place.setTokenCount(token, state.getTokens(place.getId()));
        }
    }

    /**
     * A tangible state is one in which:
     * a) Has no enabled transitions
     * b) Has entirely timed transitions leaving it
     *
     * @param state to test for tangibility
     * @return true if the current state is tangible
     */
    private boolean isTangible(State state) {
        setState(state);
        Set<Transition> enabledTransitions = animator.getEnabledTransitions();
        boolean anyTimed = false;
        boolean anyImmediate = false;
        for (Transition transition : enabledTransitions) {
            if (transition.isTimed()) {
                anyTimed = true;
            } else {
                anyImmediate = true;
            }
        }
        return enabledTransitions.isEmpty() || (anyTimed && !anyImmediate);
    }


}