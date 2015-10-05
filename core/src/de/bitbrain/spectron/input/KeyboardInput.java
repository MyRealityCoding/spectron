package de.bitbrain.spectron.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import de.bitbrain.spectron.core.GameObjectController;

public class KeyboardInput extends InputAdapter {

    private GameObjectController controller;

    public KeyboardInput(GameObjectController controller) {
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                controller.move(0, GameObjectController.Move.UP);
                return true;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                controller.move(0, GameObjectController.Move.LEFT);
                return true;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                controller.move(0, GameObjectController.Move.DOWN);
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                controller.move(0, GameObjectController.Move.RIGHT);
                return true;
        }
        return false;
    }
}
