precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
uniform float ratio;//很神奇的，如果ratio=1的话，是像开门一样的效果
void main(){
    vec2 ratio2 = vec2(1.0, ratio);
    float s = pow(2.0 * abs(progress), 3.0);
    float dist = length((vec2(textureCoordinate) - 0.5) * ratio2);
    gl_FragColor= mix(texture2D(vTexture, textureCoordinate),
    texture2D(vTexture1, textureCoordinate),
    step(s, dist)
    );



//
//    float ratio=1920.0/1080.0;
//    float progress=time;
//    vec2 ratio2 = vec2(1.0, 1.0 / ratio);
//    float s = pow(2.0 * abs(progress), 3.0);
//    float dist = length((vec2(uv) - 0.5) * ratio2);
//
//    gl_FragColor= mix(
//    texture2D(u_Sampler0, uv) ,
//    texture2D(u_Sampler1, uv),
//    step(s, dist)
//    );

}