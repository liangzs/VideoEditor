#version 100
precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
uniform vec2 direction;

void main(){
    vec2 p = textureCoordinate +  progress*sign(direction);
    float m =step(0.0, p.y) * step(p.y, 1.0) * step(0.0, p.x) * step(p.x, 1.0);
    vec4 color = mix(texture2D(vTexture, textureCoordinate), texture2D(vTexture1, textureCoordinate), m);
    gl_FragColor = vec4(color);
}