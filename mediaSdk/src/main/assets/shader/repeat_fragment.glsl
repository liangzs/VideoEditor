#version 100
precision mediump float;
varying vec2 textureCoordinate;
varying float vAlpha;
varying float vPhase;
uniform sampler2D vTexture;

void main() {
    float x = textureCoordinate.x + vPhase;
    x = mod(x, 1.0);
    gl_FragColor=texture2D(vTexture, vec2(x, textureCoordinate.y)) * vAlpha;

}