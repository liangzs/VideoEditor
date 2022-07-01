precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
uniform float count;
uniform float smoothness;

void main(){
    float pr = smoothstep(-smoothness, 0.0, textureCoordinate.x - progress * (1.0 + smoothness));
    float s = step(pr, fract(count * textureCoordinate.x));
    vec4 color = mix(texture2D(vTexture1, textureCoordinate), texture2D(vTexture, textureCoordinate),s);
    gl_FragColor = vec4(color);
}