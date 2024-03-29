package aman.three;

import aman.three.Environment.RocksAndTrees;
import aman.three.terrains.HeightMapTerrain;
import aman.three.terrains.Terrain;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.FogAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRDepthShader;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class MyGame extends ApplicationAdapter {
    public SceneManager sceneManager;
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
    public Terrain terrain;
    Scene terrainScene;
    public Renderable terrainRenderable;
    RocksAndTrees rocksAndTreesGenerator;

    // Player Movement
    boolean isSprintBtnJustClicked = false;
    boolean sprinting = false;
    boolean isWalking = false;
    boolean isJumping = false;
    boolean isJumpGoingUp = false;
    boolean isPlayerFalling = false;
    boolean isPlayerInCrouchPosition = false;

    // player controller
    PlayerController playerController;

    // Stage & batch
    public ModelBatch modelBatch;
    Batch batch;
    Stage stage;
    
    

    // HUD
    // HUD
    HUD hud;

    // touchpad
    public TouchPad touchpad;

    // only for debugging purposes
    CameraInputController cameraInputController;
    boolean isCameraDebugging = false;
    Label FPSCounter;
    boolean fogEnabled = false;

    @Override
    public void create() {
        // Create Sprite and a Stage
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        modelBatch = new ModelBatch();

        FPSCounter = new Label("FPS Counter", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        FPSCounter.setFontScale(2);
        FPSCounter.setPosition(20, 100);
        stage.addActor(FPSCounter);

        playerController = new PlayerController();

        hud = new HUD();
        hud.initializeHUD(this, stage);

        // create scene
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/Worker_Male.gltf"));
        playerScene = new Scene(sceneAsset.scene);

        PBRShaderConfig config = PBRShaderProvider.createDefaultConfig();
        config.numBones = 70;
        config.numDirectionalLights = 1;
        config.numPointLights = 0;

        PBRDepthShader.Config depthConfig = PBRShaderProvider.createDefaultDepthConfig();
        depthConfig.numBones = 70;

        sceneManager =
                new SceneManager(
                        new MyPBRShaderProvider(config), new MyPBRDepthShaderProvider(depthConfig));

        playerScene.modelInstance.transform.scale(2, 2, 2);
        playerScene.modelInstance.transform.translate(25, 0, 250);
        playerScene.modelInstance.transform.rotate(Vector3.Y, 180f);

        sceneManager.addScene(playerScene);

        camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.1f;
        if (fogEnabled) camera.far = 200f;
        else camera.far = 1200f;

        sceneManager.setCamera(camera);
        camera.position.set(0, 100f, 0);

        // Gdx.input.setCursorCatched(true);
        // Gdx.input.setInputProcessor(this);

        cameraInputController = new CameraInputController(camera);
        cameraInputController.pinchZoomFactor = 200;
        cameraInputController.scrollFactor = 100;

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (isCameraDebugging) inputMultiplexer.addProcessor(cameraInputController);
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
        sceneManager.environment.set(new ColorAttribute(ColorAttribute.Fog, 0.7f, 0.7f, 0.7f, 1f));
        // sceneManager.environment.set(new FogAttribute(FogAttribute.FogEquation).set(10, 200, 1));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        if (!fogEnabled) sceneManager.setSkyBox(skybox);

        playerController.createContoller(this);
        createTerrain();

        rocksAndTreesGenerator = new RocksAndTrees();
        rocksAndTreesGenerator.populateTrees(this , modelBatch);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;
        FPSCounter.setText("FPS : " + Gdx.graphics.getFramesPerSecond());

        if (!isCameraDebugging) {
            playerController.processInput(deltaTime);

            playerController.updateCamera();
        }

        // render
        Gdx.gl20.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(terrainRenderable);
        modelBatch.end();

        sceneManager.update(deltaTime);
        sceneManager.render();

        stage.act(deltaTime);
        stage.draw();

        if (isJumping) {
            playerScene.animationController.animate("Jump", 0.5f);
        } else if (sprinting) {
            playerScene.animationController.animate("Run", 0.5f);
        } else if (isWalking) {
            playerScene.animationController.animate("Walk", 0.5f);
        } else if (isPlayerInCrouchPosition) {
            playerScene.animationController.animate("SitDown", 0.5f);
        } else {
            playerScene.animationController.animate("Idle", -1);
        }
    }

    private void createTerrain() {
        terrain =
                new HeightMapTerrain(new Pixmap(Gdx.files.internal("textures/heightmap.png")), 50f);
        terrainRenderable = terrain.getRenderable();
        //                terrainScene = new Scene(terrain.getModelInstance());
        //                sceneManager.addScene(terrainScene);
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
        terrain.dispose();
    }
}
