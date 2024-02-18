// trimmed version for testing

attribute vec3 a_position;
uniform mat4 u_projViewWorldTrans;
uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;



#if defined(diffuseTextureFlag) && defined(blendedFlag)
#define blendedTextureFlag
attribute vec2 a_texCoord0;
varying vec2 v_texCoords0;
#endif


#ifdef PackedDepthFlag
varying float v_depth;
#endif //PackedDepthFlag

// MS
#if defined(instanced)
attribute vec4 i_offset;       // instanced data (X, scale Y, Z, rotation angle around Y)

// MS
mat2 rotate(float angle) {
    return mat2(
    cos(angle), -sin(angle),
    sin(angle), cos(angle)
    );
}
#endif // instanced


void main() {
    #ifdef blendedTextureFlag
    v_texCoords0 = a_texCoord0;
    #endif // blendedTextureFlag




    vec3 morph_pos = a_position;




    vec4 pos = u_worldTrans * vec4(morph_pos, 1.0);

    // MS
    #if defined(instanced)
    pos.xz = rotate(i_offset.w)*pos.xz;                         // rotate around Y axis
  //  pos.y *= i_offset.y;                                        // scale in Y direction
    pos += vec4(i_offset.x, i_offset.y, i_offset.z, 0);                  // offset in horizontal plane
    #endif


    //v_position = vec3(pos.xyz) / pos.w;
    pos = u_projViewTrans * pos;


    #ifdef PackedDepthFlag
    v_depth = pos.z / pos.w * 0.5 + 0.5;
    #endif //PackedDepthFlag

    gl_Position = pos;
}
