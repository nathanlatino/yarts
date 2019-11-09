package com.framework.core.factories;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.framework.core.ai.steering.SteeringHandler;
import com.framework.utils.Box2DLocation;

public class SteeringFactory {

    static SteeringFactory instance;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private SteeringFactory() {

    }

    public static SteeringFactory getInstance() {
        if (instance == null) {
            instance = new SteeringFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*						     Steering Behaviors
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Poursue
    \*------------------------------*/


    public SteeringBehavior createPoursueBehavior(SteeringHandler seeker, SteeringHandler target) {
        return new Pursue(seeker, target).setMaxPredictionTime(.1f);
    }

    /*------------------------------*\
    |*			  Flee
    \*------------------------------*/

    public SteeringBehavior createFleeBehavior(SteeringHandler runner, SteeringHandler target) {
        return new Flee(runner, target);
    }

    /*------------------------------*\
    |*			  Arrive
    \*------------------------------*/
    public SteeringBehavior createArriveBehavior(SteeringHandler runner, Vector2 target) {
        return new Arrive(runner, new Box2DLocation(target))
                .setTimeToTarget(.1f) // default 0.1f
                .setArrivalTolerance(0f) //
                .setDecelerationRadius(1f);
    }
    /*------------------------------*\
    |*			 Follow path
    \*------------------------------*/

    public SteeringBehavior createFollowPathBehavior(SteeringHandler runner, Array<Vector2> path) {
        return new FollowPath(runner, new LinePath(path))
                .setTimeToTarget(.1f)
                .setDecelerationRadius(.1f)
                .setPathOffset(.1f)
                .setPredictionTime(.1f);
    }
}
