package aman.three;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import static java.lang.Math.atan2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.Scene;

public class PlayerController {

    MyGame mainGameClass;
    float speed = 5f;
    float rotationSpeed = 80f;
    private final Matrix4 playerTransform = new Matrix4();
    private final Vector3 moveTranslation = new Vector3();
    private final Vector3 currentPosition = new Vector3();

    private float camPitch = Settings.CAMERA_START_PITCH;
    float floatToCalculateCameraRotation = 0f;
    float extraVerticalDistance = 15;
    public float distanceFromPlayer = 20f;
    private float angleAroundPlayer = 0f;
    private float angleBehindPlayer = 0f;

    boolean inTouchpad; // Erkka: if the touch is in the touchpad, false otherwise
    PerspectiveCamera camera;
    Scene playerScene;
    Stage stage;
    TouchPad touchpad;
    int deltaX; // Erkka: a helper variable to store how much the touch has been dragged sideways
    float touchpadX; // Erkka: two more helper functions to store the touchpad knob position
    float touchpadY;
    float touchpadAngle; // Erkka: another helper function to store the touchpad angle. 0 for

    float previousGetX;
    float previousGetY;
    boolean cameraCanBeRotatedNow = false;

    BitmapFont font = new BitmapFont();
    Label.LabelStyle lableStyle = new Label.LabelStyle(font, Color.BLACK);
    Label getXLabel = new Label("Label", lableStyle);
    Label getYLabel = new Label("Label", lableStyle);
    Label getY1Label = new Label("Label", lableStyle);
    Label getX1Label = new Label("Label", lableStyle);
    Label getDeltaYLabel = new Label("Label", lableStyle);
    Label getDeltaY1Label = new Label("Label", lableStyle);
    Label camPitchLabel = new Label("Label", lableStyle);
    Label floatToCalculateCamRotationLabel = new Label("Label", lableStyle);
    Label pitchChangeLabel = new Label("Label", lableStyle);

    public void createContoller(MyGame game) {
        this.mainGameClass = game;
        this.stage = mainGameClass.stage;
        camera = mainGameClass.camera;
        playerScene = mainGameClass.playerScene;
        touchpad = mainGameClass.touchpad;

        getXLabel.setFontScale(2);
        getXLabel.setPosition(30, 700);
        stage.addActor(getXLabel);

        getX1Label.setFontScale(2);
        getX1Label.setPosition(30, 670);
        stage.addActor(getX1Label);

        getYLabel.setFontScale(2);
        getYLabel.setPosition(30, 640);
        stage.addActor(getYLabel);

        getY1Label.setFontScale(2);
        getY1Label.setPosition(30, 610);
        stage.addActor(getY1Label);

        getDeltaYLabel.setFontScale(2);
        getDeltaYLabel.setPosition(30, 580);
        stage.addActor(getDeltaYLabel);

        getDeltaY1Label.setFontScale(2);
        getDeltaY1Label.setPosition(30, 550);
        stage.addActor(getDeltaY1Label);

        camPitchLabel.setFontScale(2);
        camPitchLabel.setPosition(30, 520);
        stage.addActor(camPitchLabel);

        floatToCalculateCamRotationLabel.setFontScale(2);
        floatToCalculateCamRotationLabel.setPosition(30, 490);
        stage.addActor(floatToCalculateCamRotationLabel);

        pitchChangeLabel.setFontScale(2);
        pitchChangeLabel.setPosition(30, 460);
        stage.addActor(pitchChangeLabel);
    }

    public void processInput(float deltaTime) {
        getXLabel.setText("Gdx.input.getX() : " + Gdx.input.getX());
        getX1Label.setText("Gdx.input.getX(1) : " + Gdx.input.getX(1));
        getYLabel.setText("Gdx.input.getY() : " + Gdx.input.getY());
        getY1Label.setText("Gdx.input.getY(1) : " + Gdx.input.getY(1));
        getDeltaYLabel.setText("Gdx.input.getDeltaY() : " + Gdx.input.getDeltaY());
        getDeltaY1Label.setText("Gdx.input.getDeltaY(1) : " + Gdx.input.getDeltaY(1));

        // Update the player transform
        playerTransform.set(playerScene.modelInstance.transform);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveTranslation.z += speed * deltaTime;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveTranslation.z -= speed * deltaTime;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerTransform.rotate(Vector3.Y, rotationSpeed * deltaTime);
            angleBehindPlayer += rotationSpeed * deltaTime;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerTransform.rotate(Vector3.Y, -rotationSpeed * deltaTime);
            angleBehindPlayer -= rotationSpeed * deltaTime;
        }

