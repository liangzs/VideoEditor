precision mediump float;

varying mediump vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform float strength;
void main()
{
    vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;
    vec4 textureColorRes = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=textureColorRes.a*(1.0/(textureColorRes.a+0.00001));
    texel = vec3(dot(vec3(0.3, 0.6, 0.1), texel));
    texel = vec3(texture2D(inputImageTexture2, vec2(texel.r, .16666)).r);
    vec4 resultColor = vec4(texel, 1.0*resultAlpha);
    gl_FragColor =mix(textureColorRes, resultColor, strength);
}