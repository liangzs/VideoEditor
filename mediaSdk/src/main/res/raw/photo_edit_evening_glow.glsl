precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

void main(){
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    vec4 color1 = vec4(0.6, 0.2, 0.98, 1.0)*resultAlpha;
    gl_FragColor = mix(color, color1, 0.4*strength);
}