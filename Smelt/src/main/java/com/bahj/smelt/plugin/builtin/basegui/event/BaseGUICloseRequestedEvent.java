package com.bahj.smelt.plugin.builtin.basegui.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;

/**
 * <p>
 * Indicates that the base GUI has been asked to close.  Listeners may respond to this event by providing a challenge
 * function.  If the challenge function returns <code>false</code>, then the close operation will be cancelled.
 * Challenge functions are provided by registering them with this event.  Recipients are encouraged to provide challenge
 * functions rather than performing their objections immediately to avoid holding up the event queue.
 * </p><p>
 * If a challenge returns <code>false</code>, none of the remaining challenges are tested.  For this reason, plugins
 * should not rely on closing challenges for clean-up operations.
 * </p>
 * 
 * @author Zachary Palmer
 */
public class BaseGUICloseRequestedEvent extends BaseGUIEvent {
    private List<Function<GUIExecutionContext,Boolean>> challenges;
    
    public BaseGUICloseRequestedEvent() {
        this.challenges = new ArrayList<>();
    }
    
    /**
     * Adds a challenge to this close request.  If the challenge fails (the supplier returns <code>false</code>), the
     * close will be cancelled.
     * @param challenge The challenge to add.
     */
    public void addChallenge(Function<GUIExecutionContext,Boolean> challenge) {
        this.challenges.add(challenge);
    }
    
    public List<Function<GUIExecutionContext,Boolean>> getChallenges() {
        return Collections.unmodifiableList(this.challenges);
    }
}
