package aman.three;

import aman.three.MyGame;
import aman.three.TouchPad;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class HUD {

    ImageButton sprintBtn;
    ImageButton jumpBtn;
    ImageButton crouchBtn;

    public void initializeHUD(MyGame GAME, Stage stage) {

        GAME.touchpad = new TouchPad(stage);

        // sprint button
        Texture sprintBtnTexture = new Texture(Gdx.files.internal("sprint.png"));
        ImageButton.ImageButtonStyle sprintBtnStyle = new ImageButton.ImageButtonStyle();
        sprintBtnStyle.up = new TextureRegionDrawable(sprintBtnTexture);
        sprintBtn = new ImageButton(sprintBtnStyle);
        sprintBtn.setWidth(150f);
        sprintBtn.setHeight(150f);
        sprintBtn.setPosition(100f, 400f);
        stage.addActor(sprintBtn);

        // Jump Button
        Texture jumpBtnTexture = new Texture(Gdx.files.internal("jump.png"));
        ImageButton.ImageButtonStyle jumpBtnStyle = new ImageButton.ImageButtonStyle();
        jumpBtnStyle.up = new TextureRegionDrawable(jumpBtnTexture);
        jumpBtn = new ImageButton(jumpBtnStyle);
        jumpBtn.setWidth(150f);
        jumpBtn.setHeight(150f);
        jumpBtn.setPosition(Gdx.graphics.getWidth() - 170f, 350f);
        stage.addActor(jumpBtn);
        
        
        // Jump Button
        Texture crouchBtnTexture = new Texture(Gdx.files.internal("crouch.png"));
        ImageButton.ImageButtonStyle crouchBtnStyle = new ImageButton.ImageButtonStyle();
        crouchBtnStyle.up = new TextureRegionDrawable(crouchBtnTexture);
        crouchBtn = new ImageButton(crouchBtnStyle);
        crouchBtn.setWidth(150f);
        crouchBtn.setHeight(150f);
        crouchBtn.setPosition(Gdx.graphics.getWidth() - 170f, 150f);
        stage.addActor(crouchBtn);
        

        BitmapFont font = new BitmapFont();
        Label.LabelStyle lableStyle = new Label.LabelStyle(font, Color.BLACK);

        sprintBtn.addListener(
                new InputListener() {
                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {}

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        if (GAME.sprinting) {
                            GAME.sprinting = false;
                        } else {
                            GAME.isSprintBtnJustClicked = true;
                            GAME.sprinting = true;
                        GAME.isPlayerInCrouchPosition = false;
                        }
                        event.stop();

                        return true;
                    }
                });

        jumpBtn.addListener(
                new InputListener() {
                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {}

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {

                        if (GAME.isJumping == false) {
                           GAME.isPlayerInCrouchPosition = false;
                            GAME.isJumping = true;
                        GAME.isJumpGoingUp = true;
                        
                        }
                        event.handle();

                        return true;
                    }
                });
        
        
        crouchBtn.addListener(
                new InputListener() {
                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {}

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {

                        if (GAME.isPlayerInCrouchPosition == false) {
                           
                            GAME.isPlayerInCrouchPosition = true;
                        GAME.sprinting = false;
                    //        GAME.isJumping = false;
                        
                        }else{
                            GAME.isPlayerInCrouchPosition = false;
                        }
                        event.handle();

                        return true;
                    }
                });
        
        
        
    }
}
