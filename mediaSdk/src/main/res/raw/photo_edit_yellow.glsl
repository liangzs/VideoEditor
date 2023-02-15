precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

void main(){
    vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=textureColor.a*(1.0/(textureColor.a+0.00001));
    float red = dot(textureColor.rgb, vec3(1.0, 0, 0));
    float green = dot(textureColor.rgb, vec3(0, 1.0, 0));

    float blue = dot(textureColor.rgb, vec3(0, 0, 0));
    gl_FragColor = mix(textureColor, vec4(red, green, blue, 1.0)*resultAlpha, strength);
}