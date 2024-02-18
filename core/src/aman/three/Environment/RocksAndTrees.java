package aman.three.Environment;

import aman.three.MyGame;
import aman.three.terrains.Terrain;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.VertexAttribute;

import com.badlogic.gdx.Gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.mygame.PoissonDistribution;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Application;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.nio.Buffer;
import java.nio.FloatBuffer;

public class RocksAndTrees extends MyGame {
    Terrain terrain;
    SceneManager sceneManager;
    AssetManager assetManager;
    ModelBatch modelBatch;

    Scene tree4Scene;
    
    
    
    
    Array<Vector2> tree1PositionArray = new Array<>();
    Array<Vector2> tree2PositionArray = new Array<>();
    Array<Vector2> tree3PositionArray = new Array<>();
    Array<Vector2> tree4PositionArray = new Array<>();

    Array<Vector2> rock1PositionArray = new Array<>();
    Array<Vector2> rock2PositionArray = new Array<>();
    Array<Vector2> rock3PositionArray = new Array<>();

    Array<Vector2> bush1PositionArray = new Array<>();
    Array<Vector2> bush2PositionArray = new Array<>();
    Array<Vector2> bush3PositionArray = new Array<>();

    Array<Vector2> grass1PositionArray = new Array<>();
    Array<Vector2> grass2PositionArray = new Array<>();
    Array<Vector2> grass3PositionArray = new Array<>();
    
    
    public void populateTrees(MyGame mygame , ModelBatch modelBatch) {
        this.terrain = mygame.terrain;
        this.sceneManager = mygame.sceneManager;
        assetManager = new AssetManager();
        this.modelBatch = modelBatch;
        
        

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

        SceneAsset tree1SceneAssets = assetManager.get("models/Tree Pack/tree1.glb");
        Scene tree1Scene = new Scene(tree1SceneAssets.scene);

        SceneAsset tree2SceneAssets = assetManager.get("models/Tree Pack/tree2.glb");
        Scene tree2Scene = new Scene(tree2SceneAssets.scene);

        SceneAsset tree3SceneAssets = assetManager.get("models/Tree Pack/tree3.glb");
        Scene tree3Scene = new Scene(tree3SceneAssets.scene);

        SceneAsset tree4SceneAssets = assetManager.get("models/Tree Pack/tree4.glb");
        tree4Scene = new Scene(tree4SceneAssets.scene);

        SceneAsset rock1SceneAsset = assetManager.get("models/Tree Pack/rock1.glb");
        Scene rock1Scene = new Scene(rock1SceneAsset.scene);

        SceneAsset rock2SceneAsset = assetManager.get("models/Tree Pack/rock2.glb");
        Scene rock2Scene = new Scene(rock2SceneAsset.scene);

        SceneAsset rock3SceneAsset = assetManager.get("models/Tree Pack/rock3.glb");
        Scene rock3Scene = new Scene(rock3SceneAsset.scene);

        SceneAsset bush1SceneAsset = assetManager.get("models/Tree Pack/bush1.glb");
        Scene bush1Scene = new Scene(bush1SceneAsset.scene);

        SceneAsset bush2SceneAsset = assetManager.get("models/Tree Pack/bush2.glb");
        Scene bush2Scene = new Scene(bush2SceneAsset.scene);

        SceneAsset bush3SceneAsset = assetManager.get("models/Tree Pack/bush3.glb");
        Scene bush3Scene = new Scene(bush3SceneAsset.scene);

        SceneAsset grass1SceneAsset = assetManager.get("models/Tree Pack/grass1.glb");
        Scene grass1Scene = new Scene(grass1SceneAsset.scene);

        SceneAsset grass2SceneAsset = assetManager.get("models/Tree Pack/grass2.glb");
        Scene grass2Scene = new Scene(grass2SceneAsset.scene);

        SceneAsset grass3SceneAsset = assetManager.get("models/Tree Pack/grass3.glb");
        Scene grass3Scene = new Scene(grass3SceneAsset.scene);

        fillPositionArrays();
        
        
        
        
        

        if (assetManager.isFinished()) {

           sceneManager.addScene(tree1Scene);
            for (int i = 0; i < tree1Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = tree1Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedTree1Mesh(mesh);
            }

            sceneManager.addScene(tree2Scene);
            for (int i = 0; i < tree2Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = tree2Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedTree2Mesh(mesh);
            }

            sceneManager.addScene(tree3Scene);
            for (int i = 0; i < tree3Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = tree3Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedTree3Mesh(mesh);
            }

            sceneManager.addScene(tree4Scene);
            for (int i = 0; i < tree4Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = tree4Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedTree4Mesh(mesh);
            }

            sceneManager.addScene(rock1Scene);
            for (int i = 0; i < rock1Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = rock1Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedRock1Mesh(mesh);
            }

            sceneManager.addScene(rock2Scene);
            for (int i = 0; i < rock2Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = rock2Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedRock2Mesh(mesh);
            }

            sceneManager.addScene(rock3Scene);
            for (int i = 0; i < rock3Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = rock3Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedRock3Mesh(mesh);
            }

            sceneManager.addScene(bush1Scene);
            for (int i = 0; i < bush1Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = bush1Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedBush1Mesh(mesh);
            }

            sceneManager.addScene(bush2Scene);
            for (int i = 0; i < bush2Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = bush2Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedBush2Mesh(mesh);
            }

            sceneManager.addScene(bush3Scene);
            for (int i = 0; i < bush3Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = bush3Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedBush3Mesh(mesh);
            }

            sceneManager.addScene(grass1Scene);
            for (int i = 0; i < grass1Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = grass1Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedGrass1Mesh(mesh);
            }

            sceneManager.addScene(grass2Scene);
            for (int i = 0; i < grass2Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = grass2Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedGrass2Mesh(mesh);
            }

            sceneManager.addScene(grass3Scene);
            for (int i = 0; i < grass3Scene.modelInstance.nodes.first().parts.size; i++) {
                Mesh mesh = grass3Scene.modelInstance.nodes.first().parts.get(i).meshPart.mesh;
                setupInstancedGrass3Mesh(mesh);
            }
            
        }
    }
    
    
    
    

