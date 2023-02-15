precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

#define FLIP_IMAGE

float rand(vec2 uv) {
    float a = dot(uv, vec2(92., 80.));
    float b = dot(uv, vec2(41., 62.));
    float x = sin(a) + cos(b) * 51.;
    return fract(x);
}

void main(){
    vec2 uv = textureCoordinate;
    vec2 rnd = vec2(rand(uv), rand(uv));
    uv += rnd * .05;
    gl_FragColor = texture2D(inputImageTexture, mix(textureCoordinate, uv, strength));
}