package com.framework.core.notifications.commandes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.framework.ecs.Mapper;
import com.framework.ecs.meta.components.ClassComponent;
import com.framework.enums.ColliderType;
import com.framework.enums.EntityType;
import com.framework.enums.PlayerType;

public class ContactEvent {

    private static ContactEvent instance;

    private boolean start;

    private Fixture A;
    private Fixture B;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public ContactEvent() {
    }

    public static ContactEvent getInstance(boolean start) {
        if (instance == null) {
            instance = new ContactEvent();
        }
        instance.start = start;
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Entity entityA() {
        return (Entity) A.getBody().getUserData();
    }

    public Entity entityB() {
        return (Entity) B.getBody().getUserData();
    }

    public boolean isStart() {
        return start;
    }

    /*------------------------------*\
    |*			  Type
    \*------------------------------*/

    public EntityType typeA() {
        return Mapper.type_m.get(entityA()).getType();
    }

    public EntityType typeB() {
        return Mapper.type_m.get(entityB()).getType();
    }

    /*------------------------------*\
    |*			  Class
    \*------------------------------*/

    public ClassComponent classA() {
        try {
            return Mapper.class_m.get(entityA());
        } catch (Exception ignored) {
            return null;
        }
    }

    public ClassComponent classB() {
        try {
            return Mapper.class_m.get(entityB());
        } catch (Exception ignored) {
            return null;
        }
    }

    /*------------------------------*\
    |*			  Owner
    \*------------------------------*/

    public PlayerType ownerA() {
        return Mapper.owner_m.get(entityA()).getOwner();
    }

    public PlayerType ownerB() {
        return Mapper.owner_m.get(entityB()).getOwner();
    }

    /*------------------------------*\
    |*			  Collider
    \*------------------------------*/

    public ColliderType colliderA() {
        return (ColliderType) A.getUserData();
    }

    public ColliderType colliderB() {
        return (ColliderType) B.getUserData();
    }

    public boolean aIsBody() {
        return A.getUserData() == ColliderType.BODY;
    }

    public boolean bIsBody() {
        return B.getUserData() == ColliderType.BODY;
    }

    public boolean aIsCac() {
        return A.getUserData() == ColliderType.CAC;
    }

    public boolean bIsCac() {
        return B.getUserData() == ColliderType.CAC;
    }

    public boolean aIsLOS() {
        return A.getUserData() == ColliderType.LOS;
    }

    public boolean bIsLOS() {
        return B.getUserData() == ColliderType.LOS;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setA(Fixture a) {
        A = a;
    }

    public void setB(Fixture b) {
        B = b;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    public void clear() {
        this.A = null;
        this.B = null;
    }

}