        if (touchpad.getTouchpad().isTouched()) {
            cameraCanBeRotatedNow = false;
            // here we handle the touchpad
            inTouchpad = true;
            mainGameClass.sprinting = false;

            touchpadX = touchpad.getTouchpad().getKnobPercentX();
            touchpadY = touchpad.getTouchpad().getKnobPercentY();

            if ((touchpadX != 0) || (touchpadY != 0)) {

                mainGameClass.isWalking = true;
                // Erkka: atan2() function returns the angle in radians, so we convert it to degrees
                // by *180/pi
                touchpadAngle = (float) (atan2(touchpadY, touchpadX) * 180.0d / Math.PI);

                // we now have the absolute toucpadAngle, ie. 0 is always to the east, 90 always to
                // the right
                // but the camera might be rotated, and we probably want knob to the right moving
                // the character to the right of the screen, not to the absolute east
                // luckily, we have the camera facing in angleAroundPlayer
                // but angleAroundPlayer has 0 to north, and seems to be clock-wise
                // so we need to do some computation to make angleAroundPlayer compatible
                // with the angle we calculated for the knob

                float convertedAngle = (360 - (angleAroundPlayer - 90));
                while (convertedAngle < 0) convertedAngle += 360;

                touchpadAngle -= convertedAngle;

                rotatePlayerInGivenDirection(null, touchpadAngle);
                moveTranslation.z += speed * deltaTime;
            }

        } else {
            inTouchpad = false;
            mainGameClass.isWalking = false;
        }

        if (!inTouchpad) {
            cameraCanBeRotatedNow = true;
            // Erkka: here we read the touch dragged horizontally, outside the touchpad

            if (deltaX != 0) {
                if (mainGameClass.sprinting) {
                    rotatePlayer(-deltaX);
                }

                // if in sprint mode, we rotate both the player and the camera
                // if not in sprint mode, we rotate only the camera

            }
        }

        if (mainGameClass.isSprintBtnJustClicked) {
            rotatePlayerInCamDirection(null);
            mainGameClass.isSprintBtnJustClicked = false;
        }

        if (mainGameClass.sprinting) {
            moveTranslation.z += 10f * deltaTime;
        }

        deltaX = Gdx.input.getDeltaX();

        if (!mainGameClass.sprinting) {
            if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
                cameraCanBeRotatedNow = true;
            } else {
                rotateCamera(-Gdx.input.getDeltaX(1));
            }

