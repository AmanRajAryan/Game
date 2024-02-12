package aman.three;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class TouchPad {

    private Stage stage;
    public Touchpad touchpad;
    private static Touchpad.TouchpadStyle touchpadStyle;
    private static Skin touchpadSkin;
    private static Drawable touchBackground;
    private static Drawable touchKnob;

    // pass Stage to TouchPad

    public TouchPad(Stage stage) {
        this.stage = stage;

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("JoystickSplitted.png"));
        touchpadSkin.add("touchKnob", new Texture("SmallHandleFilledGrey.png"));

        touchpadStyle = new Touchpad.TouchpadStyle();

        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");

        touchpadStyle.knob = touchKnob;
        touchpadStyle.background = touchBackground;

        // Create the touchpad and add it to the stage
        touchpad = new Touchpad(10f, touchpadStyle);
        touchpad.setBounds(100, 100, 200, 200);
        stage.addActor(touchpad);

        stage.addListener(
                new InputListener() {

                    Vector2 p = new Vector2();
                    Rectangle b = new Rectangle();

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        boolean isScreenTouched = false;
                        if (!touchpad.isTouched()) {
                            if (event.getTarget() != touchpad) {

                                if (x < Gdx.graphics.getWidth() / 2) {

                                    b.set(
                                            touchpad.getX(),
                                            touchpad.getY(),
                                            touchpad.getWidth(),
                                            touchpad.getHeight());
                                    b.setCenter(x, y);
                                    touchpad.setBounds(b.x, b.y, b.width, b.height);

                                    // Let the touchpad know to start tracking touch
                                    touchpad.fire(event);
                                    isScreenTouched = true;
                                }
                            }
                        }
                        return isScreenTouched;
                    }

                    @Override
                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        touchpad.stageToLocalCoordinates(p.set(x, y));
                        if (touchpad.hit(p.x, p.y, true) == null) {
                            // If we moved outside of the touchpad, have it follow our touch
                            // position;
                            // but we want to keep the direction of the knob, so shift to the edge
                            // of the
                            // touchpad's radius with a small amount of smoothing, so it looks nice.
                            p.set(-touchpad.getKnobPercentX(), -touchpad.getKnobPercentY())
                                    .nor()
                                    .scl(Math.min(touchpad.getWidth(), touchpad.getHeight()) * 0.5f)
                                    .add(x, y);
                            touchpad.addAction(
                                    Actions.moveToAligned(p.x, p.y, Align.center, 0.15f));
                        }
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // Put the touchpad back to its original position
                        touchpad.clearActions();
                        touchpad.addAction(Actions.moveTo(110, 110, 0.15f));
                    }
                });
    }

    // refresh stage in render function of main activity
    // by adding this after batch.end()

    // stage.act(Gdx.graphics.getDeltaTime());
    // stage.draw();

    public Touchpad getTouchpad() {
        return touchpad;
    }
}
