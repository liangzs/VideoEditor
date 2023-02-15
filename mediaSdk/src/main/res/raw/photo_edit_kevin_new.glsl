precision mediump float;

varying mediump vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform float strength;

void main()
{
    vec4 orginalTexelColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=orginalTexelColor.a*(1.0/(orginalTexelColor.a+0.00001));
    vec3 texel = orginalTexelColor.rgb;

    vec2 lookup;
    lookup.y = 0.5;

    lookup.x = texel.r;
    texel.r = texture2D(inputImageTexture2, lookup).r;

    lookup.x = texel.g;
    texel.g = texture2D(inputImageTexture2, lookup).g;

    lookup.x = texel.b;
    texel.b = texture2D(inputImageTexture2, lookup).b;

    gl_FragColor = vec4(mix(orginalTexelColor.rgb, texel, strength), 1.0)*resultAlpha;
}
