package aman.three;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import net.mgsx.gltf.scene3d.shaders.PBRShader;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;

public class MyPBRShaderProvider extends PBRShaderProvider {
    public MyPBRShaderProvider(PBRShaderConfig config) {
        
        super(config);
    }

    @Override
    protected PBRShader createShader(Renderable renderable, PBRShaderConfig config, String prefix){
        if( renderable.meshPart.mesh.isInstanced()) {
            prefix += "#define instanced\n";
        }
        config.vertexShader = Gdx.files.internal("shaders/pbr-instanced.vs.glsl").readString();
        return new MyPBRShader(renderable, config, prefix);
        
    }

}
