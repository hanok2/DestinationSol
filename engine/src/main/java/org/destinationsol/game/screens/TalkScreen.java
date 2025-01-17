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

import com.badlogic.gdx.math.Rectangle;
import org.destinationsol.GameOptions;
import org.destinationsol.SolApplication;
import org.destinationsol.common.SolColor;
import org.destinationsol.game.Hero;
import org.destinationsol.game.SolGame;
import org.destinationsol.game.ship.SolShip;
import org.destinationsol.game.ship.hulls.HullConfig;
import org.destinationsol.menu.MenuLayout;
import org.destinationsol.ui.SolInputManager;
import org.destinationsol.ui.SolUiBaseScreen;
import org.destinationsol.ui.SolUiControl;
import org.destinationsol.ui.UiDrawer;

public class TalkScreen extends SolUiBaseScreen {
    public static final float MAX_TALK_DIST = 1f;

    public final SolUiControl buyControl;
    public final SolUiControl closeControl;
    private final SolUiControl sellControl;
    private final SolUiControl shipsControl;
    private final SolUiControl hireControl;

    private final Rectangle background;
    private SolShip target;

    TalkScreen(MenuLayout menuLayout, GameOptions gameOptions) {
        sellControl = new SolUiControl(menuLayout.buttonRect(-1, 0), true, gameOptions.getKeySellMenu());
        sellControl.setDisplayName("Sell");
        controls.add(sellControl);

        buyControl = new SolUiControl(menuLayout.buttonRect(-1, 1), true, gameOptions.getKeyBuyMenu());
        buyControl.setDisplayName("Buy");
        controls.add(buyControl);

        shipsControl = new SolUiControl(menuLayout.buttonRect(-1, 2), true, gameOptions.getKeyChangeShipMenu());
        shipsControl.setDisplayName("Change Ship");
        controls.add(shipsControl);

        hireControl = new SolUiControl(menuLayout.buttonRect(-1, 3), true, gameOptions.getKeyHireShipMenu());
        hireControl.setDisplayName("Hire");
        controls.add(hireControl);

        closeControl = new SolUiControl(menuLayout.buttonRect(-1, 4), true, gameOptions.getKeyClose());
        closeControl.setDisplayName("Close");
        controls.add(closeControl);

        background = menuLayout.background(-1, 0, 5);
    }

    @Override
    public void updateCustom(SolApplication solApplication, SolInputManager.InputPointer[] inputPointers, boolean clickedOutside) {
        if (clickedOutside) {
            closeControl.maybeFlashPressed(solApplication.getOptions().getKeyClose());
            return;
        }
        SolGame game = solApplication.getGame();
        Hero hero = game.getHero();
        SolInputManager inputManager = solApplication.getInputManager();
        if (closeControl.isJustOff() || isTargetFar(hero)) {
            inputManager.setScreen(solApplication, game.getScreens().mainGameScreen);
            return;
        }

        boolean station = target.getHull().config.getType() == HullConfig.Type.STATION;
        shipsControl.setEnabled(station);
        hireControl.setEnabled(station);

        InventoryScreen inventoryScreen = game.getScreens().inventoryScreen;
        boolean sell = sellControl.isJustOff();
        boolean buy = buyControl.isJustOff();
        boolean sellShips = shipsControl.isJustOff();
        boolean hire = hireControl.isJustOff();
        if (sell || buy || sellShips || hire) {
            inventoryScreen.setOperations(sell ? inventoryScreen.sellItems : buy ? inventoryScreen.buyItemsScreen : sellShips ? inventoryScreen.changeShipScreen : inventoryScreen.hireShipsScreen);
            inputManager.setScreen(solApplication, game.getScreens().mainGameScreen);
            inputManager.addScreen(solApplication, inventoryScreen);
        }
    }

    boolean isTargetFar(Hero hero) {
        if (hero.isTranscendent() || target == null || target.getLife() <= 0) {
            return true;
        }
        float dst = target.getPosition().dst(hero.getPosition()) - hero.getHull().config.getApproxRadius() - target.getHull().config.getApproxRadius();
        return MAX_TALK_DIST < dst;
    }

    @Override
    public void drawBackground(UiDrawer uiDrawer, SolApplication solApplication) {
        uiDrawer.draw(background, SolColor.UI_BG);
    }

    @Override
    public boolean reactsToClickOutside() {
        return true;
    }

    @Override
    public boolean isCursorOnBackground(SolInputManager.InputPointer inputPointer) {
        return background.contains(inputPointer.x, inputPointer.y);
    }

    public SolShip getTarget() {
        return target;
    }

    public void setTarget(SolShip target) {
        this.target = target;
    }
}
