precision mediump float;

varying mediump vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform float strength;

 void main()
 {
     vec4 originalTextureColor = texture2D(inputImageTexture, textureCoordinate);
     float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));
     vec3 texel = vec3(
                  texture2D(inputImageTexture2, vec2(originalTextureColor.r, .16666)).r,
                  texture2D(inputImageTexture2, vec2(originalTextureColor.g, .5)).g,
                  texture2D(inputImageTexture2, vec2(originalTextureColor.b, .83333)).b);
     gl_FragColor = mix(originalTextureColor,vec4(texel, 1.0)*resultAlpha,strength);
 }
