precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

vec3 desaturate(vec3 color, float f) {
    vec3 grayXfer = vec3(0.3, 0.59, 0.11);
    vec3 gray = vec3(dot(grayXfer, color));
    return mix(color, gray, f);
}

void main(){
    vec4 originalTextureColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));
    vec3 ic = texture2D(inputImageTexture, textureCoordinate).rgb;
    vec3 nrm = normalize(vec3(1.0));
    float delta = dot(normalize(ic), nrm);
    delta = pow(delta, 10.0);
    vec3 color = desaturate(ic, delta);
    gl_FragColor = mix(originalTextureColor, vec4(color, 1.0)*resultAlpha, strength);
}