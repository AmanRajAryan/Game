package aman.three;

import aman.three.HUD;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class MyGame extends ApplicationAdapter implements AnimationController.AnimationListener {
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    public Scene playerScene;
    public PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    // Player Movement
    boolean isSprintBtnJustClicked = false;
    boolean isJumpBtnJustClicked = false;
    boolean sprinting = false;
    boolean isWalking = false;
    boolean isJumping = false;
    boolean isJumpingAnimationInProgress = false;

    // player controller
    PlayerController playerController;

    // Stage & batch
    Batch batch;
    Stage stage;

    // HUD
    // HUD
    HUD hud;

    // touchpad
    public TouchPad touchpad;

    @Override
    public void create() {
        // Create Sprite and a Stage
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);

        playerController = new PlayerController();

        hud = new HUD();
        hud.initializeHUD(this, stage);

        // create scene
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/Worker_Male.gltf"));
        playerScene = new Scene(sceneAsset.scene);

        // create scene

        PBRShaderConfig config = PBRShaderProvider.createDefaultConfig();
        config.numBones = 128;

        DepthShader.Config depthConfig = PBRShaderProvider.createDefaultDepthConfig();
        depthConfig.numBones = 128;

        sceneManager =
                new SceneManager(
                        //     new PBRShaderProvider(config), new
                        // PBRDepthShaderProvider(depthConfig)
                        );

        playerScene.modelInstance.transform.scale(2, 2, 2);
        playerScene.modelInstance.transform.rotate(Vector3.Y, 180f);

        sceneManager.addScene(playerScene);

        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 1000f;
        sceneManager.setCamera(camera);
        camera.position.set(0, 5f, 4f);

        // Gdx.input.setCursorCatched(true);
        // Gdx.input.setInputProcessor(this);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(
                new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
        playerController.createContoller(this);
        buildBoxes();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;

        playerController.processInput(deltaTime);

        playerController.updateCamera();

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        sceneManager.render();

        stage.act(deltaTime);
        stage.draw();

        if (isWalking) {
            playerScene.animationController.animate("Walk", -1);
        } else if (sprinting) {
            playerScene.animationController.animate("Run", -1);
        } else if(isJumpingAnimationInProgress) {
        	playerScene.animationController.animate("Jump" , -1);
        }
        
       
        else {
            playerScene.animationController.animate("Idle", -1);
        }
    }

    private void buildBoxes() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        for (int x = -100; x < 100; x += 10) {
            for (int z = -100; z < 100; z += 10) {
                Material material = new Material();

                material.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
                MeshPartBuilder builder =
                        modelBuilder.part(
                                x + ", " + z,
                                GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                                material);
                BoxShapeBuilder.build(builder, x, 0, z, 1f, 1f, 1f);
            }
        }

        ModelInstance model = new ModelInstance(modelBuilder.end());
        sceneManager.addScene(new Scene(model));
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {}

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {}
}
