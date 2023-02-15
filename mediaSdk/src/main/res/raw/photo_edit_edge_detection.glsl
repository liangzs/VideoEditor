#extension GL_OES_standard_derivatives : enable
varying highp vec2 textureCoordinate;
precision highp float;
uniform float strength;

uniform sampler2D inputImageTexture;

void main() {
    vec4 color =  texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    float gray = length(color.rgb);
    gl_FragColor = mix(color, vec4(vec3(step(0.06, length(vec2(dFdx(gray), dFdy(gray))))), 1.0)*resultAlpha, strength);
}