precision highp float;
varying highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float strength;

void main()
{
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    float c = color.r*0.33+color.g*0.33+color.b*0.33;
    gl_FragColor = mix(color,vec4(c,c,c, 1.0),strength)*resultAlpha;
}