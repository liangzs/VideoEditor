precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
const float smoothness=0.1;
const bool  opening = true;//true是打开形式，false是关闭形式
const vec2 center = vec2(0.5, 0.5);
const float SQRT_2 = 1.414213562373;
void main(){
    float x = opening ? progress : 1.-progress;
    float m = smoothstep(-smoothness, 0.0, SQRT_2*distance(center, textureCoordinate) - x*(1.+smoothness));
    gl_FragColor= mix(texture2D(vTexture1, textureCoordinate), texture2D(vTexture, textureCoordinate), opening ? 1.-m : m);
}