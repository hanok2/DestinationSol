/*
 * Copyright 2020 The Terasology Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.destinationsol.world.generators;

import org.destinationsol.Const;
import org.destinationsol.game.context.Context;
import org.destinationsol.game.context.internal.ContextImpl;
import org.destinationsol.modules.ModuleManager;
import org.destinationsol.world.WorldBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.destinationsol.world.generators.SolarSystemGenerator.MAZE_GAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolarSystemGeneratorTest {
    private Context context;
    private ModuleManager moduleManager;
    WorldBuilder worldBuilder;
    SolarSystemGenerator solarSystemGenerator;

    @BeforeEach
    public void setUp() throws Exception {
        context = new ContextImpl();
        moduleManager = new ModuleManager();
        moduleManager.init();
        context.put(ModuleManager.class, moduleManager);
        worldBuilder = new WorldBuilder(context, 1);

        ArrayList<SolarSystemGenerator> solarSystemGenerators = worldBuilder.initializeRandomSolarSystemGenerators();
        solarSystemGenerator = solarSystemGenerators.get(0);
        setupSolarSystemGenerator();
    }

    private void setupSolarSystemGenerator() {
        solarSystemGenerator.setPlanetCount(5);
        solarSystemGenerator.setPossibleBeltCount(1);
        solarSystemGenerator.setMazeCount(2);
        solarSystemGenerator.setSunCount(1);
        solarSystemGenerator.initializeRandomSunGenerators();
        solarSystemGenerator.initializeRandomPlanetGenerators();
        solarSystemGenerator.initializeRandomMazeGenerators();
        solarSystemGenerator.initializeRandomBeltGenerators(1f);
        solarSystemGenerator.buildFeatureGenerators();
        solarSystemGenerator.setRadius(solarSystemGenerator.calcSolarSystemRadius());
        solarSystemGenerator.calculateSunPositionOneSun();
        solarSystemGenerator.calculateMazePositions();
        solarSystemGenerator.calculatePlanetPositions();
        solarSystemGenerator.calculateBeltPositions();
    }

    @Test
    void hasCorrectPlanetCountForDefaultSolarSystem() {
        if(solarSystemGenerator.getClass().equals(SolarSystemGeneratorImpl.class)) {
            assertEquals(solarSystemGenerator.getPlanetCount(), 5);
        }
    }

    @Test
    void hasCorrectMazeCountForDefaultSolarSystem() {
        if(solarSystemGenerator.getClass().equals(SolarSystemGeneratorImpl.class)) {
            assertEquals(solarSystemGenerator.getMazeCount(), 2);
        }
    }

    @Test
    void hasCorrectPossibleBeltCountForDefaultSolarSystem() {
        if(solarSystemGenerator.getClass().equals(SolarSystemGeneratorImpl.class)) {
            assertEquals(solarSystemGenerator.getPossibleBeltCount(), 1);
        }
    }

    @Test
    void hasCorrectRadius() {
        float radiusFromFeatures = 0;
        radiusFromFeatures += Const.SUN_RADIUS;
        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            radiusFromFeatures += Const.PLANET_GAP;
            radiusFromFeatures += featureGenerator.getRadius();
            radiusFromFeatures += featureGenerator.getRadius();
            radiusFromFeatures += Const.PLANET_GAP;
        }
        radiusFromFeatures += MAZE_GAP;
        //This value will tend to be off by 0.0001 even if calculated correctly, so we are testing if they are very close
        assertTrue(solarSystemGenerator.getRadius() - radiusFromFeatures < 1f);

    }

    @Test
    void mazesAreCorrectDistanceFromSolarSystemCenter() {
        float actualMazeDistance = 0;
        float expectedMazeDistance = 0;
        expectedMazeDistance = solarSystemGenerator.getRadius() - MAZE_GAP - SolarSystemGenerator.MAX_MAZE_RADIUS;

        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            if (featureGenerator.getClass().getSuperclass().equals(MazeGenerator.class)) {
                actualMazeDistance = featureGenerator.getPosition().dst(solarSystemGenerator.getPosition());
                //This value will tend to be off by 0.0001 even if calculated correctly, so we are testing if they are very close
                assertTrue(expectedMazeDistance- actualMazeDistance < 1f);
            }
        }
    }

    @Test
    void beltPositionEqualsSolarSystemPosition() {
        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            if (featureGenerator.getClass().getSuperclass().equals(BeltGenerator.class)) {
                assertEquals(featureGenerator.getPosition(), solarSystemGenerator.getPosition());
            }
        }
    }

    @Test
    void sunPositionEqualsSolarSystemPosition() {
        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            if (featureGenerator.getClass().getSuperclass().equals(SunGenerator.class)) {
                assertEquals(featureGenerator.getPosition(), solarSystemGenerator.getPosition());
            }
        }
    }

    @Test
    void planetAreInsideSolarSystem() {
        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            if (featureGenerator.getClass().getSuperclass().equals(PlanetGenerator.class)) {
                assertTrue(featureGenerator.getPosition().dst(solarSystemGenerator.getPosition()) < solarSystemGenerator.getRadius());
            }
        }
    }

    @Test
    void beltsAreInsideSolarSystem() {
        for (FeatureGenerator featureGenerator : solarSystemGenerator.getActiveFeatureGenerators()) {
            if (featureGenerator.getClass().getSuperclass().equals(BeltGenerator.class)) {
                assertTrue(featureGenerator.getPosition().dst(solarSystemGenerator.getPosition()) < solarSystemGenerator.getRadius());
            }
        }
    }
}
