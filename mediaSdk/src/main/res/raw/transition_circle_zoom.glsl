precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
uniform float ratio;
void main(){
    vec2 ratio2 = vec2(1.0, 1.0);
    float s = pow(2.0 * abs(progress), 3.0);
    float dist = length((vec2(textureCoordinate) - 0.5) * ratio2);
    gl_FragColor= mix(texture2D(vTexture, textureCoordinate),
    texture2D(vTexture1, textureCoordinate),
    step(s, dist)
    );
}