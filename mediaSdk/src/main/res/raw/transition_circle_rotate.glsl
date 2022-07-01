precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
//旋转分割线的模糊度
const float smoothness=1.0; // = 1.0
const highp float PI = 3.141592653589;

void main(){
    vec2 rp = textureCoordinate*2.-1.;
    gl_FragColor= mix(
    texture2D(vTexture, textureCoordinate),
    texture2D(vTexture1, textureCoordinate),
    smoothstep(0., smoothness, atan(rp.y,rp.x) - (progress-.5) * PI * 2.5)
    );
}