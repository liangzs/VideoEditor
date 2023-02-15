precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

void main(){
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    gl_FragColor = mix(color, vec4(0.75, 0.75, 0.75, 1.0)*resultAlpha, 0.6*strength);
}