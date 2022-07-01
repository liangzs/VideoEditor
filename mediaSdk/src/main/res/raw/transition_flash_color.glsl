precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
uniform vec3 color;
const float colorPhase=.4;

void main(){
    gl_FragColor= mix(
    mix(vec4(color, 1.0), texture2D(vTexture1, textureCoordinate), smoothstep(1.0-colorPhase, 0.0, progress)),
    mix(vec4(color, 1.0), texture2D(vTexture, textureCoordinate), smoothstep(colorPhase, 1.0, progress)),
    progress);
}