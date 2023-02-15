precision mediump float;

varying mediump vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform float strength;
void main()
{

    vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;
    vec4 textureColorRes = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=textureColorRes.a*(1.0/(textureColorRes.a+0.000001));

    texel = vec3(
    texture2D(inputImageTexture2, vec2(texel.r, .16666*resultAlpha)).r,
    texture2D(inputImageTexture2, vec2(texel.g, .5*resultAlpha)).g,
    texture2D(inputImageTexture2, vec2(texel.b, .83333*resultAlpha)).b);

    vec3 resultColor = mix(textureColorRes.rgb, texel.rgb, strength);
    gl_FragColor = vec4(resultColor, 1.0)*resultAlpha;
}