            if (cameraCanBeRotatedNow) {
                rotateCamera(-deltaX);
            }

        } else {
            rotateCamera(-deltaX);
        }

        // Apply the move translation to the transform
        playerTransform.translate(moveTranslation);

        // Set the modified transform
        playerScene.modelInstance.transform.set(playerTransform);

        // Update vector position
        playerScene.modelInstance.transform.getTranslation(currentPosition);

        // Clear the move translation out
        moveTranslation.set(0, 0, 0);
    }

    public void updateCamera() {
        float horDistance = calculateHorizontalDistance(distanceFromPlayer);
        float vertDistance = calculateVerticalDistance(distanceFromPlayer + extraVerticalDistance);

        calculatePitch();
        calculateCameraPosition(currentPosition, -horDistance, vertDistance);
        camera.up.set(Vector3.Y);
        camera.lookAt(
                currentPosition.x,
                currentPosition.y + floatToCalculateCameraRotation * 1.2f,
                currentPosition.z);
        camera.update();
    }

    private void calculateCameraPosition(
            Vector3 currentPosition, float horDistance, float vertDistance) {
        float offsetX = (float) (horDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float) (horDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

        camera.position.x = currentPosition.x - offsetX;
        camera.position.z = currentPosition.z - offsetZ;
        camera.position.y = currentPosition.y + vertDistance;
    }

    public void rotateCamera(float angle) {
        angleAroundPlayer += angle;
        if (angleAroundPlayer >= 360) angleAroundPlayer -= 360;
        if (angleAroundPlayer < 0) angleAroundPlayer += 360;
    }

    public void rotatePlayer(float angle) {
        playerTransform.rotate(Vector3.Y, angle);
        angleBehindPlayer += angle;
        if (angleBehindPlayer >= 360) angleBehindPlayer -= 360;
        if (angleBehindPlayer < 0) angleBehindPlayer += 360;
    }

    public void rotatePlayerInCamDirection(Float rapidRotationMax) {

        // playerTransform.rotate(Vector3.Y , camera.direction);
        // Erkka: this is what you had. I try to simplify the problem:
        // suppose camera.direction is 270 degrees
        // playerTransfrom.rotate would then turn the player 270 degrees
        // and the next time this is run, it would again turn the player 270 degrees
        // so, playerTransfrom.rotate is not "rotate to given angle" but "rotate by given angle"
        // and then it gets more complicated, since I think camera.direction has the angles for X,Y
        // and Z axises

        // we get the difference between camera angle and the angle behind the player
        float diff = angleAroundPlayer - angleBehindPlayer;
        if (diff >= 360) diff -= 360;
        if (diff < 0) diff += 360;

        // if rapidRotationMax is null, we will turn the player all the way so that the player will
        // immediately face the camera direction
        // but if we need an animation-style slow turn towards the camera, then we'd need to set the
        // rapidRotationMax
        if (rapidRotationMax != null) {
            if (diff > rapidRotationMax) diff = rapidRotationMax;
            if (diff < -rapidRotationMax) diff = -rapidRotationMax;
        }

        // and then we turn
        rotatePlayer(diff);
    }

    public void rotatePlayerInGivenDirection(Float rapidRotationMax, float towards) {

        // we get the difference between the desired angle and the angle behind the player
        float diff = towards - angleBehindPlayer;
        if (diff >= 360) diff -= 360;
        if (diff < 0) diff += 360;

        // if rapidRotationMax is null, we will turn the player all the way so that the player will
        // immediately face the camera direction
        // but if we need an animation-style slow turn towards the camera, then we'd need to set the
        // rapidRotationMax
        if (rapidRotationMax != null) {
            if (diff > rapidRotationMax) diff = rapidRotationMax;
            if (diff < -rapidRotationMax) diff = -rapidRotationMax;
        }

        // and then we turn
        rotatePlayer(diff);
    }

    private void calculatePitch() {
        float pitchChange = 0;

        

        floatToCalculateCamRotationLabel.setText(
                "floatToCalculateCameraRotation : " + floatToCalculateCameraRotation);

        if (Gdx.input.isTouched(0)) {
            if (Gdx.input.getX(0) > Gdx.graphics.getWidth() / 2) {
                pitchChange = -Gdx.input.getDeltaY(0) * Settings.CAMERA_PITCH_FACTOR;
                camPitch -= pitchChange;

                // touch drag is downward
                if (Gdx.input.getDeltaY(0) > 0) {
                    if (floatToCalculateCameraRotation > 0) {
                        floatToCalculateCameraRotation += pitchChange;
                        distanceFromPlayer -= pitchChange * 0.5;
                        extraVerticalDistance -= pitchChange * 2.5;
                    }
                }
            }
        }
        if (Gdx.input.isTouched(1)) {
            if (Gdx.input.getX(1) > Gdx.graphics.getWidth() / 2) {
                pitchChange = -Gdx.input.getDeltaY(1) * Settings.CAMERA_PITCH_FACTOR;
                camPitch -= pitchChange;

                // touch drag is downward
                if (Gdx.input.getDeltaY(1) > 0) {
                    if (floatToCalculateCameraRotation > 0) {
                        floatToCalculateCameraRotation += pitchChange;
                        distanceFromPlayer -= pitchChange * 0.5;
                        extraVerticalDistance -= pitchChange * 2.5;
                    }
                }
            }
        }

        camPitchLabel.setText("camPitch : " + camPitch);
        pitchChangeLabel.setText("pitchChange : " + pitchChange);

        if (camPitch < Settings.CAMERA_MIN_PITCH) {
            camPitch = Settings.CAMERA_MIN_PITCH;

            if (floatToCalculateCameraRotation < 10) {
                floatToCalculateCameraRotation += pitchChange;
                distanceFromPlayer -= pitchChange * 0.5;
                extraVerticalDistance -= pitchChange * 2.5;
            }
        } else if (camPitch > Settings.CAMERA_MAX_PITCH) camPitch = Settings.CAMERA_MAX_PITCH;
    }

    private float calculateVerticalDistance(float distanceFromPlayer) {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
    }

    private float calculateHorizontalDistance(float distanceFromPlayer) {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
    }
}
