precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float strength;
void main() {
    vec4 centralColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=centralColor.a*(1.0/(centralColor.a+0.00001));
    vec4 resultColor = vec4((1.0 - centralColor.rgb)*resultAlpha, centralColor.w);
    gl_FragColor =mix(centralColor, resultColor, strength);
}