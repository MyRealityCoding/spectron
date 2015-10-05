package de.bitbrain.spectron.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import de.bitbrain.spectron.core.GameObjectController;

public class GamepadInput implements ControllerListener {

    private GameObjectController controller;

    public GamepadInput(GameObjectController controller) {
        this.controller = controller;
    }
    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller c, int buttonCode) {
        switch (buttonCode) {
            case Input.Keys.UP:
                controller.move(0, GameObjectController.Move.UP);
                return true;
            case Input.Keys.LEFT:
                controller.move(0, GameObjectController.Move.LEFT);
                return true;
            case Input.Keys.DOWN:
                controller.move(0, GameObjectController.Move.DOWN);
                return true;
            case Input.Keys.RIGHT:
                controller.move(0, GameObjectController.Move.RIGHT);
                return true;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
