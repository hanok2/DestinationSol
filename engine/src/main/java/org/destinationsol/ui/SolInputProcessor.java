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
package org.destinationsol.ui;

import com.badlogic.gdx.InputProcessor;
import org.destinationsol.game.screens.ConsoleScreen;

import java.util.Optional;

public class SolInputProcessor implements InputProcessor {

    private final SolInputManager inputManager;

    SolInputProcessor(SolInputManager inputMan) {
        inputManager = inputMan;
    }

    @Override
    public boolean keyDown(int keyCode) {
        inputManager.maybeFlashPressed(keyCode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        Optional<ConsoleScreen> optionalScreen = ConsoleScreen.getInstance();
        if(optionalScreen.isPresent()) {
            optionalScreen.get().onCharEntered(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputManager.maybeFlashPressed(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        inputManager.maybeTouchDragged(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        inputManager.scrolled(amountY > 0);
        return false;
    }
}
