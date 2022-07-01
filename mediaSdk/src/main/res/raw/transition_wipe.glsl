precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
const float size=.4;// = 0.8

float rand (vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 437.5453);
}

void main(){
    float r = rand(vec2(0, textureCoordinate.y));
    float m = smoothstep(0.0, -size, textureCoordinate.x*(1.0-size) + size*r - (progress * (1.0 + size)));
    gl_FragColor= mix(
    texture2D(vTexture1, textureCoordinate),
    texture2D(vTexture, textureCoordinate),
    m
    );
}