    private void setupInstancedTree1Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                tree1PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(tree1PositionArray.size * 4);

        for (Vector2 position : tree1PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedTree2Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                tree2PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(tree2PositionArray.size * 4);

        for (Vector2 position : tree2PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedTree3Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                tree3PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(tree3PositionArray.size * 4);

        for (Vector2 position : tree3PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedTree4Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                tree4PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(tree4PositionArray.size * 4);

        for (Vector2 position : tree4PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedRock1Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                rock1PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(rock1PositionArray.size * 4);

        for (Vector2 position : rock1PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedRock2Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                rock2PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(rock2PositionArray.size * 4);

        for (Vector2 position : rock2PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedRock3Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                rock3PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(rock3PositionArray.size * 4);

        for (Vector2 position : rock3PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedBush1Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                bush1PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(bush1PositionArray.size * 4);

        for (Vector2 position : bush1PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedBush2Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                bush2PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(bush2PositionArray.size * 4);

        for (Vector2 position : bush2PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedBush3Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                bush3PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(bush3PositionArray.size * 4);

        for (Vector2 position : bush3PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedGrass1Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                grass1PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(grass1PositionArray.size * 4);

        for (Vector2 position : grass1PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedGrass2Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                grass2PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(grass2PositionArray.size * 4);

        for (Vector2 position : grass2PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    private void setupInstancedGrass3Mesh(Mesh mesh) {

        // add 4 floats per instance
        mesh.enableInstancedRendering(
                true,
                grass3PositionArray.size,
                new VertexAttribute(VertexAttributes.Usage.Position, 4, "i_offset"));

        // Create offset FloatBuffer that will contain instance data to pass to shader
        FloatBuffer offsets = BufferUtils.newFloatBuffer(grass3PositionArray.size * 4);

        for (Vector2 position : grass3PositionArray) {
            float angle = 0f; // random rotation around Y (up) axis

            offsets.put(new float[] {position.x, Y(position.x, position.y), position.y, angle});
        }

        ((Buffer) offsets).position(0);
        mesh.setInstanceData(offsets);
    }

    public float randomPos() {
        return MathUtils.random(7f, 970f);
    }

    public float grassRandomPos() {
        return MathUtils.random(300f, 700f);
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

    public void fillPositionArrays() {
        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            rock1PositionArray.add(position);
        }

        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            rock2PositionArray.add(position);
        }
        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            rock3PositionArray.add(position);
        }

        for (int i = 0; i < 1700; ++i) {

            Vector2 position = new Vector2(grassRandomPos(), grassRandomPos());
            grass1PositionArray.add(position);
        }

        for (int i = 0; i < 1700; ++i) {

            Vector2 position = new Vector2(grassRandomPos(), grassRandomPos());
            grass2PositionArray.add(position);
        }

        for (int i = 0; i < 1700; ++i) {

            Vector2 position = new Vector2(grassRandomPos(), grassRandomPos());
            grass3PositionArray.add(position);
        }

        for (int i = 0; i < 170; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            bush1PositionArray.add(position);
        }

        for (int i = 0; i < 170; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            bush2PositionArray.add(position);
        }

        for (int i = 0; i < 170; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            bush3PositionArray.add(position);
        }

        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            tree1PositionArray.add(position);
            tree1PositionArray.add(position);
        }

        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            tree2PositionArray.add(position);
            tree2PositionArray.add(position);
        }

        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            tree3PositionArray.add(position);
            tree3PositionArray.add(position);
        }

        for (int i = 0; i < 100; ++i) {

            Vector2 position = new Vector2(randomPos(), randomPos());
            tree4PositionArray.add(position);
            tree4PositionArray.add(position);
        }
    }

    public float Y(float x, float z) {
        return terrain.getHeightAtWorldCoord(x, z);
    }
}
