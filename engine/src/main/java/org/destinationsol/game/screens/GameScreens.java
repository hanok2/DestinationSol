/*
 * Copyright 2018 MovingBlocks
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
package org.destinationsol.game.screens;

import org.destinationsol.SolApplication;
import org.destinationsol.game.context.Context;
import org.destinationsol.ui.SolLayouts;

public class GameScreens {
    public final MainGameScreen mainGameScreen;
    public final MapScreen mapScreen;
    public final MenuScreen menuScreen;
    public final InventoryScreen inventoryScreen;
    public final TalkScreen talkScreen;
    public final WaypointCreationScreen waypointCreationScreen;
    public final ConsoleScreen consoleScreen;

    public GameScreens(SolApplication cmp, Context context) {
        SolLayouts layouts = cmp.getLayouts();
        RightPaneLayout rightPaneLayout = layouts.rightPaneLayout;
        mainGameScreen = new MainGameScreen(rightPaneLayout, context);
        mapScreen = new MapScreen(rightPaneLayout, cmp.isMobile(), cmp.getOptions());
        menuScreen = new MenuScreen(layouts.menuLayout, cmp.getOptions());
        inventoryScreen = new InventoryScreen(cmp.getOptions());
        talkScreen = new TalkScreen(layouts.menuLayout, cmp.getOptions());
        waypointCreationScreen = new WaypointCreationScreen(layouts.menuLayout, cmp.getOptions(), mapScreen);
        consoleScreen = new ConsoleScreen(context);
    }

    // This was added for PlayerCreatorTest.java (used in PlayerCreator)
    // so that it can successfully mock the returned result.
    public MainGameScreen getMainGameScreen() {
        return mainGameScreen;
    }
}
