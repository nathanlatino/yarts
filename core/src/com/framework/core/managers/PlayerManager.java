package com.framework.core.managers;

import com.framework.core.factories.ComponentFactory;

public class PlayerManager {

    private static PlayerManager instance;

    private float gold;
    private int population;

    private PlayerManager entityFactory;
    private ComponentFactory componentFactory;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private PlayerManager() {
        gold = 0;
        population = 0;
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public float getGold() {
        return gold;
    }

    public void setGold(float gold) {
        this.gold = gold;
    }

    public void incrementGold(float amount) {
        this.gold += amount;
    }

    public void DecrementGold(float amount) {
        this.gold -= amount;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
