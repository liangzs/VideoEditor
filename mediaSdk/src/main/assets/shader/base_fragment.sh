#version 100
precision mediump float;
varying vec2 textureCoordinate;
varying float vAlpha;
uniform sampler2D vTexture;
void main() {
    gl_FragColor = texture2D(vTexture, textureCoordinate)*vAlpha;
}