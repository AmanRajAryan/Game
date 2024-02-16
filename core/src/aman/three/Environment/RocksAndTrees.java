package aman.three.Environment;

import aman.three.terrains.Terrain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.MathUtils;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class RocksAndTrees {
    Terrain terrain;
    SceneManager sceneManager;
    AssetManager assetManager;

    public void populateTrees(SceneManager sceneManager, Terrain terrain) {
        this.terrain = terrain;
        this.sceneManager = sceneManager;
        assetManager = new AssetManager();

        assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
        assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());

        assetManager.load("models/Tree Pack/tree1.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/tree2.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/tree3.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/tree4.glb", SceneAsset.class);
        
        assetManager.load("models/Tree Pack/bush1.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/bush2.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/bush3.glb", SceneAsset.class);
        
        assetManager.load("models/Tree Pack/rock1.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/rock2.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/rock3.glb", SceneAsset.class);
        
        assetManager.load("models/Tree Pack/grass1.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/grass2.glb", SceneAsset.class);
        assetManager.load("models/Tree Pack/grass3.glb", SceneAsset.class);
        
        
        assetManager.finishLoading();

        if (assetManager.isFinished()) {
            int i = 0;
            while (i < 100) {
                i++;
                if (i < 50) {
                    createTreeOrRock("rock1", randomPos(), randomPos(), rockRandomScale());
                    createTreeOrRock("rock2", randomPos(), randomPos(), rockRandomScale());
                    createTreeOrRock("rock3", randomPos(), randomPos(), rockRandomScale());
                }
                
//                createTreeOrRock("grass1", randomPos(), randomPos(), bushRandomScale());
//                createTreeOrRock("grass2", randomPos(), randomPos(), bushRandomScale());
//                createTreeOrRock("grass3", randomPos(), randomPos(), bushRandomScale());

                
//                if (i < 70) {
//
//                    createTreeOrRock("bush1", randomPos(), randomPos(), bushRandomScale());
//                    createTreeOrRock("bush2", randomPos(), randomPos(), bushRandomScale());
//                    createTreeOrRock("bush2", randomPos(), randomPos(), bushRandomScale());
//                }
                
                if (i < 30) {
                    createTreeOrRock("tree1", randomPos(), randomPos(), treeRandomScale());
                    createTreeOrRock("tree2", randomPos(), randomPos(), treeRandomScale());
                    createTreeOrRock("tree3", randomPos(), randomPos(), treeRandomScale());
                    createTreeOrRock("tree4", randomPos(), randomPos(), treeRandomScale());
                }
            }
        }
    }

    public float randomPos() {
        return MathUtils.random(7f, 970f);
    }

    public float rockRandomScale() {
        return MathUtils.random(3f, 5f);
    }

    public float treeRandomScale() {
        return MathUtils.random(4f, 8f);
    }

    public float bushRandomScale() {
        return MathUtils.random(1f, 3f);
    }

    public void createTreeOrRock(String fileName, float x, float z, float scale) {
        SceneAsset treeSceneAssets =
                assetManager.get("models/Tree Pack/" + fileName + ".glb", SceneAsset.class);
        Scene treeScene = new Scene(treeSceneAssets.scene);
        treeScene.modelInstance.transform.setTranslation(x, Y(x, z), z);
        treeScene.modelInstance.transform.scale(scale, scale, scale);
        sceneManager.addScene(treeScene);
    }

    public float Y(float x, float z) {
        return terrain.getHeightAtWorldCoord(x, z);
    }
}
